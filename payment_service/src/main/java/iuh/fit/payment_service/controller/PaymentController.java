package iuh.fit.payment_service.controller;

import iuh.fit.payment_service.dto.PaymentDTO;
import iuh.fit.payment_service.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payment")
@CrossOrigin(origins = "http://localhost:5173")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/process")
    public PaymentDTO processPayment() {
        Long userId = getCurrentUserId();
        return paymentService.processPayment(userId);
    }

    @GetMapping("/history")
    public List<PaymentDTO> getPaymentHistory() {
        Long userId = getCurrentUserId();
        return paymentService.getPaymentHistory(userId);
    }

    // Lấy userId từ SecurityContext (yêu cầu bạn đã cấu hình JWT và set userId vào principal)
    private Long getCurrentUserId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof org.springframework.security.core.userdetails.User user) {
            // Bạn cần cách để ánh xạ username hoặc authorities thành userId nếu không dùng custom token
            return Long.parseLong(user.getUsername()); // giả sử username chính là userId
        }
        throw new IllegalStateException("Không thể xác định người dùng hiện tại");
    }
}
