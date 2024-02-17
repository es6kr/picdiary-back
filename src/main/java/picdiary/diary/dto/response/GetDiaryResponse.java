package picdiary.diary.dto.response;

import picdiary.diary.domain.Diary;

import java.net.URL;

public record GetDiaryResponse(Long diaryId, String content, String date, Diary.Emotion emotion, URL imageUrl) {
}
