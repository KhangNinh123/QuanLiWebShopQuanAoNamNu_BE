package iuh.fit.spring.user_service.Repository;


import iuh.fit.spring.user_service.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    User findByEmail(String email);
}