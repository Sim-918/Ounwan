package Test.test_hello.controller;

import Test.test_hello.domain.Post;
import Test.test_hello.domain.PostImage;
import Test.test_hello.service.PostService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.security.Principal;
import java.util.List;


@Controller
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    // ✅ 게시글 전체 조회 (비회원도 접근 가능)
    @GetMapping
    public String listPosts(Model model, Principal principal) {
        List<Post> posts = postService.getAllPosts();
        model.addAttribute("posts", posts);

        // ✅ 현재 로그인한 사용자 정보를 Thymeleaf에 전달
        if (principal != null) {
            model.addAttribute("currentUsername", principal.getName());
        } else {
            model.addAttribute("currentUsername", ""); // 비회원일 경우 빈 문자열
        }
        return "home"; // home.html에서 목록 출력
    }

    // ✅ 게시글 상세 조회 (비회원도 접근 가능)
    @GetMapping("/{id}")
    public String viewPost(@PathVariable("id") Long id, Model model) {
        Post post = postService.getPostById(id);

        //  디버깅 로그 추가
        System.out.println("🔍 게시글 ID: " + id);
        System.out.println("🔍 제목: " + post.getTitle());
        System.out.println("🔍 동영상 URL: " + post.getVideoUrl());

        if (post.getImages() != null) {
            for (PostImage image : post.getImages()) {
                System.out.println("🔍 이미지 URL: " + image.getImageUrl());
            }
        }

        model.addAttribute("post", post);
        return "post_detail";
    }

    // ✅ 게시글 작성 폼 (GET 요청)
    @GetMapping("/create")
    public String showCreateForm(Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        return "post_create";
    }

    // ✅ 게시글 작성 처리
    @PostMapping("/create")
    public String createPost(
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam(value = "images", required = false) List<MultipartFile> images,
            @RequestParam(value = "video", required = false) MultipartFile video,
            Principal principal,
            RedirectAttributes redirectAttributes) {

        System.out.println("🔍 게시글 생성 요청: 제목=" + title);
        System.out.println("🔍 이미지 개수: " + (images != null ? images.size() : 0));
        System.out.println("🔍 동영상 업로드 여부: " + (video != null && !video.isEmpty()));

        if (principal == null) {
            return "redirect:/login";
        }

        try {
            postService.createPost(title, content, images, video, principal.getName());
            redirectAttributes.addFlashAttribute("successMessage", "게시글이 성공적으로 등록되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "게시글 등록 중 오류 발생: " + e.getMessage());
        }

        return "redirect:/";  // ✅ 성공 후 홈으로 리디렉션
    }


    // ✅ 게시글 수정 폼 (GET 요청)
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }

        Post post = postService.getPostById(id);

        // ✅ 본인 글인지 확인
        if (!post.getUser().getUsername().equals(principal.getName())) {
            throw new RuntimeException("자신이 작성한 게시글만 수정할 수 있습니다!");
        }

        model.addAttribute("post", post);
        return "post_edit";  // ✅ 수정 페이지 렌더링 (post_edit.html)
    }

    // ✅ 게시글 수정 처리 (POST 요청)
    @PostMapping("/edit/{id}")
    public String updatePost(
            @PathVariable("id") Long id,
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam(value = "newImages", required = false) List<MultipartFile> newImages,
            @RequestParam(value = "newVideo", required = false) MultipartFile newVideo,
            Principal principal,
            RedirectAttributes redirectAttributes) {

        if (principal == null) {
            return "redirect:/login";
        }

        Post post = postService.getPostById(id);

        // ✅ 현재 로그인한 사용자가 게시글 작성자인지 확인
        if (!post.getUser().getUsername().equals(principal.getName())) {
            throw new RuntimeException("자신이 작성한 게시글만 수정할 수 있습니다!");
        }

        postService.updatePost(post, title, content, newImages, newVideo);

        redirectAttributes.addFlashAttribute("successMessage", "게시글이 성공적으로 수정되었습니다.");
        return "redirect:/posts/" + id;  // ✅ 수정 후 해당 게시글 상세 페이지로 이동
    }



    // ✅ 게시글 삭제
    @PostMapping("/delete/{id}")
    public String deletePost(@PathVariable("id") Long id, Principal principal, RedirectAttributes redirectAttributes) {
        if (principal == null) {
            return "redirect:/login";
        }

        try {
            Post post = postService.getPostById(id);

            // ✅ 현재 로그인한 사용자가 게시글 작성자인지 확인
            if (!post.getUser().getUsername().equals(principal.getName())) {
                throw new RuntimeException("자신이 작성한 게시글만 삭제할 수 있습니다!");
            }

            postService.deletePost(id);
            redirectAttributes.addFlashAttribute("successMessage", "게시글이 성공적으로 삭제되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "게시글 삭제 중 오류 발생: " + e.getMessage());
        }

        return "redirect:/";
    }

}
