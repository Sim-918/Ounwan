package Test.test_hello.controller;


import Test.test_hello.domain.User;
import Test.test_hello.dto.UserRegisterDto;
import Test.test_hello.service.user.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@Controller
public class UserController {
    @Autowired
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("userRegisterDto",new UserRegisterDto());
        return "register";
    }

//  todo 회원가입 실패, 성공 alert
    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute UserRegisterDto userDto, BindingResult result) {
        if (result.hasErrors()) return "register";

        userService.register(userDto);
        return "redirect:/login?success=true";
    }

}