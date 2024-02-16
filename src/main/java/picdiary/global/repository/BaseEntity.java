package picdiary.global.repository;

import lombok.Data;
import picdiary.global.domain.IBaseEntity;

import java.time.LocalDateTime;

@Data
public class BaseEntity implements IBaseEntity {
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
