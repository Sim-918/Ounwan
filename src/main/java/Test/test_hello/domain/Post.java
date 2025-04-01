package Test.test_hello.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "post")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id2")
    private User user;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime creDt = LocalDateTime.now();

    private LocalDateTime updDt;

    @Column(length = 30)
    private String updId;

    @Enumerated(EnumType.STRING)
    private User.YesNo delYn = User.YesNo.N;

    private int repCnt = 0;
    private int likeCnt = 0;
    private int unlikeCnt = 0;
    private int viewCnt = 0;
}
