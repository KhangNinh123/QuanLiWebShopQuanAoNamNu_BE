package iuh.fit.spring.product_service.Repository;

import iuh.fit.spring.product_service.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Long> {
}
