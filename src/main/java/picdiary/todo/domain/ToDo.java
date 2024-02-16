package picdiary.todo.domain;

import java.time.LocalDateTime;

import picdiary.global.domain.IBaseEntity;

/**
 * @param content 투두리스트 내용
 * @param date      캘린더 설정 날짜
 *
 * @param createdAt 투두리스트 생성 일자
 * @param updatedAt 투두리스트 수정 일자
 * 
 * @param diaryId   일기 식별자
 *
 */
public record ToDo(String content, LocalDateTime date, LocalDateTime createdAt,
                   LocalDateTime updatedAt, Long diaryId) implements IBaseEntity {
}
