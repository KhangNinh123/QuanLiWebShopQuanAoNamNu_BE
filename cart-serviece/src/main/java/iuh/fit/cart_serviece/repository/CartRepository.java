package iuh.fit.cart_serviece.repository;
import iuh.fit.cart_serviece.Entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findByUserId(Long userId); // Tìm giỏ hàng của người dùng
    void deleteByUserIdAndProductId(Long userId, Long productId); // Xóa sản phẩm khỏi giỏ hàng
}