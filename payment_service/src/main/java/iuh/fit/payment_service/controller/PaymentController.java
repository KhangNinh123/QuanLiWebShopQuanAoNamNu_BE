package iuh.fit.payment_service.controller;

import iuh.fit.payment_service.dto.PaymentDTO;
import iuh.fit.payment_service.service.PaymentService;
import iuh.fit.payment_service.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payment")
@CrossOrigin(origins = "http://localhost:5173")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/process")
    public PaymentDTO processPayment(@RequestHeader("Authorization") String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        String username = jwtUtil.extractUsername(token);
        return paymentService.processPayment(username, token);
    }

    @GetMapping("/history")
    public List<PaymentDTO> getPaymentHistory(@RequestHeader("Authorization") String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        String username = jwtUtil.extractUsername(token);
        return paymentService.getPaymentHistory(username);
    }
}
