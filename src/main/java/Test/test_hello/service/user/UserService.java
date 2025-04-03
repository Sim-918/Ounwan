package Test.test_hello.service.user;


import Test.test_hello.domain.User;
import Test.test_hello.dto.UserRegisterDto;
import Test.test_hello.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getRole() == null) {
            user.setRole(User.Role.U);
        }
        userRepository.save(user);
    }
    public void register(UserRegisterDto dto){
        User user=new User();
        user.setUserId(dto.getUserId());
        user.setPhoneNum(dto.getPhoneNum());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(User.Role.U);
        userRepository.save(user);
    }

    public List<User> getUsersByDelYn(User.YesNo yn) {
        return userRepository.findAllByDelYn(yn);
    }
//    soft-delete 처리
    @Transactional
    public void deleteByUserId(String userId){
        User user=userRepository.findByUserId(userId)
        .orElseThrow(()->new NoSuchElementException("사용자 없음"));

        user.setDelYn(User.YesNo.Y);
        user.setUpdDt(LocalDateTime.now());
        user.setUpdId("admin");
        userRepository.save(user);
    }
//    계정 복원
    @Transactional
    public void restoreByUserId(String userId){
        User user=userRepository.findByUserId(userId)
                .orElseThrow(()->new NoSuchElementException("사용자 없음"));

        user.setDelYn(User.YesNo.N);
        user.setUpdDt(LocalDateTime.now());
        user.setUpdId("admin");
        userRepository.save(user);

    }
}
