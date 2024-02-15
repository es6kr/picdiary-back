package picdiary.diary.repository;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BaseEntity {
    LocalDateTime createdAt;
}
