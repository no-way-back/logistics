# 이벤트 리스너 기반 이벤트 처리

### ✦ㅤ이벤트 가이드
- [이벤트 가이드](https://github.com/no-way-back/logistics/blob/feat/event-listener/EVENT_GUIDE.md)
	
### ✦ㅤ구현 내용
- [x] Step 1-2 : 이벤트 발행 및 `TransactionalEventListener`
- [x] Step 3 : 비동기 처리
- [x] Step 4 : 이벤트 체인
- [x] Step 5 : 패키지 구조화

  ```
  order
  │
  ├── common
  │   ├── config
  │   │   └── AsyncConfig.java
  │   ├── event
  │   │   ├── AsyncTransactionalEventListener.java
  │   │   ├── DomainEvent.java
  │   │   └── EventPublisher.java
  │   └── eventstore
  │       ├── EventStoreListener.java
  │       ├── StoredEvent.java
  │       └── StoredEventController.java
  │
  ├── order
  │   ├── application
  │   │   ├── OrderService.java
  │   │   └── eventlistener
  │   │       └── OrderEventListener.java
  │   ├── domain
  │   │   ├── entity
  │   │   └── event
  │   │       └── OrderCreatedEvent.java
  │   ├── infrastructure
  │   └── presentation
  │
  ├── payment
  │   ├── application
  │   │   ├── PaymentService.java
  │   │   └── eventlistener
  │   │       └── PaymentEventListener.java
  │   ├── domain
  │   │   ├── entity
  │   │   │   └── Payment.java
  │   │   └── event
  │   │       └── PaymentCompletedEvent.java
  │   └── infrastructure
  │
  └── product
      ├── application
      │   ├── ProductService.java
      │   └── eventlistener
      │       └── ProductEventListener.java
      ├── domain
      │   └── entity
      │       └── Product.java
      └── infrastructure
  ```


### ✦ㅤ주요 이벤트 목록
- `OrderCreatedEvent` : 주문 생성 시 발행
- `PaymentCompletedEvent` : 결제 완료 시 발행

---

### 1. 이벤트 기반 처리 흐름 및 시퀀스
- 이벤트 플로우 다이어그램
  
  <img width="607" height="1187" alt="event_flow_diagram" src="https://github.com/user-attachments/assets/17e11185-fbd4-4656-bb2e-792f7d96d456" />
- 이벤트 시퀀스 다이어그램
  
  <img width="3860" height="2652" alt="sequence_diagram" src="https://github.com/user-attachments/assets/e66fe890-63f2-43be-8438-c1c7878a1165" />
		
### 2. 실행 방법
- 빌드 및 실행 명령어

  ```bash
  cd order
  ./gradlew clean build
  ./gradlew bootRun
  ```
- 테스트 실행 방법
  
  ```bash
  cd order
  ./gradlew test
  ```

### 3. 회고
- **이벤트 기반 아키텍처를 구현하며 느낀 점**
  
  - 이벤트를 적용하여 서비스 간 결합도를 낮출 수 있었다.
  
  - 비동기 처리를 적용해봄으로써 장점과 단점을 체감할 수 있었다.

    - 결제와 재고 차감을 비동기로 처리함으로써, 주문 생성이라는 주요 기능에 지연이 발생하지 않음
    - 비동기로 처리된 부분에 대해 메인 스레드는 이를 알 수 없으므로 예외 처리 흐름 추적이 어려움
    - 트랜잭션이 각 스레드마다 별도로 관리되므로 예외 처리 로직을 구현해야 함

- **이벤트 설계의 장단점**
  
  - 장점
    - 서비스 간 결합도 감소 : 이벤트 발행자와 수신자가 서로를 알지 않아도 되므로 의존성이 낮아짐
    - 확장성 향상 : 기능을 새로 추가할 때, 기존 로직의 변경 없이 새로운 이벤트 리스너를 추가함으로써 구현 가능

  - 단점
    - 디버깅 및 테스트의 어려움 : 비동기로 이벤트를 처리할 경우, 이벤트의 흐름 추적이 어려움
    - 데이터 일관성 관리 : 트랜잭션이 분리되어있어 예외 발생 시 데이터 일관성이 깨질 수 있음<br>
      	AFTER_COMMIT 설정이나 Outbox 패턴 적용 등을 통해 후속 처리를 보장해야 함

- **어려웠던 부분과 해결 방법**
  - 트랜잭션과 `AFTER_COMMIT` 이벤트 호출
    - 발생 문제 : 이벤트 리스너에 `@TransactionalEventListener(phase = AFTER_COMMIT)`을 붙였으나, 이벤트를 발행하는 메서드에 `@Transactional`이 없으면 트랜잭션 커밋이 발생하지 않아 이벤트 리스너가 실행되지 않음
    - 해결 방법 : 이벤트를 발행하는 메서드에 `@Transactional`을 붙여 트랜잭션 커밋 시점에 이벤트 리스너가 수행되도록 함

  - 이벤트 VO 설계
    - 발생 문제 : 이벤트에 담을 데이터 범위를 결정하기 어려움<br>
      최소 정보만 담게 되면 추가적인 조회를 필요로하며, 너무 많이 담는 경우에는 이벤트가 비대해지고 결합도가 높아짐
    - 해결 방법 : 후속 서비스를 처리하는 데 필요한 최소 단위 데이터를 VO로 정의하여 처리
      
  - 비동기 이벤트 테스트
    - 발생 문제 : `@Async` + `@TransactionalEventListener` 조합으로 인해 Mockito doThrow나 `@SpyBean`이 예상대로 동작하지 않음
    - 해결 방법 : 리스너 내부 의존성을 mock 처리 → 테스트에서 예외 발생 시나리오 재현 가능
