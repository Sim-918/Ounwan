package Test.test_hello.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;


@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Model model, Principal principal) {
        if (principal != null) {
            model.addAttribute("userId", principal.getName()); // 로그인 사용자 ID 전달
        } else {
            model.addAttribute("userId", null); // 비로그인
        }
        return "home";
    }
}
