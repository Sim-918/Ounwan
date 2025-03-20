package Test.test_hello.controller;

import Test.test_hello.domain.Post;
import Test.test_hello.service.PostService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;

import java.security.Principal;

//@Controller
//public class HomeController {
//
//    @GetMapping("/")
//    public String home(Model model, Principal principal) {
//        if (principal != null) {
//            model.addAttribute("username", principal.getName());
//        }
//        return "home";  // ✅ home.html 렌더링
//    }
//}
@Controller
public class HomeController {

    private final PostService postService; // ✅ 게시글 조회를 위해 PostService 주입

    public HomeController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/")
    public String home(Model model, Principal principal) {
        if (principal != null) {
            model.addAttribute("username", principal.getName());
            model.addAttribute("loggedInUser", principal.getName()); // ✅ 추가
        } else {
            model.addAttribute("loggedInUser", null);
        }

        List<Post> posts = postService.getAllPosts();
        model.addAttribute("posts", posts);

        return "home";
    }
}
