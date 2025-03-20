package Test.test_hello.service;


import Test.test_hello.domain.User;
import Test.test_hello.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;
import jakarta.transaction.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerUser(String username, String email, String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 사용자 이름입니다.");
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password)); // ✅ 비밀번호 암호화

        //  기본 프로필 이미지 설정
        user.setProfileImageUrl("/images/default_image.png");
        //save
        userRepository.save(user);
    }
    // ✅ 사용자 조회 (Optional 사용)
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // ✅ 회원 정보 업데이트 (비밀번호 선택 변경 가능)
    @Transactional
    public boolean updateUser(String username, String email, String newPassword, String confirmPassword) {
        Optional<User> optionalUser = findByUsername(username);
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("사용자를 찾을 수 없습니다.");
        }

        User user = optionalUser.get();
        user.setEmail(email); // 이메일 업데이트

        // ✅ 비밀번호가 입력된 경우만 변경
        if (newPassword != null && !newPassword.isBlank()) {
            if (!newPassword.equals(confirmPassword)) {
                throw new RuntimeException("새 비밀번호가 일치하지 않습니다.");
            }
            user.setPassword(passwordEncoder.encode(newPassword));
        }

        userRepository.save(user);
        return true;
    }
    @Transactional
    public void updateProfileImage(String username, String imageUrl) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("사용자를 찾을 수 없습니다.");
        }

        User user = optionalUser.get();

        // ✅ 디버깅: 저장될 이미지 URL 확인
        System.out.println("🔍 저장될 프로필 이미지 URL: " + imageUrl);

        user.setProfileImageUrl(imageUrl);
        userRepository.save(user);
    }

}
