package picdiary.diary.dto.response;

import java.net.URL;

public record GetDiaryResponse(Long diaryId, String content, String date, URL imageUrl) {
}
