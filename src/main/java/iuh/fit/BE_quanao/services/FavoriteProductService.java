package iuh.fit.BE_quanao.services;

import iuh.fit.BE_quanao.entities.FavoriteProduct;

import java.util.List;

public interface FavoriteProductService {
    FavoriteProduct addToFavorites(Long userId, Long productId);
    void removeFromFavorites(Long userId, Long productId);
    List<FavoriteProduct> getUserFavorites(Long userId);
    boolean isProductFavorited(Long userId, Long productId);
} 