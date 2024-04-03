package picdiary.diary.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import picdiary.diary.dto.request.DiaryCreateRequest;
import picdiary.diary.dto.request.DiaryUpdateRequest;
import picdiary.diary.dto.response.GetDiaryResponse;
import picdiary.diary.exception.DiaryErrorCode;
import picdiary.diary.repository.DiaryEntity;
import picdiary.diary.service.DiaryService;
import picdiary.diary.service.S3Service;
import picdiary.global.dto.response.ApplicationResponse;
import picdiary.global.exception.ApplicationException;
import picdiary.user.repository.UserEntity;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

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
    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<ApplicationResponse<Long>> createDiary(@Parameter(hidden = true) UserEntity user, @RequestBody DiaryCreateRequest request) {
        Long diaryId = diaryService.createDiary(user.getId(), request);
        return ApplicationResponse.success(diaryId, "다이어리가 생성되었습니다.").entity();
    }

    /**
     * AWS S3 파일 저장
     */
    private void createDiaryImageFile(UserEntity user, DiaryUpdateRequest request, String date) {
        var file = request.getImageFile();
        if (file != null) try {
            String prefix = String.format("%s/%s", user.getId(), date);
            request.setImageFileName(s3Service.saveFile(prefix, file));
        } catch (IOException e) {
            // 파일 저장 중 오류 발생 시 처리
            throw new ApplicationException(DiaryErrorCode.SAVE_ERROR);
        }
    }

    /**
     * Multipart 다이어리 작성
     */
    @PostMapping(consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApplicationResponse<Long>> createForm(@Parameter(hidden = true) UserEntity user, @ModelAttribute DiaryCreateRequest request) {
        createDiaryImageFile(user, request, request.getDate());
        Long diaryId = diaryService.createDiary(user.getId(), request);
        return ApplicationResponse.success(diaryId, "다이어리가 생성되었습니다.").entity();
    }

    @GetMapping
    @Operation(description = "다이어리 월별 조회")
    public ApplicationResponse<Stream<GetDiaryResponse>> getDiaryByMonth(@Parameter(hidden = true) UserEntity user, @Parameter(examples = {@ExampleObject("202402")}) @RequestParam("month") String month) {
        var diaries = diaryService.getDiaryByMonth(user.getId(), month);
        var data = diaries.stream().map(diary -> new GetDiaryResponse(diary.getId(), diary.getContent(), diary.getDate()
            .format(formatter), diary.getEmotion(), s3Service.getUrl(diary.getImageFileName())));
        return ApplicationResponse.data(data);
    }

    /**
     * 다이어리 정보 조회
     */
    @GetMapping("/{date}")
    public GetDiaryResponse getDiaryInfo(@Parameter(hidden = true) UserEntity user, @PathVariable("date") String date) {
        DiaryEntity diary = diaryService.getDiary(user.getId(), date);
        return new GetDiaryResponse(diary.getId(), diary.getContent(), diary.getDate()
            .format(formatter), diary.getEmotion(), s3Service.getUrl(diary.getImageFileName()));
    }

    /**
     * 다이어리 수정
     */
    @PatchMapping(value = "/{diaryId}", consumes = APPLICATION_JSON_VALUE)
    public ApplicationResponse<Long> updateDiary(@Parameter(hidden = true) UserEntity user, @PathVariable("diaryId") Long diaryId, @RequestBody DiaryUpdateRequest request) {
        diaryService.updateDiary(user.getId(), diaryId, request);
        return ApplicationResponse.success(diaryId, "다이어리가 수정되었습니다.");
    }

    /**
     * 다이어리 수정
     */
    @PatchMapping(value = "/{diaryId}", consumes = MULTIPART_FORM_DATA_VALUE)
    public ApplicationResponse<Long> updateForm(@Parameter(hidden = true) UserEntity user, @PathVariable("diaryId") Long diaryId, @ModelAttribute DiaryUpdateRequest request) {
        var diary = diaryService.findDiaryById(diaryId);
        createDiaryImageFile(user, request, diary.getDate().format(formatter));
        diaryService.updateDiary(user.getId(), diaryId, request);
        return ApplicationResponse.success(diaryId, "다이어리가 수정되었습니다.");
    }

    /**
     * 다이어리 삭제
     */
    @DeleteMapping("/{diaryId}")
    public ApplicationResponse<Long> deleteDiary(@Parameter(hidden = true) UserEntity user, @PathVariable("diaryId") Long diaryId) {
        DiaryEntity diary = diaryService.findDiaryById(diaryId);
        if (diary.getImageFileName() != null) {
            s3Service.deleteImage(diary.getImageFileName());
        }
        diaryService.deleteDiary(user.getId(), diary);
        return ApplicationResponse.success(diaryId, "다이어리가 삭제되었습니다.");
    }
}
