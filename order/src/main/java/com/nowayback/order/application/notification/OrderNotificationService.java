package com.nowayback.order.application.notification;

import com.nowayback.order.application.client.GeminiClient;
import com.nowayback.order.application.client.request.GeminiDeadlinePrompt;
import com.nowayback.order.application.client.response.GeminiDeadlineResponse;
import com.nowayback.order.domain.entity.Order;
import com.nowayback.order.domain.repository.OrderRepository;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderNotificationService {
    private final GeminiClient geminiClient;
    private final OrderRepository orderRepository;

    public void notifyOrderCreated(UUID orderId) {
        Optional<Order> findOrder = orderRepository.findById(orderId);
        if (findOrder.isEmpty()) {
            log.error("주문 없음 orderId={}", orderId);
            return;
        }
        OffsetDateTime deadLine = getDeadLine(findOrder.get());
        // TODO: 알림 발송
    }

    private OffsetDateTime getDeadLine(Order order) {
        GeminiDeadlinePrompt prompt = createPrompt(order);

        GeminiDeadlineResponse deadline = geminiClient.generateDeadline(prompt);

        log.info("Final dispatch deadline for order {}: {}",
            order.getId(), deadline.finalDispatchDeadline());

        return deadline.finalDispatchDeadline();
    }

    private GeminiDeadlinePrompt createPrompt(Order order) {
        String itemsSummary = order.getOrderItems().stream()
            .map(item -> item.getName() + " x" + item.getQuantity())
            .reduce((a, b) -> a + ", " + b)
            .orElse("");

        String toAddress = joinAddress(
            order.getReceiverCompanySnapshot().getAddress(),
            order.getReceiverCompanySnapshot().getDetailAddress()
        );

        GeminiDeadlinePrompt.Location from =
            new GeminiDeadlinePrompt.Location(
                order.getSupplierCompanySnapshot().getName(),
                order.getSupplierCompanySnapshot().getAddress()
            );

        GeminiDeadlinePrompt.Location to =
            new GeminiDeadlinePrompt.Location(
                order.getReceiverCompanySnapshot().getName(),
                toAddress
            );

        GeminiDeadlinePrompt.WorkingHours workingHours =
            new GeminiDeadlinePrompt.WorkingHours(9, 18);

        return new GeminiDeadlinePrompt(
            order.getId(),
            itemsSummary,
            order.getRequest(),
            from,
            to,
            workingHours,
            order.getCreatedAt(),
            "ko-KR"
        );
    }

    private static String joinAddress(String address, String detailAddress) {
        if (address == null) {
            return detailAddress == null ? "" : detailAddress;
        }
        if (detailAddress == null || detailAddress.isBlank()) {
            return address;
        }
        return address + " " + detailAddress;
    }
}
