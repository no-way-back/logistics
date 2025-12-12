package com.nowayback.order.infrastructure.messaging.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductEventConsumer {

    @KafkaListener(topics = "order-stock-decrease-succeeded", groupId = "${spring.kafka.consumer.group-id}")
    public void onDecreaseStockSucceeded(String message, Acknowledgment ack) {
        log.info("Received stock decrease succeeded message='{}'", message);
    }

    @KafkaListener(topics = "order-stock-decrease-failed", groupId = "${spring.kafka.consumer.group-id}")
    public void onDecreaseStockFailed(String message, Acknowledgment ack) {
        log.info("Received stock decrease failed message='{}'", message);
    }
}
