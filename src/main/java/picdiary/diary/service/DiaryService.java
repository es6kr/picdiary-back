package picdiary.diary.service;

import picdiary.diary.controller.dto.request.DiaryCreateRequest;
import picdiary.diary.controller.dto.request.DiaryUpdateRequest;
import picdiary.diary.controller.dto.response.GetDiaryResponse;
import org.springframework.stereotype.Service;

@Service
public interface DiaryService {

    GetDiaryResponse getDiaryInfo(Long userEmail, String date);

    Long createDiary(Long userId, DiaryCreateRequest request);

    void updateDiary(Long userId, Long diaryId, DiaryUpdateRequest request);

    void deleteDiary(Long userId, Long diaryId);

}
