package picdiary.diary.controller.dto.response;

import java.time.LocalDateTime;

public record GetDiaryResponse(Long diaryId, String content, LocalDateTime date) {
}
