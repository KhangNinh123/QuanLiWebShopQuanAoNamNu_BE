package com.example.productservice.service;


import com.example.productservice.dto.ProductDTO;
import com.example.productservice.entity.Product;
import com.example.productservice.responsitory.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<ProductDTO> getProductsByCategoryAndType(String category, String type) {
        return productRepository.findByCategoryAndType(
                Product.Category.valueOf(category),
                Product.Type.valueOf(type)
        ).stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<ProductDTO> searchProducts(String name) {
        return productRepository.findByNameContainingIgnoreCase(name)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<ProductDTO> filterProducts(Double minPrice, Double maxPrice, String category) {
        return productRepository.findByPriceBetweenAndCategory(
                minPrice, maxPrice, Product.Category.valueOf(category)
        ).stream().map(this::toDTO).collect(Collectors.toList());
    }

    private ProductDTO toDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setPrice(product.getPrice());
        dto.setImage(product.getImage());
        dto.setCategory(product.getCategory().name());
        dto.setType(product.getType().name());
        return dto;
    }
}