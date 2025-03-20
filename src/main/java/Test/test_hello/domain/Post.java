package Test.test_hello.domain;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    private String videoUrl; // ✅ 동영상 URL

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    // ✅ 게시글(N) → 사용자(1) 관계 (ManyToOne)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // ✅ 게시글(1) → 이미지(N) 관계 (OneToMany)
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostImage> images;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // ✅ 작성자 이름 반환
    public String getAuthorName() {
        return user != null ? user.getUsername() : "Unknown";
    }
}