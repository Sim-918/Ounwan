package Test.test_hello;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Set;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        todo security공부->깃허브 readme 메모
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/","/register", "/login", "/css/**", "/js/**").permitAll()
                        .requestMatchers("/admin/**").hasRole("A")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")                      // 로그인 폼 경로
                        .loginProcessingUrl("/login")             // POST 로그인 처리 URL
                        .usernameParameter("userId")
                        .successHandler(customLoginSuccessHandler()) // ✅ 역할에 따라 분기
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout") // 로그아웃 URL
                        .logoutSuccessUrl("/") // 성공 후 이동
                        .invalidateHttpSession(true) // 세션 무효화
                        .deleteCookies("JSESSIONID") // 쿠키 제거
                        .clearAuthentication(true) // 인증정보 제거
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // ✅ 로그인 성공 후 역할(Role)에 따라 분기
    @Bean
    public AuthenticationSuccessHandler customLoginSuccessHandler() {
        return (HttpServletRequest request, HttpServletResponse response, Authentication authentication) -> {
            Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());

            if (roles.contains("ROLE_A")) {
                response.sendRedirect("/admin");
            } else {
                response.sendRedirect("/");
            }
        };
    }
}