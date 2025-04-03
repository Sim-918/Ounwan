package Test.test_hello.controller;

import Test.test_hello.domain.Comment;
import Test.test_hello.domain.Post;
import Test.test_hello.domain.PostFile;
import Test.test_hello.domain.User;
import Test.test_hello.repository.PostRepository;
import Test.test_hello.repository.UserRepository;
import Test.test_hello.service.admin.AdminService;
import Test.test_hello.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;


@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final UserRepository userRepository;
    private final UserService userService;
    private final PostRepository postRepository;

    // --- dashboard ---
    @GetMapping
    public String adminPage(Model model) {
        model.addAttribute("todayHeavyReportedCount", adminService.getTodayHeavyReportedCount());
        model.addAttribute("todayPostCount", adminService.getTodayPostCount());
        model.addAttribute("userCount", userRepository.count());
        model.addAttribute("postCount", adminService.getAllPosts().size());
        model.addAttribute("users", adminService.getAllUsers());
        model.addAttribute("posts", adminService.getAllPosts());
        model.addAttribute("comments", adminService.getAllComments());
        model.addAttribute("files", adminService.getAllFiles());
        return "admin/dashborad";
    }

    // --- USER ---
    @GetMapping("/account")
    public String adminAccountPage(@RequestParam(name = "deleted", required = false, defaultValue = "false") boolean deleted,
                                   Model model) {
        List<User> users = deleted
                ? userService.getUsersByDelYn(User.YesNo.Y)
                : userService.getUsersByDelYn(User.YesNo.N);

        model.addAttribute("users", users);
        model.addAttribute("showDeleted", deleted);
        return "admin/account";
    }

    @PostMapping("/account")
    public String createAccount(
            @RequestParam("userId") String userId,
            @RequestParam("userPassword") String userPassword,
            @RequestParam("userPhone") String userPhone,
            @RequestParam("userEmail") String userEmail,
            @RequestParam("role") String role
    ) {
        User user = new User();
        user.setUserId(userId);
        user.setPassword(userPassword); // 암호화는 UserService에서 처리
        user.setEmail(userEmail);
        user.setPhoneNum(userPhone);
        user.setRole("admin".equals(role) ? User.Role.A : User.Role.U);

        userService.registerUser(user);

        return "redirect:/admin/account";
    }

    @PostMapping("/account/update")
    public String updateAccount(
            @RequestParam("id") Long id,
            @RequestParam("userId") String userId,
            @RequestParam("email") String email,
            @RequestParam("phoneNum") String phoneNum,
            @RequestParam("role") String role
    ) {
        User user = userRepository.findById(id).orElseThrow();
        user.setEmail(email);
        user.setPhoneNum(phoneNum);
        user.setRole("admin".equalsIgnoreCase(role) ? User.Role.A : User.Role.U);
        userRepository.save(user);
        return "redirect:/admin/account";
    }

    @PostMapping("/account/delete")
    public String deleteAccount(@RequestParam("userId") String userId) {
        userService.deleteByUserId(userId);
        return "redirect:/admin/account";
    }

    @PostMapping("/account/restore")
    public String restoreAccount(@RequestParam("userId") String userId) {
        userService.restoreByUserId(userId);
        return "redirect:/admin/account?deleted=true";
    }


    // --- POST ---
    @GetMapping("/post")
    public String postList(Model model){
        List<Post> posts= postRepository.findAllByOrderByCreDtDesc();
        model.addAttribute("posts",posts);
        return "admin/post";
    }

    @PostMapping("/post/create")
    public String createPost(@RequestParam("title") String title,
                             @RequestParam("content") String content,
                             Principal principal){

        User admin=userRepository.findByUserId(principal.getName()).orElseThrow();

        Post post=new Post();
        post.setTitle(title);
        post.setContent(content);
        post.setUser(admin);
        post.setCreDt(LocalDateTime.now());
        post.setDelYn(User.YesNo.N);
        post.setRepCnt(0);
        post.setLikeCnt(0);
        post.setUnlikeCnt(0);
        post.setViewCnt(0);

        postRepository.save(post);

        return "redirect:/admin/post";

    }

    // --- COMMENT ---
    @PostMapping("/comment/{id}/delete")
    public String deleteComment(@PathVariable Long id) {
        adminService.deleteComment(id);
        return "redirect:/admin";
    }

    @PostMapping("/comment")
    public String createComment(@ModelAttribute Comment comment) {
        adminService.saveComment(comment);
        return "redirect:/admin";
    }

    // --- FILE ---
    @PostMapping("/file/{id}/delete")
    public String deleteFile(@PathVariable Long id) {
        adminService.deleteFile(id);
        return "redirect:/admin";
    }

    @PostMapping("/file")
    public String createFile(@ModelAttribute PostFile file) {
        adminService.saveFile(file);
        return "redirect:/admin";
    }
}