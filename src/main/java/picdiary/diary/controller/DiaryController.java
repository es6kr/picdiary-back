package picdiary.diary.controller;

import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;
import picdiary.diary.dto.request.DiaryCreateRequest;
import picdiary.diary.dto.request.DiaryUpdateRequest;
import picdiary.diary.dto.response.GetDiaryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import picdiary.diary.repository.DiaryEntity;
import picdiary.diary.service.DiaryService;
import picdiary.diary.service.S3Service;
import picdiary.global.domain.CustomUserDetails;
import picdiary.global.dto.response.ApplicationResponse;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/diaries")
@RequiredArgsConstructor
public class DiaryController {

    private final DiaryService diaryService;
    private final S3Service s3Service;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

    /**
     * 다이어리 작성
     */
    @PostMapping
    public ResponseEntity<ApplicationResponse<Long>> createDiary(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestParam("imageFile") MultipartFile file, @RequestParam("content") String content, @RequestParam("date") String date) {
        Assert.notNull(userDetails, "로그인이 필요합니다.");
        DiaryCreateRequest.DiaryCreateRequestBuilder builder = DiaryCreateRequest.builder().content(content).date(date);

        /* AWS S3 파일 저장 */
        if (file != null) try {
            String prefix = String.format("%s/%s", userDetails.userEntity().getId(), date);
            builder.imageFileName(s3Service.saveFile(prefix, file));
        } catch (IOException e) {
            // 파일 저장 중 오류 발생 시 처리
            return ApplicationResponse.error("파일 저장 중 오류가 발생했습니다.", null);
        }

        Long diaryId = diaryService.createDiary(userDetails.userEntity().getId(), builder.build());
        return ApplicationResponse.success(diaryId, "다이어리가 생성되었습니다.");
    }

    /**
     * 다이어리 정보 조회
     */
    @GetMapping("/{date}")
    public GetDiaryResponse getDiaryInfo(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable("date") String date) {
        Assert.notNull(userDetails, "로그인이 필요합니다.");
        DiaryEntity diary = diaryService.getDiary(userDetails.userEntity().getId(), date);
        return new GetDiaryResponse(diary.getId(), diary.getContent(), diary.getDate().format(formatter), s3Service.getUrl(diary.getImageFileName()));
    }

    /**
     * 다이어리 수정
     */
    @PatchMapping("/{diaryId}")
    public ResponseEntity<ApplicationResponse<Long>> updateDiary(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable("diaryId") Long diaryId, @RequestBody DiaryUpdateRequest request) {
        diaryService.updateDiary(userDetails.userEntity().getId(), diaryId, request);
        return ApplicationResponse.success(diaryId, "다이어리가 수정되었습니다.");
    }

    /**
     * 다이어리 삭제
     */
    @DeleteMapping("/{diaryId}")
    public ResponseEntity<ApplicationResponse<Long>> deleteDiary(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable("diaryId") Long diaryId) {
        diaryService.deleteDiary(userDetails.userEntity().getId(), diaryId);
        return ApplicationResponse.success(diaryId, "다이어리가 삭제되었습니다.");
    }
}
