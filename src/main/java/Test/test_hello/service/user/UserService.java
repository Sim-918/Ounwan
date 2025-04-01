package Test.test_hello.service.user;


import Test.test_hello.domain.User;
import Test.test_hello.dto.UserRegisterDto;
import Test.test_hello.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
}
