package picdiary.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import picdiary.global.exception.ApplicationException;
import picdiary.user.exception.UserErrorCode;
import picdiary.user.repository.UserEntity;
import picdiary.user.repository.UserJpaRepository;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserJpaRepository userRepository;

    public UserEntity findUserById(long diaryId) {
        return userRepository.findById(diaryId)
                .orElseThrow(() -> new ApplicationException(UserErrorCode.NOT_FOUND_USER));
    }
}
