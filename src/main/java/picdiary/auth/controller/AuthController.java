package picdiary.auth.controller;

import lombok.RequiredArgsConstructor;

import java.util.Base64;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import picdiary.auth.dto.AuthRequest;
import picdiary.auth.service.AuthService;
import picdiary.global.dto.response.ApplicationResponse;
import picdiary.user.repository.UserEntity;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final AuthenticationProvider authenticationProvider;

    /**
     * 로그인
     */
    @PostMapping("/login")
    public ResponseEntity<ApplicationResponse<Object>> login(@RequestBody AuthRequest request) {
        var auth = new UsernamePasswordAuthenticationToken(request.email(), request.password());
        authenticationProvider.authenticate(auth);
        return ApplicationResponse.data(generateBasicToken(request)); // password

    }

    /**
     * 회원가입
     */
    @PostMapping("/signup")
    public ResponseEntity<ApplicationResponse<Long>> signUp(@RequestBody AuthRequest request) {
        UserEntity user = authService.createUser(request);
        return ApplicationResponse.success(user.getId(), "회원가입이 완료되었습니다.");
    }

    private String generateBasicToken(AuthRequest request) {
        String username = request.email();
        String password = request.password();
        return Base64.getEncoder()
            .encodeToString((username + ":" + password).getBytes());
    }
}
