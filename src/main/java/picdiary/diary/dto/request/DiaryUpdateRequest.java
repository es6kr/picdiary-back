package picdiary.diary.dto.request;

import picdiary.diary.domain.Diary;

public record DiaryUpdateRequest(String content, Diary.Emotion emotion) {
}
