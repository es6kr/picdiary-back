package picdiary;

import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import picdiary.auth.dto.AuthRequest;
import picdiary.auth.service.AuthService;

@Component
@AllArgsConstructor
public class DefaultUserInitializer implements CommandLineRunner {
    private final AuthService authService;

    @Override
    public void run(String... args) throws Exception {
        authService.createUser(new AuthRequest("admin", "1234"));
    }
}
