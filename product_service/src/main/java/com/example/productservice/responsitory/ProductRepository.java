package com.example.productservice.responsitory;

import com.example.productservice.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategoryAndType(Product.Category category, Product.Type type);
    List<Product> findByNameContainingIgnoreCase(String name);
    List<Product> findByPriceBetweenAndCategory(Double minPrice, Double maxPrice, Product.Category category);
}
