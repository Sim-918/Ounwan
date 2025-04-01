package Test.test_hello.repository;


import Test.test_hello.domain.PostFile;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PostFileRepository extends JpaRepository<PostFile, Long> {
}