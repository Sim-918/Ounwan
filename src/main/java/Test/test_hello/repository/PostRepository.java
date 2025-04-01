package Test.test_hello.repository;

import Test.test_hello.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface PostRepository extends JpaRepository<Post,Long>{
    @Query("SELECT COUNT(p) FROM Post p WHERE DATE(p.creDt) = CURRENT_DATE")
    long countPostsToday();
    @Query("SELECT COUNT(p) FROM Post p WHERE DATE(p.creDt) = CURRENT_DATE AND p.repCnt >= 10")
    long countTodayHeavyReportedPosts();
    List<Post> findAllByOrderByCreDtDesc();
}
