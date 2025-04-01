package Test.test_hello.domain;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    idx 로 수정
    private Long id;
//    userId로 수정
    @Column(unique = true, nullable = false, length = 30)
    private String userId;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(unique = true, nullable = false, length = 100)
    private String email;

    @Column(nullable = false)
    private String profileImageUrl = "/images/default_image.png";

    @Column(length = 20)
    private String phoneNum;

    private LocalDateTime cre_dt = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    private Role role = Role.U;

    private LocalDateTime upd_dt;

    @Column(length = 30)
    private String upd_id;

    @Enumerated(EnumType.STRING)
    private YesNo del_yn = YesNo.N;

    @Enumerated(EnumType.STRING)
    private YesNo rep_yn = YesNo.N;

    private int pw_fail_cnt = 0;

    @Enumerated(EnumType.STRING)
    private YesNo lock_yn = YesNo.N;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts;

    public enum Role { A, U }
    public enum YesNo { Y, N }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(() -> "ROLE_" + this.role.name());  // ROLE_A, ROLE_U
    }

    @Override
    public String getUsername() {
        return this.userId;
    }
    // ✅ 반드시 존재해야 함!
    public void setUsername(String userId) {
        this.userId = userId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }



}