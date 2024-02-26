package picdiary.diary.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

public interface DiaryJpaRepository extends JpaRepository<DiaryEntity, Long> {
    Optional<DiaryEntity> findByUserIdAndDate(long user, LocalDate date);

    Collection<DiaryEntity> findByUserIdAndDateBetween(long userId, LocalDate from, LocalDate to);
}
