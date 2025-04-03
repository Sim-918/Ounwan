package Test.test_hello.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "PostFile")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id2")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id22")
    private User user;

    private String originFilename;
    private String storedFilename;
    private String filetype;
    private String filesize;

    private LocalDateTime creAt = LocalDateTime.now();

    @Column(length = 30)
    private String creId;

    @Enumerated(EnumType.STRING)
    private User.YesNo delYn = User.YesNo.N;
}