package Test.test_hello.controller;

import Test.test_hello.domain.Post;
import Test.test_hello.repository.PostRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;


@Controller
public class HomeController {

    private final PostRepository postRepository;

    public HomeController(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @GetMapping("/")
    public String home(Model model, Principal principal) {
        if (principal != null) {
            model.addAttribute("userId", principal.getName()); // 로그인 사용자 ID 전달
        } else {
            model.addAttribute("userId", null); // 비로그인
        }

        List<Post> posts= postRepository.findAllByOrderByCreDtDesc();
        model.addAttribute("posts",posts);
        return "home";
    }
}
