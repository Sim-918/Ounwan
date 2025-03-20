package Test.test_hello;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/register", "/login", "/css/**", "/js/**", "/posts/**", "/images/**").permitAll() // ✅ "/" 비회원도 접근 가능하게 변경
                        .requestMatchers("/posts/create", "/posts/edit/**", "/posts/delete/**").authenticated() // ✅ 비회원 차단
                        .anyRequest().authenticated() // 나머지는 로그인 필요
                )
                .formLogin(form -> form
                        .loginPage("/login") // ✅ 커스텀 로그인 페이지
                        .defaultSuccessUrl("/", true) // ✅ 로그인 성공 시 "/"로 이동
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/") // ✅ 로그아웃 후 "/"로 이동
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .exceptionHandling(ex -> ex.accessDeniedPage("/access-denied")); // ✅ 접근 권한 문제 시 리디렉션

        return http.build();
    }


    // ✅ 비밀번호 암호화
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}