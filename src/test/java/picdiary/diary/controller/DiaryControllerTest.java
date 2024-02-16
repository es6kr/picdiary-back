package picdiary.diary.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import picdiary.diary.service.DiaryService;
import picdiary.diary.service.S3Service;
import picdiary.global.dto.response.ApplicationResponse;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class DiaryControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private DiaryService diaryService;
    @MockBean
    private S3Service s3Service;
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

        // Mocking MultipartFile
        MockMultipartFile file = new MockMultipartFile("imageFile", "test.jpg", MediaType.IMAGE_JPEG_VALUE, "test image data".getBytes());

        var requestBuilder = multipart("/diaries").file(file).param("content", "Test content").param("date", "2024-02-16").headers(headers);
        var actions = mockMvc.perform(requestBuilder).andExpect(status().isOk()).andReturn();
        var bytes = actions.getResponse().getContentAsByteArray();
        var diaryId = objectMapper.readValue(bytes, typeReference).getData();
        assertNotNull(diaryId);

        mockMvc.perform(delete("/diaries/" + diaryId).headers(headers)).andExpect(status().isOk()).andReturn();
    }
}
