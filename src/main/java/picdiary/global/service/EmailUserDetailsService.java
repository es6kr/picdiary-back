package picdiary.global.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import picdiary.global.domain.CustomUserDetails;
import picdiary.user.repository.UserJpaRepository;

@Service
@RequiredArgsConstructor
public class EmailUserDetailsService implements UserDetailsService {
    private final UserJpaRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("해당하는 유저를 찾을 수 없습니다."));

        return new CustomUserDetails(user);
    }
}
