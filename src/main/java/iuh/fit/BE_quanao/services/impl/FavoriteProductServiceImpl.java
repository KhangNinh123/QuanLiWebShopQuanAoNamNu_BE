package iuh.fit.BE_quanao.services.impl;

import iuh.fit.BE_quanao.entities.FavoriteProduct;
import iuh.fit.BE_quanao.entities.Product;
import iuh.fit.BE_quanao.entities.User;
import iuh.fit.BE_quanao.repositories.FavoriteProductRepository;
import iuh.fit.BE_quanao.repositories.ProductRepository;
import iuh.fit.BE_quanao.repositories.UserRepository;
import iuh.fit.BE_quanao.services.FavoriteProductService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteProductServiceImpl implements FavoriteProductService {

    private final FavoriteProductRepository favoriteProductRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public FavoriteProduct addToFavorites(Long userId, Long productId) {
        // Kiểm tra xem sản phẩm đã được yêu thích chưa
        if (isProductFavorited(userId, productId)) {
            throw new IllegalStateException("Product is already in favorites");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        FavoriteProduct favoriteProduct = new FavoriteProduct();
        favoriteProduct.setUser(user);
        favoriteProduct.setProduct(product);

        return favoriteProductRepository.save(favoriteProduct);
    }

    @Override
    @Transactional
    public void removeFromFavorites(Long userId, Long productId) {
        if (!isProductFavorited(userId, productId)) {
            throw new EntityNotFoundException("Favorite product not found");
        }
        favoriteProductRepository.deleteByUserIdAndProductId(userId, productId);
    }

    @Override
    public List<FavoriteProduct> getUserFavorites(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new EntityNotFoundException("User not found");
        }
        return favoriteProductRepository.findByUserId(userId);
    }

    @Override
    public boolean isProductFavorited(Long userId, Long productId) {
        return favoriteProductRepository.existsByUserIdAndProductId(userId, productId);
    }
} 