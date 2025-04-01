package Test.test_hello.service.post;

import Test.test_hello.domain.Post;
import Test.test_hello.domain.User;
import Test.test_hello.repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;


@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    public Post createPost(Post post) {
        return postRepository.save(post);
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Post getPostById(Long id) {
        return postRepository.findById(id).orElse(null);
    }

    @Transactional
    public void deletePostById(Long id) {
        Post post = postRepository.findById(id).orElseThrow();
        post.setDelYn(User.YesNo.Y);
    }
}
