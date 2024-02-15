package picdiary.diary.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import picdiary.diary.repository.DiaryEntity;
import picdiary.user.repository.UserEntity;

import java.time.LocalDateTime;
import java.util.Optional;

public interface DiaryJpaRepository extends JpaRepository<DiaryEntity, Long> {

    Optional<DiaryEntity> findByUser(UserEntity user);

    Optional<DiaryEntity> findByUserAndDate(UserEntity user, LocalDateTime date);
}
