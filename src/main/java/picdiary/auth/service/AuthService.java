package picdiary.auth.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import picdiary.auth.dto.LoginRequest;
import picdiary.user.repository.UserEntity;
import picdiary.user.repository.UserJpaRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final PasswordEncoder passwordEncoder;
    private final UserJpaRepository userRepository;

    public UserEntity createUser(LoginRequest request) {
        UserEntity newUser = findOrCreate(request.email());
        newUser.setPassword(passwordEncoder.encode(request.password()));
        return userRepository.save(newUser);
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
