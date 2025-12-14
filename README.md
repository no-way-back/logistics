# SAGA Pattern 구현

## ✦ 실행 방법

1. 각 마이크로 서비스와 데이터베이스, Kafka 실행
```bash
docker-compose up -d
```

2. 주문 생성 테스트
  - Endpoint : `POST http://localhost:18085/orders`
  - Headers :
    - `X-User-Id`: 00000000-0000-0000-0000-000000000000
    - `X-User-Role`: MASTER
    - `X-Username`: tester
      
  - Body (JSON)
      ```json
      {
        "supplier": {
          "supplierCompanyId": "00000000-0000-0000-0000-000000000001",
          "name": "공급업체 A",
          "address": "서울특별시 강남구 테헤란로 123",
          "detailAddress": "7층 A-702",
          "contact": "02-1234-5678"
        },
        "receiver": {
          "receiverCompanyId": "00000000-0000-0000-0000-000000000002",
          "name": "수령업체 B",
          "address": "경기도 성남시 분당구 판교역로 235",
          "detailAddress": "3층 물류센터",
          "contact": "031-987-6543"
        },
        "request": "빠른 출고 요청드립니다.",
        "orderItems": [
          {
            "productId": "00000000-0000-0000-0000-000000000000",
            "name": "초코바 24입",
            "price": 12900,
            "quantity": 3
          },
          {
            "productId": "00000000-0000-0000-0000-000000000001",
            "name": "사과 주스 1L",
            "price": 3200,
            "quantity": 10
          }
        ]
      }
      ```

## ✦ 테스트 결과
- 주문 처리 흐름
  
  ① `PENDING` 상태에서 주문 발행 이벤트 발생
  
  ② 상품 재고 감소 이벤트 발행, Saga 상태를 `STOCK_REQUESTED`로 변경
  
  ③ 상품 서비스에서 재고 감소 처리 후 결과 이벤트 발행
  
  ④ 재고 감소 성공에 대한 이벤트를 수신한 뒤, Saga 상태를 `STOCK_REDUCED`로 변경
  
  ⑤ 결제 생성에 대한 이벤트를 발행, Saga 상태를 `PAYMENT_REQUESTED`로 변경
  
  ⑥ 결제 서비스에서 결제 생성 처리 후 결과 이벤트 발행
  
  ⑦ 결제 생성에 대한 이벤트를 수신한 뒤, Saga 상태를 `PAYMENT_SUCCEEDED`로 변경
     
  <img width="1418" height="49" alt="image" src="https://github.com/user-attachments/assets/6f78c56d-323b-4ff3-9a3b-55cf911c17a8" />

- 이벤트 관련 테스트 코드
  - [🔗 EventFlowIntegrationTest.java](https://github.com/no-way-back/logistics/blob/feat/saga-pattern/order/src/test/java/com/nowayback/order/event/EventFlowIntegrationTest.java)
  - [🔗 EventIdempotencyIntegrationTest.java](https://github.com/no-way-back/logistics/blob/feat/saga-pattern/order/src/test/java/com/nowayback/order/event/EventIdempotencyIntegrationTest.java)


## ✦ 회고

### 1. 설계 vs 구현

**1-1. 책임 분리**

  - 이벤트 발행/처리를 각 Service에서 처리할 경우 전체 흐름 파악이 어려움
    
  - 현재는 `SagaStateService`에서 이벤트 발행/처리를 담당
    
  - 문제점 :
    - `SagaStateService`에서 `kafkaEventPublisher`로 직접 이벤트를 발행하므로, 비즈니스 로직과 같은 트랜잭션에서 실행되고 있음
    - DB 트랜잭션 실패 시 Kafka 이벤트는 이미 발행됨 → 이벤트 유실 가능
    - DB를 커밋한 후 이벤트를 발행할 수 있도록 개선이 필요

**1-2. 멱등성**
  - 설계 : 같은 요청이 여러번 오더라도 한 번만 처리하도록 강제하는 키(멱등 키)를 사용
    
  - 구현 :
    - `eventId` 기준으로 동일 이벤트 중복 처리 방지
    
    - Kafka 특성상 이벤트 중복 가능성 방지 가능
      
    - 비즈니스 단위 멱등성을 완전히 보장하지 못하는 상태
      
      `eventId`, `orderId` 단위의 처리를 고민하였으나 `orderId`의 경우 주문 취소, 환불 등에도 사용할 수 있으므로 적절치 않다고 판단
      
      `eventId`의 경우, 매번 새로운 `eventId`를 발행하므로 비즈니스 멱등성을 제어하지 못함


### 2. 구현하면서 어려웠던 점

**2-1. 이벤트 처리 흐름 기준**
  - 현재 구조 :
    
    ① Consumer 이벤트 수신 → EventDispatcher 전달
    
    ② EventDispatcher → processed_events 저장 → EventHandler로 전달
    
    ③ EventHandler → SagaStateService로 이벤트 처리 → Saga 처리
       
  - 이벤트 처리 범위를 어디까지 할 지 기준 잡는 것에 대해 어려움을 느낌
    
**2-2. 중복 이벤트 처리**
- 문제 상황
  - 동일한 이벤트가 여러 번 전달되는 경우를 고려
  - 동시에 이벤트가 수신되면, 애플리케이션에서 중복 여부를 판단할 때 두 요청 모두 "중복 아님"으로 판단되는 레이스 컨디션이 발생

- 원인
  - 초기 구현 시, 이벤트 중복 여부를 애플리케이션 로직으로만 판단 (`DataIntegrityViolationException`)
  - 데이터베이스 insert나 update 전, 이미 비즈니스 로직이 수행되므로 트랜잭션 단위의 중복 방지가 불가
    
- 해결 :
  
  - Postgres의 ON ONFLICT DO NOTHING 구문 활용
    
    - DB insert 시점에서 eventId 중복 여부를 원자적으로 판단
    - 
      ```java
        @Modifying
        @Query(value = """
            INSERT INTO order_service.processed_events (event_id, event_type, received_at)
            VALUES (:eventId, :eventType, NOW())
            ON CONFLICT (event_id) DO NOTHING
            """, nativeQuery = true)
        int insertIgnore(
            @Param("eventId") UUID eventId,
            @Param("eventType") String eventType
        );
      ```

### 3. 실무 적용 시 고려 사항
3-1. 이벤트 발행과 트랜잭션 분리

- 문제 :
  - DB 커밋 실패 시 이벤트는 이미 발행 → 유실 가능
    
- 해결 :
  - Transactional Outbox Pattern 도입
  - Debezium 등 CDC 도구 사용 → DB 변경 사항을 Kafka로 자동 발행
 
3-2. Saga 타임아웃 처리 부재
- 결제/재고 서비스 미응답 시 주문 상태가 계속 `PENDING`으로 남음
  
- 개선 :
  - `saga_states`에 `timeout_at` 컬럼 추가
  - 스케줄러로 주기적 타임아웃 확인 및 보상 트랜잭션 처리

3-3. 이벤트 순서 보장
- 현재는 이벤트 수신 시 비즈니스 로직 처리 후 Saga, Order 상태를 변경하도록 처리
  
- 이벤트 수신 순서가 의도대로 동작하지 않을 경우 상태 전이가 검증 없이 처리될 수 있음
  
- 개선 :
  - 허용 가능한 상태 전이 관계를 명시적으로 검증 필요
