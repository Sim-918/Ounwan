package Test.test_hello.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Comment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id2")
    private Post post;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime creDt = LocalDateTime.now();
    private LocalDateTime updDt;

    @Column(length = 30)
    private String updId;

    @Enumerated(EnumType.STRING)
    private User.YesNo delYn = User.YesNo.N;

    private int repCnt = 0;
}
