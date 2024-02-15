package picdiary.diary.controller;

import org.springframework.util.Assert;
import picdiary.diary.dto.request.DiaryCreateRequest;
import picdiary.diary.dto.request.DiaryUpdateRequest;
import picdiary.diary.dto.response.GetDiaryResponse;
import picdiary.diary.service.DiaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import picdiary.global.domain.CustomUserDetails;
import picdiary.global.dto.response.ApplicationResponse;

@RestController
@RequestMapping("/diaries")
@RequiredArgsConstructor
public class DiaryController {

    private final DiaryService diaryService;

    /**
     * 다이어리 작성
     */
    @PostMapping
    public ResponseEntity<ApplicationResponse<Long>> createDiary(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                 @RequestBody DiaryCreateRequest request) {
        Assert.notNull(userDetails, "로그인이 필요합니다.");
        Long diaryId = diaryService.createDiary(userDetails.userEntity().getId(), request);
        return ApplicationResponse.success(diaryId, "다이어리가 생성되었습니다.");
    }

    /**
     * 다이어리 정보 조회
     */
    @GetMapping
    public ResponseEntity<ApplicationResponse<GetDiaryResponse>> getDiaryInfo(
            @AuthenticationPrincipal CustomUserDetails userDetails, @RequestParam("date") String date) {
        Assert.notNull(userDetails, "로그인이 필요합니다.");
        GetDiaryResponse userDiaryResponse = diaryService.getDiaryInfo(userDetails.userEntity().getId(), date);
        return ApplicationResponse.data(userDiaryResponse);
    }

    /**
     * 다이어리 수정
     */
    @PatchMapping("/{diaryId}")
    public ResponseEntity<ApplicationResponse<Long>> updateDiary(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable("diaryId") Long diaryId,
            @RequestBody DiaryUpdateRequest request) {
        diaryService.updateDiary(userDetails.userEntity().getId(), diaryId, request);
        return ApplicationResponse.success(diaryId, "다이어리가 수정되었습니다.");
    }

    /**
     * 다이어리 삭제
     */
    @DeleteMapping("/{diaryId}")
    public ResponseEntity<ApplicationResponse<Long>> deleteDiary(
            @AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable("diaryId") Long diaryId) {
        diaryService.deleteDiary(userDetails.userEntity().getId(), diaryId);
        return ApplicationResponse.success(diaryId, "다이어리가 삭제되었습니다.");
    }
}
