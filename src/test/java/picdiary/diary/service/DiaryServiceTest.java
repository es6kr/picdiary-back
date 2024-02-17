package picdiary.diary.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import picdiary.diary.dto.request.DiaryCreateRequest;
import picdiary.diary.dto.request.DiaryUpdateRequest;
import picdiary.diary.repository.DiaryEntity;

import static org.junit.jupiter.api.Assertions.*;


@Transactional
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DiaryServiceTest {

    @Autowired
    private DiaryService diaryService;

    long userId = 1L;
    String date = "20240123";
    Long diaryId;

    @Test
    public void testDiary() {
        DiaryCreateRequest createRequest = DiaryCreateRequest.builder().date(date).build();
        diaryId = diaryService.createDiary(userId, createRequest);
        assertNotNull(diaryId);

        DiaryEntity diary = diaryService.getDiary(userId, date);
        assertNotNull(diary);
        assertNotNull(diary.createdAt());
        assertNotNull(diary.updatedAt());

        var content = "testUpdateDiary";
        DiaryUpdateRequest updateRequest = new DiaryUpdateRequest(content, false);
        diary = diaryService.updateDiary(userId, diaryId, updateRequest);
        assertEquals(content, diary.getContent());
        assertNotEquals(diary.createdAt(), diary.updatedAt());

        diaryService.deleteDiary(userId, diaryId);
    }
}
