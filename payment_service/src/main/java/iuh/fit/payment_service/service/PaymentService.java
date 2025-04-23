package iuh.fit.payment_service.service;


import iuh.fit.payment_service.Client.CartClient;
import iuh.fit.payment_service.dto.PaymentDTO;
import iuh.fit.payment_service.entity.Payment;
import iuh.fit.payment_service.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private CartClient cartClient;

    public PaymentDTO processPayment(Long userId, String token) {
        List<CartItemDTO> cartItems = cartClient.getCart(userId, token).block();
        if (cartItems == null || cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        double totalAmount = cartItems.stream()
                .mapToDouble(item -> item.getProductPrice() * item.getQuantity())
                .sum();

        String transactionId = "TXN_" + UUID.randomUUID().toString().substring(0, 8);
        Payment payment = new Payment();
        payment.setUserId(userId);
        payment.setTotalAmount(totalAmount);
        payment.setStatus(Payment.Status.SUCCESS);
        payment.setTransactionId(transactionId);

        payment = paymentRepository.save(payment);

        cartClient.checkout(userId, token).block();

        return toDTO(payment);
    }

    public List<PaymentDTO> getPaymentHistory(Long userId) {
        return paymentRepository.findByUserId(userId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private PaymentDTO toDTO(Payment payment) {
        PaymentDTO dto = new PaymentDTO();
        dto.setId(payment.getId());
        dto.setUserId(payment.getUserId());
        dto.setTotalAmount(payment.getTotalAmount());
        dto.setStatus(payment.getStatus().name());
        dto.setTransactionId(payment.getTransactionId());
        dto.setCreatedAt(payment.getCreatedAt());
        return dto;
    }
}