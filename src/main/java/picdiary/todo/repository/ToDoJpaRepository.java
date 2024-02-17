package picdiary.todo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import picdiary.diary.repository.DiaryEntity;
import java.util.List;

public interface ToDoJpaRepository extends JpaRepository<ToDoEntity, Long> {
    List<ToDoEntity> findByDiary(DiaryEntity diary);
}
