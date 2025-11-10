## 🔬 테스트 가이드
[테스트 가이드](https://github.com/no-way-back/logistics/blob/feat/tdd-practice/TEST_GUIDE.md)

## 📑 각 Step별 학습 내용
### 📎 Step 1: TDD 기초와 도메인 모델 테스트
- 학습 내용
    - Given-When-Then 구조의 적용
        - Red: 실패하는 명세 작성 시, 기능의 기대 동작 방식에 대한 명확한 시나리오 정의 가능
        - Green: 테스트를 통과시키는 최소한의 코드만 작성하여 불필요한 과잉 설계를 피할 수 있음
        - Refactor: 테스트 코드를 통한 검증을 수행하여 버그 걱정 없이 코드를 개선하고 품질을 높일 수 있음

- 느낀점
  
  Given-When-Then 구조를 적용함으로써 구현 전에 무엇을 만들지 정확히 이해하고, 설계에 집중할 수 있었습니다.
  
  테스트하기 쉬운 구조와 명확한 관심사 분리 원칙을 자연스럽게 적용하여 좋은 설계 방식을 익힐 수 있었습니다.

- 관련 코드
  - [OrderTest](https://github.com/no-way-back/logistics/blob/feat/tdd-practice/order/src/test/java/com/nowayback/order/domain/entity/OrderTest.java)
  - [DeliveryTest](https://github.com/no-way-back/logistics/blob/feat/tdd-practice/delivery/src/test/java/com/nowayback/delivery/domain/delivery/entity/DeliveryTest.java)
  - [HubTest](https://github.com/no-way-back/logistics/blob/feat/tdd-practice/hub/src/test/java/com/nowayback/hub/domain/hub/HubEntityTest.java)

### 📎 Step 2: 서비스 레이어 테스트와 Mock 활용
- 학습 내용
    - 서비스 레이어의 역할과 TDD 적용
        TDD 사이클을 적용해 도메인 로직을 실제 비즈니스 흐름으로 조합하고 외부 시스템과의 연동을 제어하는 계층으로 구현
        Given-When-Then 구조를 기반으로 테스트를 먼저 작성하여 서비스의 동작을 명확히 정의하고 유스케이스 단위로 설계
    - Mockito를 활용한 의존성 격리
        OrderRepository, ProductClient, DeliveryClient 등 외부 의존성을 @Mock으로 대체하여 서비스 로직을 독립적으로 검증
    - Mock의 활용과 인식 변화
        성공/실패 시나리오를 각각 설정하여 예외 처리와 롤백 동작을 검증
        도메인과 서비스의 경계를 명확히 구분할 수 있음

- 느낀점

  서비스 레이어에 TDD를 적용함으로써, 구현 전 기능의 의도를 구체적으로 이해할 수 있었으며 테스트가 서비스 설계 방향을 자연스럽게 이끌어 개발 표율이 향상될 수 있었습니다.
  
  또한 Mockito를 활용하여 실제 API나 DB 없이 테스트가 가능해져 로직 흐름에 집중할 수 있게 되어, 테스트 안정성과 실행 속도를 향상할 수 있었습니다.
  
  Mock의 활용을 통해 테스트를 단순화한다는 의미를 체감할 수 있었으며, 특정 로직이 도메인의 책임인지, 서비스의 책임인지를 구분할 수 있게 되어 코드 구조가 명확히 할 수 있었습니다. 이를 통해 유지보수성을 향상시킬 수 있었습니다.
    
- 관련 코드
  - [OrderServiceTest](https://github.com/no-way-back/logistics/blob/feat/tdd-practice/order/src/test/java/com/nowayback/order/application/OrderServiceTest.java)
  - [DeliveryServiceTest](https://github.com/no-way-back/logistics/blob/feat/tdd-practice/delivery/src/test/java/com/nowayback/delivery/application/DeliveryServiceTest.java)
  - [HubServiceTest](https://github.com/no-way-back/logistics/blob/feat/tdd-practice/hub/src/test/java/com/nowayback/hub/application/hub/HubServiceTest.java)

### 📎 Step 3: Repository 테스트
- 학습 내용

    `Testcontainers` 적용을 통한 실제 환경과 유사한 테스트 구축이 가능
- 느낀점

    `Testcontainers`를 적용함으로써 테스트 데이터를 격리하고, Repository 테스트를 통해 실제 DB에서 의도대로 동작하는지 확인할 수 있었습니다.
- 관련 코드
  - [OrderRepositoryTest](https://github.com/no-way-back/logistics/blob/feat/tdd-practice/order/src/test/java/com/nowayback/order/domain/repository/OrderRepositoryTest.java)
  - [DeliveryRepositoryTest](https://github.com/no-way-back/logistics/blob/feat/tdd-practice/delivery/src/test/java/com/nowayback/delivery/infrastructure/repository/DeliveryRepositoryTest.java)

### 📎 Step 4: API 테스트와 통합 테스트
- 학습 내용

    API 테스트를 통해 HTTP 요청/응답 상태와 JSON 응답을 검증
- 느낀점

    API 테스트를 작성함으로써, API 명세서에 작성된 내용을 명확히 보장할 수 있었습니다. 이를 통해 클라이언트 요청에 대한 오류를 사전에 방지할 수 있었습니다.
- 관련 코드
  - [DeliveryControllerTest](https://github.com/no-way-back/logistics/blob/feat/tdd-practice/delivery/src/test/java/com/nowayback/delivery/presentation/DeliveryControllerTest.java)
  - [HubControllerTest](https://github.com/no-way-back/logistics/blob/feat/tdd-practice/hub/src/test/java/com/nowayback/hub/presentation/hub/HubControllerTest.java)

### 📎 Step 5: 복잡한 비즈니스 로직과 리팩토링
- 학습 내용
    - 파라미터화된 테스트 (`@ParameterizedTest`)
        `@ValueSource`, `@CsvSource`, `@EnumSource` 등을 활용하여 다양한 형태의 테스트 데이터를 주입하여 테스트를 수행

          @ParameterizedTest
          @CsvSource({
                "WAITING_AT_HUB,        TRANSIT_BETWEEN_HUBS",
                "TRANSIT_BETWEEN_HUBS,  AT_DESTINATION_HUB",
                "AT_DESTINATION_HUB,    OUT_FOR_DELIVERY",
                "OUT_FOR_DELIVERY,      DELIVERED"
          })
          @DisplayName("정의된 다음 상태로만 전이할 수 있다.")
          void updateDeliveryStatus_ShouldAllowValidTransitions(DeliveryStatus initialStatus, DeliveryStatus newStatus) throws Exception {
            /* given */
            Delivery delivery = createDeliveryWithStatus(initialStatus);

            /* when */
            delivery.updateStatus(newStatus);

            /* then */
            assertThat(delivery.getStatus()).isEqualTo(newStatus);
          }
      
    - 테스트 Fixture 공통화
        여러 테스트에서 반복적으로 사용되는 테스트 데이터를 재사용하도록 Fixture 패턴 적용

- 느낀점
  
  `@ParameterizedTest`를 활용함으로써 반복적인 테스트 코드를 줄여 깔끔한 구조를 유지할 수 있었으며, 여러 케이스에 대한 테스트를 수행하여 테스트 커버리지를 향상시킬 수 있었습니다.
  
  Fixture 패턴을 적용하게 되어, 테스트 데이터 변경 시 Fixture 내의 값만을 변경하여 유지보수가 편리했습니다. 또한 공통적인 데이터 선언이 감소해 테스트 할 기능 자체에 집중할 수 있었습니다.

## 📑 회고
각 레이어에 대한 모든 테스트를 작성하는 데 있어서, 많은 테스트 코드를 작성해야 했기에 많은 시간이 드는 어려움이 있었습니다.
테스트를 작성함으로써 리팩토링에 대한 자신감을 얻을 수 있었고, 유효성 검사를 꼼꼼하게 수행하는 장점이 있었습니다.
TDD 실습을 통해, 도메인 로직은 테스트 가능한 형태로 분리해야한다는 점과, Mock을 활용한 서비스 테스트는 팀 협업 효율을 높인다는 인사이트를 얻을 수 있었습니다.
