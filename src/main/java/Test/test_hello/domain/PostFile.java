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

    private String origin_filename;
    private String stored_filename;
    private String filetype;
    private String filesize;

    private LocalDateTime cre_at = LocalDateTime.now();

    @Column(length = 30)
    private String cre_id;

    @Enumerated(EnumType.STRING)
    private User.YesNo del_yn = User.YesNo.N;
}