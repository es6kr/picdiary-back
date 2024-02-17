package picdiary.diary.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import picdiary.diary.dto.response.GetDiaryResponse;
import picdiary.diary.service.S3Service;
import picdiary.global.dto.response.ApplicationResponse;

import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class DiaryControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private S3Service s3Service;

    @Test
    @Transactional
    public void testDiary() throws Exception {
        var content = "Test content";
        var date = "20240214";
        var fileName = "test.jpg";
        var headers = new HttpHeaders();
        var userId = 1L;
        var imageFileName = String.format("%d/%s%s", userId, date, fileName);
        var imageUrl = new URL("http://aws.test/" + imageFileName);

        headers.add(HttpHeaders.AUTHORIZATION, "Basic YWRtaW46MTIzNA==");
        var typeReference = new TypeReference<ApplicationResponse<Long>>() {
        };

        // Mocking MultipartFile
        MockMultipartFile file = new MockMultipartFile("imageFile", fileName, MediaType.IMAGE_JPEG_VALUE, "test image data".getBytes());
        Mockito.when(s3Service.getUrl(Mockito.anyString())).thenReturn(imageUrl);
        Mockito.when(s3Service.saveFile(Mockito.anyString(), Mockito.any())).thenReturn(imageFileName);

        var requestBuilder = multipart("/diaries").file(file).param("content", content).param("date", date).headers(headers);
        var actions = mockMvc.perform(requestBuilder).andExpect(status().isOk()).andReturn();
        var bytes = actions.getResponse().getContentAsByteArray();
        var diaryId = objectMapper.readValue(bytes, typeReference).getData();
        assertNotNull(diaryId);

        /* 다이어리 조회 */
        var getDiaryActions = mockMvc.perform(get("/diaries/" + date).headers(headers)).andExpect(status().isOk()).andReturn();
        var getDiaryBytes = getDiaryActions.getResponse().getContentAsByteArray();
        var diaryDetails = objectMapper.readValue(getDiaryBytes, GetDiaryResponse.class);
        assertEquals(content, diaryDetails.content());
        assertEquals(date, diaryDetails.date());
        assertEquals(imageUrl, diaryDetails.imageUrl());

        /* 다이어리 삭제 */
        mockMvc.perform(delete("/diaries/" + diaryId).headers(headers)).andExpect(status().isOk()).andReturn();
    }
}
