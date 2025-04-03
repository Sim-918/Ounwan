package Test.test_hello.service.post;


import Test.test_hello.domain.PostFile;
import Test.test_hello.repository.PostFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostFileService {
    private final PostFileRepository postFileRepository;

    public PostFile saveFile(PostFile file) {
        return postFileRepository.save(file);
    }

    public List<PostFile> getAllFiles() {
        return postFileRepository.findAll();
    }
}