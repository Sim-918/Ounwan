package Test.test_hello.service.admin;


import Test.test_hello.domain.Comment;
import Test.test_hello.domain.Post;
import Test.test_hello.domain.PostFile;
import Test.test_hello.domain.User;
import Test.test_hello.repository.CommentRepository;
import Test.test_hello.repository.PostFileRepository;
import Test.test_hello.repository.PostRepository;
import Test.test_hello.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private PasswordEncoder passwordEncoder;
    private final CommentRepository commentRepository;
    private final PostFileRepository postFileRepository;

    // --- USER ---

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUser(Long id) {
        return userRepository.findById(id);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    // --- POST ---
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Optional<Post> getPost(Long id) {
        return postRepository.findById(id);
    }

    public Post savePost(Post post) {
        return postRepository.save(post);
    }

    public long getTodayPostCount() {
        return postRepository.countPostsToday();
    }

    public long getTodayHeavyReportedCount() {
        return postRepository.countTodayHeavyReportedPosts();
    }

    @Transactional
    public void deletePost(Long id) {
        Post post = postRepository.findById(id).orElseThrow();
        post.setDelYn(User.YesNo.Y);
    }

    // --- COMMENT ---
    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }

    public Optional<Comment> getComment(Long id) {
        return commentRepository.findById(id);
    }

    public Comment saveComment(Comment comment) {
        return commentRepository.save(comment);
    }

    @Transactional
    public void deleteComment(Long id) {
        Comment comment = commentRepository.findById(id).orElseThrow();
        comment.setDelYn(User.YesNo.Y);
    }

    // --- POSTFILE ---
    public List<PostFile> getAllFiles() {
        return postFileRepository.findAll();
    }

    public Optional<PostFile> getFile(Long id) {
        return postFileRepository.findById(id);
    }

    public PostFile saveFile(PostFile file) {
        return postFileRepository.save(file);
    }

    @Transactional
    public void deleteFile(Long id) {
        PostFile file = postFileRepository.findById(id).orElseThrow();
        file.setDelYn(User.YesNo.Y);
    }
}