package picdiary.diary.domain;

import java.time.LocalDateTime;

/**
 * @param diaryId   일기 식별자
 * @param userId    일기 작성자 ID
 * @param date      캘린더 설정 날짜
 *
 * @param content   일기 내용
 * @param createdAt 일기 생성 일자
 * @param updatedAt 일기 수정 일자
 * @param emotion   일기의 감정
 *
 * @param imageFileName 사진
 */
public record Diary(Long diaryId, Long userId, LocalDateTime date, String content, LocalDateTime createdAt,
                    LocalDateTime updatedAt, Diary.Emotion emotion, String imageFileName) {
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
