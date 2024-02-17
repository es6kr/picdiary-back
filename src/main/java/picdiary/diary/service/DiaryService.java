package picdiary.diary.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import picdiary.diary.dto.request.DiaryCreateRequest;
import picdiary.diary.dto.request.DiaryUpdateRequest;
import picdiary.diary.exception.DiaryErrorCode;
import picdiary.diary.repository.DiaryEntity;
import picdiary.diary.repository.DiaryJpaRepository;
import picdiary.global.exception.ApplicationException;
import picdiary.user.repository.UserEntity;
import picdiary.user.service.UserService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@Transactional
@RequiredArgsConstructor
public class DiaryService {
    private final DiaryJpaRepository diaryRepository;
    private final UserService userService;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

    /**
     * 다이어리 작성
     */
    public Long createDiary(Long userId, DiaryCreateRequest request) {
        LocalDate localDate = LocalDate.parse(request.getDate(), formatter);

        UserEntity savedUser = userService.findUserById(userId);

        DiaryEntity diaryEntity = new DiaryEntity(
                request.getContent(),
                localDate,
                request.getImageFileName(),
                savedUser
        );
        return diaryRepository.save(diaryEntity).getId();
    }

    /**
     * 다이어리 조회
     */
    public DiaryEntity getDiary(Long userId, String date) {
        String dateString = String.valueOf(date);
        LocalDate localDate = LocalDate.parse(dateString, formatter);
        return diaryRepository.findByUserIdAndDate(userId, localDate)
            .orElseThrow(() -> new ApplicationException(DiaryErrorCode.NO_DIARY));
    }

    /**
     * 다이어리 수정
     *
     * @return 수정된 diary
     */
    public DiaryEntity updateDiary(Long userId, Long diaryId, DiaryUpdateRequest request) {
        DiaryEntity savedDiary = findDiaryById(diaryId);

        savedDiary.diaryUpdate(userId, request.content());
        return diaryRepository.save(savedDiary);
    }

    /**
     * 다이어리 삭제
     */
    public void deleteDiary(Long userId, Long diaryId) {
        DiaryEntity savedDiary = findDiaryById(diaryId);

        savedDiary.validateUserIsWriter(userId);
        diaryRepository.deleteById(savedDiary.getId());
    }

    private DiaryEntity findDiaryById(Long diaryId) {
        return diaryRepository.findById(diaryId)
                .orElseThrow(() -> new ApplicationException(DiaryErrorCode.NO_DIARY));
    }

}
