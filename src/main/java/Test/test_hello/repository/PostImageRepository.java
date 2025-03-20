package Test.test_hello.repository;


import Test.test_hello.domain.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PostImageRepository extends JpaRepository<PostImage, Long> {
}