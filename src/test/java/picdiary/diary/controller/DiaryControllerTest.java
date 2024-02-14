package picdiary.diary.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import picdiary.diary.controller.dto.request.DiaryCreateRequest;
import picdiary.global.dto.response.ApplicationResponse;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class DiaryControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Transactional
    public void testDiary() throws Exception {
        var date = "20240214";
        var headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Basic YWRtaW46MTIzNA==");
        var typeReference = new TypeReference<ApplicationResponse<Long>>() {
        };

        var request = new DiaryCreateRequest("단위테스트", date);
        var bytes = objectMapper.writeValueAsBytes(request);
        var requestBuilder = post("/diaries").content(bytes).contentType("application/json").headers(headers);
        var actions = mockMvc.perform(requestBuilder).andExpect(status().isOk()).andReturn();
        bytes = actions.getResponse().getContentAsByteArray();
        var diaryId = objectMapper.readValue(bytes, typeReference).getData();
        assertNotNull(diaryId);

        mockMvc.perform(delete("/diaries/" + diaryId).headers(headers)).andExpect(status().isOk()).andReturn();
    }
}
