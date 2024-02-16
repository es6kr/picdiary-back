package picdiary.diary.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import picdiary.diary.dto.request.DiaryCreateRequest;
import picdiary.diary.dto.request.DiaryUpdateRequest;
import picdiary.diary.dto.response.GetDiaryResponse;
import picdiary.diary.exception.DiaryErrorCode;
import picdiary.diary.repository.DiaryEntity;
import picdiary.diary.repository.DiaryJpaRepository;
import picdiary.global.exception.ApplicationException;
import picdiary.user.exception.UserErrorCode;
import picdiary.user.repository.UserEntity;
import picdiary.user.repository.UserJpaRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Transactional
public class DiaryServiceImpl implements DiaryService {

    private final UserJpaRepository userRepository;
    private final DiaryJpaRepository diaryRepository;
    private final S3Service s3Service;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

    private GetDiaryResponse result(DiaryEntity diary) {
        return new GetDiaryResponse(
                diary.getId(),
                diary.getContent(),
                diary.getDate().format(formatter),
                s3Service.getUrl(diary.getImageFileName())
        );
    }

    /**
     * 다이어리 작성
     */
    @Override
    public Long createDiary(Long userId, DiaryCreateRequest request) {
        String dateString = String.valueOf(request.getDate());
        LocalDate localDate = LocalDate.parse(dateString, formatter);

        UserEntity savedUser = findUserById(userId);

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
    @Override
    public GetDiaryResponse getDiaryInfo(Long userId, String date) {
        String dateString = String.valueOf(date);
        LocalDate localDate = LocalDate.parse(dateString, formatter);

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ApplicationException(UserErrorCode.NOT_FOUND_USER));
        DiaryEntity diary = diaryRepository.findByUserAndDate(user, localDate.atStartOfDay())
                .orElseThrow(() -> new ApplicationException(DiaryErrorCode.NO_DIARY));

        return result(diary);
    }

    /**
     * 다이어리 수정
     */

    @Override
    public void updateDiary(Long userEmail, Long diaryId, DiaryUpdateRequest request) {
        UserEntity savedUser = findUserById(userEmail);
        DiaryEntity savedDiary = findDiaryById(diaryId);

        savedDiary.diaryUpdate(savedUser, request.content());
    }

    /**
     * 다이어리 삭제
     */
    @Override
    public void deleteDiary(Long userId, Long diaryId) {
        UserEntity savedUser = findUserById(userId);
        DiaryEntity savedDiary = findDiaryById(diaryId);

        savedDiary.validateUserIsWriter(savedUser);
        diaryRepository.deleteById(savedDiary.getId());
    }

    private UserEntity findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ApplicationException(UserErrorCode.NOT_EXIST));
    }

    private DiaryEntity findDiaryById(Long diaryId) {
        return diaryRepository.findById(diaryId)
                .orElseThrow(() -> new ApplicationException(DiaryErrorCode.NO_DIARY));
    }

}
