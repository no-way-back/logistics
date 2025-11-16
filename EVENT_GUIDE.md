### 1. 이벤트 네이밍 규칙
- 이벤트명 : `[도메인명][동작]Event.java`
- 과거형 사용 : `OrderCreatedEvent`, `PaymentCompletedEvent`

### 2. 리스너 작성 가이드
- `@Async` 사용 시점
  
  이메일 발송, 로그 저장과 같은 사용자가 기다릴 필요 없는 작업을 비동기로 처리합니다.
  
  사용자에게 바로 결과를 반환해야 하거나, 하나의 트랜잭션으로 묶여야하는 경우 동기로 처리합니다.
  
  (비동기 이벤트 리스너 학습을 위해 프로젝트 내 `PaymentEventListener`, `StockEventListener`는 비동기로 작성되었습니다.)
- 예외 처리 방법
  
  이벤트 처리 중 예외 발생 시 로그로 실패를 기록합니다.

### 3. 테스트 작성 가이드
- 이벤트 발행 테스트
  ```java
  @Test
  @DisplayName("주문 생성 시 OrderCreatedEvent가 발행된다")
  void createOrder_ShouldPublishOrderCreatedEvent() {
      // given
      Product product = Product.builder()
          .id(UUID.randomUUID())
          .name("테스트 상품")
          .stock(100)
          .build();
      productRepository.save(product);
  
      CreateOrderCommand orderCommand = createOrderCommandWithOrderItems(List.of(
          CreateOrderItem.of(
              product.getId(),
              product.getName(),
              BigDecimal.valueOf(1000),
              10
          )
      ));
  
      // when
      orderService.createOrder(orderCommand);
  
      // then
      // 이벤트 리스너가 호출되었는지 검증
      await()
          .atMost(Duration.ofSeconds(5))
          .untilAsserted(() -> {
              verify(orderEventListener, times(1))
                  .processEvent(any(OrderCreatedEvent.class));
          });
      }
  }
  ```
- 비동기 처리 검증
  ```java
  @Test
  @DisplayName("이벤트가 비동기로 처리된다")
  void events_ShouldBeProcessedAsynchronously() throws InterruptedException {
      // given
      CreateOrderCommand orderCommand = createOrderCommand();
      String mainThread = Thread.currentThread().getName();
      log.info("메인 스레드: {}", mainThread);

      // when
      orderService.createOrder(orderCommand);

      // then
      await()
          .atMost(2, TimeUnit.SECONDS)
          .pollInterval(100, TimeUnit.MILLISECONDS)
          .untilAsserted(() -> {
              verify(paymentEventListener, times(1))
                  .processEvent(any(OrderCreatedEvent.class));
          });
  }
  ```
  <img width="1661" height="250" alt="STEP3_events_ShouldBeProcessedAsynchronously" src="https://github.com/user-attachments/assets/8069415c-9b55-43ca-bde6-9ffe40d9673e" />

  
- 전체 플로우 통합 테스트
  ```java
  @Test
  @DisplayName("주문 생성부터 재고 차감까지 전체 플로우가 정상 동작한다")
  void fullOrderFlow_ShouldWorkCorrectly() throws InterruptedException {
  		// given
        Product product = Product.builder()
            .id(UUID.randomUUID())
            .name("테스트 상품")
            .stock(100)
            .build();
        productRepository.save(product);

        CreateOrderCommand orderCommand = createOrderCommandWithOrderItems(List.of(
            CreateOrderItem.of(
                product.getId(),
                product.getName(),
                BigDecimal.valueOf(1000),
                10
            )
        ));

        // when
        OrderCreateResult response = orderService.createOrder(orderCommand);

        // then
        // 1. 주문 생성 확인
        Order order = orderRepository.findById(response.orderId()).orElseThrow();
        assertThat(order.getStatus()).isEqualTo(OrderStatus.CREATED);

        await()
            .atMost(Duration.ofSeconds(5))
            .untilAsserted(() -> {
                List<Payment> payments = paymentRepository.findByOrderId(order.getId());
                assertThat(payments).hasSize(1);
                assertThat(payments.get(0).getStatus()).isEqualTo(PaymentStatus.COMPLETED);

                Product updatedProduct = productRepository.findById(product.getId()).orElseThrow();
                assertThat(updatedProduct.getStock()).isEqualTo(90);
            });
  }
  ```
- 🔗 관련 코드
  - [`OrderServiceEventTest.java`](https://github.com/no-way-back/logistics/blob/feat/event-listener/order/src/test/java/com/nowayback/order/application/OrderServiceEventTest.java)
  - [`PaymentEventListenerTest.java`](https://github.com/no-way-back/logistics/blob/feat/event-listener/order/src/test/java/com/nowayback/order/payment/application/eventlistener/PaymentEventListenerTest.java)
  - [`OrderFlowIntegrationTest.java`](https://github.com/no-way-back/logistics/blob/feat/event-listener/order/src/test/java/com/nowayback/order/OrderFlowIntegrationTest.java)

### 4. EventStore 구현
발행된 모든 이벤트를 데이터베이스에 저장하고, 이를 조회할 수 있는 API를 제공

- 🔗 관련 코드
  - [`StoredEvent`](https://github.com/no-way-back/logistics/blob/feat/event-listener/order/src/main/java/com/nowayback/order/common/eventstore/StoredEvent.java)
  - [`EventStoreListener`](https://github.com/no-way-back/logistics/blob/feat/event-listener/order/src/main/java/com/nowayback/order/common/eventstore/EventStoreListener.java)
  - [`StoredEventController`](https://github.com/no-way-back/logistics/blob/feat/event-listener/order/src/main/java/com/nowayback/order/common/eventstore/StoredEventController.java)
