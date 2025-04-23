package iuh.fit.cart_service.Service;

import iuh.fit.cart_service.DTO.CartItemDTO;
import iuh.fit.cart_service.DTO.CartRequest;
import iuh.fit.cart_service.DTO.ProductDTO;
import iuh.fit.cart_service.Entity.Cart;
import iuh.fit.cart_service.Repository.CartRepository;
import iuh.fit.cart_service.client.ProductClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductClient productClient;

    public void addToCart(CartRequest request, Long userId) {
        Cart cart = new Cart();
        cart.setUserId(userId);
        cart.setProductId(request.getProductId());
        cart.setQuantity(request.getQuantity());
        cartRepository.save(cart);
    }

    public List<CartItemDTO> getCart(Long userId) {
        List<Cart> cartItems = cartRepository.findByUserId(userId);
        return cartItems.stream().map(cart -> {
            // Gọi Product Service để lấy thông tin sản phẩm
            ProductDTO product = productClient.getProductById(cart.getProductId())
                    .block(); // Sử dụng block() để chờ kết quả đồng bộ
            CartItemDTO cartItemDTO = new CartItemDTO();
            cartItemDTO.setId(cart.getId());
            cartItemDTO.setProductId(cart.getProductId());
            cartItemDTO.setProductName(product.getName());
            cartItemDTO.setProductPrice(product.getPrice());
            cartItemDTO.setProductImage(product.getImage());
            cartItemDTO.setQuantity(cart.getQuantity());
            return cartItemDTO;
        }).collect(Collectors.toList());
    }

    public void checkout(Long userId) {
        cartRepository.deleteByUserId(userId);
    }
}