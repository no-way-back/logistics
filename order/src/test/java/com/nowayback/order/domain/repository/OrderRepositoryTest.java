package com.nowayback.order.domain.repository;

import static com.nowayback.order.fixture.OrderFixture.createOrder;
import static org.assertj.core.api.Assertions.assertThat;

import com.nowayback.order.domain.entity.Order;
import com.nowayback.order.presentation.persistence.OrderRepositoryImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@Import(OrderRepositoryImpl.class)
class OrderRepositoryTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
        .withDatabaseName("order-test")
        .withUsername("test")
        .withPassword("test")
        .withInitScript("sql/init-order-schema.sql");

    @DynamicPropertySource
    static void overrideDataSource(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("주문을 저장하면 ID가 발급되고 저장된다.")
    void save_ShouldPersistOrder() {
        // given
        Order order = createOrder();

        // when
        Order savedOrder = orderRepository.save(order);
        entityManager.flush();
        entityManager.clear();

        // then
        Order findOrder = entityManager.find(Order.class, savedOrder.getId());
        assertThat(savedOrder.getId()).isNotNull();
        assertThat(findOrder).isNotNull();
        assertThat(findOrder.getId()).isEqualTo(savedOrder.getId());
        assertThat(findOrder.getName()).isEqualTo(order.getName());
        assertThat(findOrder.getStatus()).isEqualTo(order.getStatus());
    }

}