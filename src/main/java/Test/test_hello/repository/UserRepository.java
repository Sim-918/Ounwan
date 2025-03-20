package Test.test_hello.repository;

import Test.test_hello.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username); // ✅ 사용자 이름으로 찾는 메서드
}