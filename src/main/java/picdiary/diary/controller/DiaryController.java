package picdiary.diary.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import picdiary.diary.domain.Diary;
import picdiary.diary.dto.request.DiaryCreateRequest;
import picdiary.diary.dto.request.DiaryUpdateRequest;
import picdiary.diary.dto.response.GetDiaryResponse;
import picdiary.diary.repository.DiaryEntity;
import picdiary.diary.service.DiaryService;
import picdiary.diary.service.S3Service;
import picdiary.global.dto.response.ApplicationResponse;
import picdiary.user.repository.UserEntity;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/diaries")
@SecurityRequirement(name = "basicAuth")
public class DiaryController {

    private final DiaryService diaryService;
    private final S3Service s3Service;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

    /**
     * 다이어리 작성
     */
    @PostMapping
    public ResponseEntity<ApplicationResponse<Long>> createDiary(UserEntity user, @RequestParam("imageFile") MultipartFile file, @RequestParam("content") String content, @RequestParam("date") String date, @RequestParam("emotion") Diary.Emotion emotion) {
        DiaryCreateRequest.DiaryCreateRequestBuilder builder = DiaryCreateRequest.builder().content(content).date(date)
            .emotion(emotion);

        /* AWS S3 파일 저장 */
        if (file != null) try {
            String prefix = String.format("%s/%s", user.getId(), date);
            builder.imageFileName(s3Service.saveFile(prefix, file));
        } catch (IOException e) {
            // 파일 저장 중 오류 발생 시 처리
            return ApplicationResponse.error("파일 저장 중 오류가 발생했습니다.", null);
        }

        Long diaryId = diaryService.createDiary(user.getId(), builder.build());
        return ApplicationResponse.success(diaryId, "다이어리가 생성되었습니다.").entity();
    }

    /**
     * 다이어리 정보 조회
     */
    @GetMapping("/{date}")
    public GetDiaryResponse getDiaryInfo(UserEntity user, @PathVariable("date") String date) {
        DiaryEntity diary = diaryService.getDiary(user.getId(), date);
        return new GetDiaryResponse(diary.getId(), diary.getContent(), diary.getDate()
            .format(formatter), diary.getEmotion(), s3Service.getUrl(diary.getImageFileName()));
    }

    /**
     * 다이어리 수정
     */
    @PatchMapping("/{diaryId}")
    public ApplicationResponse<Long> updateDiary(UserEntity user, @PathVariable("diaryId") Long diaryId, @RequestBody DiaryUpdateRequest request) {
        diaryService.updateDiary(user.getId(), diaryId, request);
        return ApplicationResponse.success(diaryId, "다이어리가 수정되었습니다.");
    }

    /**
     * 다이어리 삭제
     */
    @DeleteMapping("/{diaryId}")
    public ApplicationResponse<Long> deleteDiary(UserEntity user, @PathVariable("diaryId") Long diaryId) {
        diaryService.deleteDiary(user.getId(), diaryId);
        return ApplicationResponse.success(diaryId, "다이어리가 삭제되었습니다.");
    }
}
