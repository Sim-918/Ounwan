package Test.test_hello.controller;


import Test.test_hello.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
@RequestMapping("/register")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String showRegisterPage(@RequestParam(value = "error", required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("errorMessage", error);
        }
        return "register"; // 🚀 `register.html` 반환
    }

    @PostMapping
    public String registerUser(@RequestParam("username") String username,
                               @RequestParam("email") String email,
                               @RequestParam("password") String password,
                               @RequestParam("confirmPassword") String confirmPassword,
                               Model model) {
        try {
            userService.registerUser(username, email, password, confirmPassword);
            return "redirect:/login"; // 🚀 회원가입 성공 후 로그인 페이지로 이동
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "register"; // 🚨 오류 발생 시 다시 `register.html` 로 이동
        }
    }
}