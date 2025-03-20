package Test.test_hello.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        // 현재 세션 무효화
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        // 쿠키 삭제
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                cookie.setMaxAge(0);
                cookie.setPath("/");
                response.addCookie(cookie);
            }
        }

        return "redirect:/";
    }
}
