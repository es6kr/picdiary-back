package picdiary;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import picdiary.user.repository.UserEntity;
import picdiary.user.repository.UserJpaRepository;

import java.util.Optional;

@Component
@AllArgsConstructor
public class DefaultUserInitializer implements CommandLineRunner {
    private final UserJpaRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        UserEntity user = findOrCreate("admin");
        user.setPassword(passwordEncoder.encode("1234")); // 패스워드 인코딩
        // 다른 필드 설정 가능
        userRepository.save(user);
    }

    @Transactional
    public UserEntity findOrCreate(final String email) {
        Optional<UserEntity> result = userRepository.findByEmail(email);
        if (result.isEmpty()) {
            UserEntity user = new UserEntity();
            user.setEmail(email);
            return user;
        }
        return result.get();
    }
}
