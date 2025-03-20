package Test.test_hello.repository;

import Test.test_hello.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PostRepository extends JpaRepository<Post,Long>{
    List<Post> findAllByOrderByIdDesc();

}
