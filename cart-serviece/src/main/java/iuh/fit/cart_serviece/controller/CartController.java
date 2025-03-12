package iuh.fit.cart_serviece.controller;
import iuh.fit.cart_serviece.Entity.Cart;
import iuh.fit.cart_serviece.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carts")
public class CartController {
    @Autowired
    private CartService cartService;

    @GetMapping("/{userId}")
    public List<Cart> getCartByUserId(@PathVariable Long userId) {
        return cartService.getCartByUserId(userId);
    }

    @PostMapping
    public ResponseEntity<?> addToCart(@RequestBody Cart cart) {
        if (cart.getProductId() == null || cart.getUserId() == null || cart.getQuantity() == null) {
            return ResponseEntity.badRequest().body("Product ID, User ID, and Quantity cannot be null");
        }
        return ResponseEntity.ok(cartService.addToCart(cart));
    }


    @DeleteMapping("/{userId}/products/{productId}")
    public void removeFromCart(@PathVariable Long userId, @PathVariable Long productId) {
        cartService.removeFromCart(userId, productId);
    }
}