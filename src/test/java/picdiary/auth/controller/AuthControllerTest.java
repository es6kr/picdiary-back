package picdiary.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import picdiary.auth.dto.AuthRequest;
import picdiary.auth.service.AuthService;
import picdiary.global.dto.response.ApplicationResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @see AuthController
 */
@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private AuthService authService;

    @Test
    @Transactional
    public void testLogin() throws Exception {
        var email = "test@test.com";
        var password = "password";
        var request = new AuthRequest(email, password);
        var token = "dGVzdEB0ZXN0LmNvbTpwYXNzd29yZA==";
        var user = authService.createUser(request);

        var loginPayload = objectMapper.writeValueAsBytes(request);
        var getLoginActions = mockMvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON)
                .content(loginPayload))
            .andExpect(status().isOk())
            .andReturn();
        var loginResponse = objectMapper.readValue(getLoginActions.getResponse()
            .getContentAsByteArray(), ApplicationResponse.class);
        assertEquals(token, loginResponse.getData());

        authService.deleteUser(user);
    }

}
