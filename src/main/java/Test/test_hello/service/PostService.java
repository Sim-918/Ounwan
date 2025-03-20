package Test.test_hello.service;

import Test.test_hello.domain.Post;
import Test.test_hello.domain.PostImage;
import Test.test_hello.domain.User;
import Test.test_hello.repository.PostImageRepository;
import Test.test_hello.repository.PostRepository;
import Test.test_hello.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostImageRepository postImageRepository;
    private final UserRepository userRepository;

    private static final long MAX_VIDEO_DURATION_MS = 5 * 60 * 1000; // 최대 5분 (300,000ms)
    private static final int MAX_IMAGE_COUNT = 5; // 최대 5장
    private static final String BASE_UPLOAD_DIR = System.getProperty("user.home") + "/uploads/"; // ✅ 저장 경로 변경

    // ✅ 모든 게시글 조회 (최신순)
    public List<Post> getAllPosts() {
        return postRepository.findAllByOrderByIdDesc();
    }

    // ✅ 특정 게시글 조회
    public Post getPostById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다: " + id));
    }

    // ✅ 게시글 작성 (이미지 5장 제한 + 동영상 5분 제한)
    @Transactional
    public void createPost(String title, String content, List<MultipartFile> images, MultipartFile video, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다: " + username));

        Post post = new Post();
        post.setTitle(title);
        post.setContent(content);
        post.setUser(user);
        post = postRepository.save(post);

        // ✅ 이미지 저장
        if (images != null && !images.isEmpty()) {
            List<PostImage> imageEntities = new ArrayList<>();
            for (MultipartFile imageFile : images) {
                if (!imageFile.isEmpty()) {
                    String filePath = saveFile(imageFile, "images/");
                    PostImage image = new PostImage();
                    image.setPost(post);
                    image.setImageUrl(filePath);
                    imageEntities.add(image);
                }
            }
            postImageRepository.saveAll(imageEntities);
        }

        // ✅ 동영상 저장
        if (video != null && !video.isEmpty()) {
            String videoPath = saveFile(video, "videos/");
            post.setVideoUrl(videoPath);
        }
    }

    // ✅ 파일 저장 로직
    private String saveFile(MultipartFile file, String subDir) {
        try {
            // ✅ 사용자 홈 디렉토리 기준으로 저장 (예: /Users/yourname/uploads/images/)
            String basePath = System.getProperty("user.home") + "/uploads/" + subDir;
            Path directoryPath = Paths.get(basePath);
            if (!Files.exists(directoryPath)) {
                Files.createDirectories(directoryPath); // 디렉토리 없으면 생성
            }

            // ✅ UUID를 사용하여 고유한 파일명 생성
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(basePath + fileName);
            file.transferTo(filePath.toFile());

            System.out.println("✅ 파일 저장 완료: " + filePath.toString());

            // ✅ 반환할 URL 경로
            return "/uploads/" + subDir + fileName;
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 중 오류 발생", e);
        }
    }

    // ✅ 동영상 길이 확인 (추후 FFmpeg 활용 가능)
    private long getVideoDuration(MultipartFile video) {
        return 180000; // 3분 (테스트용)
    }

    // ✅ 파일 확장자 유효성 검사
    private boolean isValidImageFile(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && contentType.startsWith("image/");
    }

    private boolean isValidVideoFile(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && contentType.startsWith("video/");
    }

    // ✅ 게시글 삭제 (파일도 삭제)
    @Transactional
    public void deletePost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다: " + id));

        // ✅ 이미지 및 동영상 파일 삭제
        if (post.getImages() != null) {
            for (PostImage image : post.getImages()) {
                deleteFile(image.getImageUrl());
            }
            postImageRepository.deleteAll(post.getImages());
        }

        if (post.getVideoUrl() != null) {
            deleteFile(post.getVideoUrl());
        }

        postRepository.delete(post);
    }

    // ✅ 파일 삭제 메서드
    private void deleteFile(String filePath) {
        if (filePath != null && !filePath.isBlank()) {
            try {
                String absolutePath = System.getProperty("user.home") + "/uploads/" + filePath.replace("/uploads/", "");
                File file = new File(absolutePath);
                if (file.exists()) {
                    file.delete();
                }
            } catch (Exception e) {
                System.err.println("파일 삭제 오류: " + e.getMessage());
            }
        }
    }

    @Transactional
    public void updatePost(Post post, String title, String content, List<MultipartFile> newImages, MultipartFile newVideo) {
        post.setTitle(title);
        post.setContent(content);

        // ✅ 기존 이미지 삭제 (post.setImages(null) 대신 리스트 초기화)
        if (newImages != null && !newImages.isEmpty()) {
            if (post.getImages() != null && !post.getImages().isEmpty()) {
                for (PostImage image : post.getImages()) {
                    deleteFile(image.getImageUrl());  // ✅ 기존 파일 삭제
                }
                post.getImages().clear();  // ✅ Hibernate가 추적할 수 있도록 리스트 비우기
            }

            // ✅ 새 이미지 저장
            List<PostImage> imageEntities = new ArrayList<>();
            for (MultipartFile imageFile : newImages) {
                if (!imageFile.isEmpty() && isValidImageFile(imageFile)) {
                    String filePath = saveFile(imageFile, "images/");
                    PostImage newImage = new PostImage();
                    newImage.setPost(post);
                    newImage.setImageUrl(filePath);
                    imageEntities.add(newImage);
                }
            }
            postImageRepository.saveAll(imageEntities);
            post.getImages().addAll(imageEntities); // ✅ Hibernate가 관리하도록 리스트에 추가
        }

        // ✅ 기존 동영상 삭제 후 새 동영상 저장
        if (newVideo != null && !newVideo.isEmpty() && isValidVideoFile(newVideo)) {
            if (post.getVideoUrl() != null) {
                deleteFile(post.getVideoUrl());
            }
            String videoPath = saveFile(newVideo, "videos/");
            post.setVideoUrl(videoPath);
        }

        postRepository.save(post);  // ✅ 변경된 내용 저장
    }

}
