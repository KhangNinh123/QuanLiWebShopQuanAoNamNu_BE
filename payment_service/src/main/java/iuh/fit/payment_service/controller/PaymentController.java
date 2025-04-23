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
    public PaymentDTO processPayment(@RequestHeader("Authorization") String authorizationHeader) {
        Long userId = getCurrentUserId();
        String token = authorizationHeader.substring(7);
        return paymentService.processPayment(userId, token);
    }

    @GetMapping("/history")
    public List<PaymentDTO> getPaymentHistory() {
        Long userId = getCurrentUserId();
        return paymentService.getPaymentHistory(userId);
    }

    private Long getCurrentUserId() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return username.equals("john_doe") ? 1L : 2L;
    }
}