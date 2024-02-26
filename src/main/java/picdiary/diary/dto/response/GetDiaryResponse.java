package picdiary.diary.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import picdiary.diary.domain.Diary;

import java.net.URL;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record GetDiaryResponse(Long diaryId, String content, String date, Diary.Emotion emotion, URL imageUrl) {
}
