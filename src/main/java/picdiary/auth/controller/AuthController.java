package picdiary.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import picdiary.auth.dto.LoginRequest;
import picdiary.auth.service.AuthService;
import picdiary.global.dto.response.ApplicationResponse;
import picdiary.user.repository.UserEntity;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * 회원가입
     */
    @PostMapping("/signup")
    public ResponseEntity<ApplicationResponse<Long>> signUp(@RequestBody LoginRequest request) {
        UserEntity user = authService.createUser(request);
        return ApplicationResponse.success(user.getId(), "회원가입이 완료되었습니다.");
    }

}
