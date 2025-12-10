package com.nowayback.payment.application;

import com.nowayback.payment.application.dto.command.CancelPaymentCommand;
import com.nowayback.payment.application.dto.command.ConfirmPaymentCommand;
import com.nowayback.payment.application.dto.command.CreatePaymentCommand;
import com.nowayback.payment.application.dto.result.PaymentResult;
import com.nowayback.payment.domain.PaymentRepository;
import com.nowayback.payment.domain.entity.Payment;
import com.nowayback.payment.domain.vo.PaymentStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    @Transactional
    public PaymentResult createPayment(CreatePaymentCommand command) {
        validateExistingPendingPayment(command.orderId());

        Payment payment = Payment.create(
                command.orderId(),
                command.userId(),
                command.amount()
        );

        Payment savedPayment = paymentRepository.save(payment);

        return PaymentResult.from(savedPayment);
    }

    @Transactional
    public PaymentResult confirmPayment(ConfirmPaymentCommand command) {
        Payment payment = getPendingPaymentByOrderId(command.orderId());

        PaymentGatewayResult result = confirmPaymentWithPG(
                command.pgPaymentKey(),
                command.pgOrderId(),
                command.pgMethod(),
                payment.getAmount()
        );

        if (result.status() == PaymentGatewayStatus.FAILED) {
            payment.fail();

            // 실패 이벤트 발행
        } else {
            LocalDateTime approvedAt = result.approvedAt();
            payment.confirm(command.pgPaymentKey(), command.pgOrderId(), command.pgMethod(), approvedAt);

            // 성공 이벤트 발행
        }

        LocalDateTime approvedAt = LocalDateTime.now();
        payment.confirm(command.pgPaymentKey(), command.pgOrderId(), command.pgMethod(), approvedAt);

        return PaymentResult.from(payment);
    }

    @Transactional
    public PaymentResult failPayment(UUID paymentId) {
        Payment payment = getPaymentById(paymentId);

        payment.fail();

        return PaymentResult.from(payment);
    }

    @Transactional
    public PaymentResult cancelPayment(CancelPaymentCommand command) {
        Payment payment = getPaymentById(command.paymentId());

        LocalDateTime canceledAt = LocalDateTime.now();
        payment.cancel(command.cancelReason(), canceledAt);

        return PaymentResult.from(payment);
    }

    private PaymentGatewayResult confirmPaymentWithPG(String pgPaymentKey, String pgOrderId, String pgMethod, BigDecimal amount) {
        // 실제 PG사 연동 로직 구현
        return new PaymentGatewayResult(PaymentGatewayStatus.DONE, LocalDateTime.now());
    }

    private void validateExistingPendingPayment(UUID uuid) {
        boolean exists = paymentRepository.existsByOrderIdAndStatus(uuid, PaymentStatus.PENDING);
        if (exists) {
            throw new IllegalStateException("이미 존재하는 대기 중인 결제가 있습니다.");
        }
    }

    private Payment getPaymentById(UUID paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> new IllegalStateException("결제를 찾을 수 없습니다."));
    }

    private Payment getPendingPaymentByOrderId(UUID uuid) {
        return paymentRepository.findByOrderIdAndStatus(uuid, PaymentStatus.PENDING)
                .orElseThrow(() -> new IllegalStateException("대기 중인 결제를 찾을 수 없습니다."));
    }

    public record PaymentGatewayResult(
            PaymentGatewayStatus status,
            LocalDateTime approvedAt
    ) {}

    public enum PaymentGatewayStatus {
        DONE,
        FAILED
    }
}
