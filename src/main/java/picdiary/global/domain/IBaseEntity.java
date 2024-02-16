package picdiary.global.domain;

import java.time.LocalDateTime;

public interface IBaseEntity {
    default LocalDateTime createdAt() {
        return getCreatedAt();
    }

    default LocalDateTime updatedAt() {
        return getUpdatedAt();        
    }

    default LocalDateTime getCreatedAt() {
        return createdAt();
    }

    default LocalDateTime getUpdatedAt() {
        return updatedAt();
    }
}
