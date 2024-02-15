package picdiary;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import picdiary.auth.service.AuthService;
import picdiary.user.repository.UserEntity;
import picdiary.user.repository.UserJpaRepository;

import java.util.Optional;

@Component
@AllArgsConstructor
public class DefaultUserInitializer implements CommandLineRunner {
    private final AuthService authService;
    private final UserJpaRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        UserEntity user = authService.findOrCreate("admin");
        user.setPassword(passwordEncoder.encode("1234")); // 패스워드 인코딩
        userRepository.save(user);
    }
}
