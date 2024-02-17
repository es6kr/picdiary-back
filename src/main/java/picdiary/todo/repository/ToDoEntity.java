package picdiary.todo.repository;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import picdiary.diary.repository.DiaryEntity;
import picdiary.global.exception.ApplicationException;
import picdiary.global.repository.BaseEntity;
import picdiary.todo.exception.TodoErrorCode;

@Getter
@Entity
@Table(name = "todo")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.JOINED)
public class ToDoEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String content;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "diary_id")
    private DiaryEntity diary;

    public ToDoEntity(String content, DiaryEntity diary) {
        this.content = content;
        this.diary = diary;
    }

    public void update(long userId, String content) {
        validateUserIsWriter(userId);
        this.content = content;
    }

    public void validateUserIsWriter(long userId) {
        if (!this.diary.getUser().isWriter(userId)) {
            throw new ApplicationException(TodoErrorCode.IS_NOT_WRITER);
        }
    }

}
