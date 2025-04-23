package iuh.fit.cart_service.Controller;

import iuh.fit.cart_service.DTO.CartItemDTO;
import iuh.fit.cart_service.DTO.CartRequest;
import iuh.fit.cart_service.Service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "http://localhost:5173")
public class CartController {
    @Autowired
    private CartService cartService;

    @PostMapping("/add")
    public void addToCart(@RequestBody CartRequest request) {
        Long userId = getCurrentUserId(); // Lấy userId từ token
        cartService.addToCart( request, userId);
    }

    @GetMapping
    public List<CartItemDTO> getCart() {
        Long userId = getCurrentUserId();
        return cartService.getCart(userId);
    }

    @PostMapping("/checkout")
    public void checkout() {
        Long userId = getCurrentUserId();
        cartService.checkout(userId);
    }

    private Long getCurrentUserId() {
        // Trong thực tế, bạn có thể gọi API của User Service để lấy userId từ username
        // Ở đây, tôi giả lập userId dựa trên username từ token
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return username.equals("john_doe") ? 1L : 2L; // Giả lập: john_doe -> userId 1, jane_smith -> userId 2
    }
}