package iuh.fit.payment_service.service;

import iuh.fit.cart_service.DTO.CartItemDTO;
import iuh.fit.payment_service.Client.CartClient;
import iuh.fit.payment_service.Client.UserClient;
import iuh.fit.payment_service.dto.PaymentDTO;
import iuh.fit.payment_service.entity.Payment;
import iuh.fit.payment_service.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private CartClient cartClient;

    @Autowired
    private UserClient userClient;

    public PaymentDTO processPayment(String username, String token) {
        // Lấy giỏ hàng theo username (không cần userId)
        List<CartItemDTO> cartItems = cartClient.getCartByUsername(username, token).block();
        if (cartItems == null || cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        double totalAmount = cartItems.stream()
                .mapToDouble(item -> item.getProductPrice() * item.getQuantity())
                .sum();

        // Nếu cần userId để lưu vào bảng payment, lấy userId từ user_service
        Long userId = userClient.getUserIdByUsername(username, token).block();
        if (userId == null) {
            throw new RuntimeException("Không tìm thấy userId cho username: " + username);
        }

        String transactionId = "TXN_" + UUID.randomUUID().toString().substring(0, 8);
        Payment payment = new Payment();
        payment.setUserId(userId);
        payment.setTotalAmount(totalAmount);
        payment.setStatus(Payment.Status.SUCCESS);
        payment.setTransactionId(transactionId);

        payment = paymentRepository.save(payment);

        cartClient.checkoutByUsername(username, token).block();

        return toDTO(payment);
    }

    public List<PaymentDTO> getPaymentHistory(String username) {
        Long userId = userClient.getUserIdByUsername(username, null).block();
        if (userId == null) {
            throw new RuntimeException("Không tìm thấy userId cho username: " + username);
        }
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