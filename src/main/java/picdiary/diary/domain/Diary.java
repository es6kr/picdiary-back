package picdiary.diary.domain;

import java.time.LocalDateTime;

/**
 * @param diaryId   일기 식별자
 * @param userId    일기 작성자 ID
 * @param content   일기 내용
 * @param date      캘린더 설정 날짜
 * @param createdAt 일기 생성 일자
 * @param updatedAt 일기 수정 일자
 * @param emotion   일기의 감정
 */
public record Diary(Long diaryId, Long userId, String content, LocalDateTime date, LocalDateTime createdAt,
                    LocalDateTime updatedAt, Diary.Emotion emotion) {
    // 감정을 나타내는 열거형
    public enum Emotion {
        HAPPY,
        SAD,
        ANGRY,
        EXCITED,
        CALM,
        CONFUSED,
        LOVE,
        SURPRISED,
        EXERCISE
    }
}
