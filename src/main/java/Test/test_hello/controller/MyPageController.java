package Test.test_hello.controller;


import Test.test_hello.domain.User;
import Test.test_hello.dto.UserUpdateRequest;
import Test.test_hello.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;


import java.util.Optional;

@Controller
@RequestMapping("/mypage")
public class MyPageController {

    private final UserService userService;

    public MyPageController(UserService userService) {
        this.userService = userService;
    }

    // ✅ 마이페이지 조회
    @GetMapping
    public String myPage(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        if (userDetails == null) {
            return "redirect:/login";
        }

        Optional<User> userOptional = userService.findByUsername(userDetails.getUsername());
        if (userOptional.isEmpty()) {
            return "redirect:/";
        }

        model.addAttribute("user", userOptional.get());
        return "my_page";
    }

    // ✅ 회원 정보 수정
    @PostMapping("/update")
    public String updateUser(@AuthenticationPrincipal UserDetails userDetails,
                             @ModelAttribute UserUpdateRequest userUpdateRequest,
                             Model model) {

        if (userDetails == null) {
            return "redirect:/login";
        }

        try {
            boolean success = userService.updateUser(userDetails.getUsername(),
                    userUpdateRequest.getEmail(),
                    userUpdateRequest.getNewPassword(),
                    userUpdateRequest.getConfirmPassword());

            if (success) {
                model.addAttribute("successMessage", "정보가 성공적으로 수정되었습니다.");
            }
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
        }

        return myPage(userDetails, model);
    }
    // 이미지 변경
    @PostMapping("/upload-profile")
    public String uploadProfileImage(@RequestParam("image") MultipartFile imageFile,
                                     Principal principal,
                                     RedirectAttributes redirectAttributes) {
        if (principal == null) {
            return "redirect:/login";
        }

        String username = principal.getName();
        String uploadDir = "C:/upload/images/";  // ✅ 로컬 저장 경로
        String fileName = username + "_" + imageFile.getOriginalFilename(); // ✅ 유저명_파일명 형식

        try {
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath); // ✅ 디렉토리 생성
            }

            Path filePath = uploadPath.resolve(fileName);
            imageFile.transferTo(filePath.toFile());

            // ✅ 웹에서 접근 가능한 경로로 저장
            String imageUrl = "/upload/images/" + fileName;

            //디버깅
            System.out.println("🔍 저장된 이미지 URL: " + imageUrl);

            userService.updateProfileImage(username, imageUrl);

            redirectAttributes.addFlashAttribute("successMessage", "프로필 이미지가 변경되었습니다.");
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "이미지 업로드 중 오류 발생: " + e.getMessage());
        }

        return "redirect:/mypage";
    }

}