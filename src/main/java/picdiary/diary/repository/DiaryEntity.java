package picdiary.diary.repository;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import picdiary.diary.domain.Diary;
import picdiary.diary.dto.request.DiaryUpdateRequest;
import picdiary.diary.exception.DiaryErrorCode;
import picdiary.global.exception.ApplicationException;
import picdiary.global.repository.BaseEntity;
import picdiary.todo.repository.ToDoEntity;
import picdiary.user.repository.UserEntity;

import java.time.LocalDate;
import java.util.List;

@Getter
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "diary", uniqueConstraints = {@UniqueConstraint(columnNames = {"date", "user_id"})})
public class DiaryEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 다이어리 아이디

    @Column
    private String content; // 일기 내용

    @Column
    private LocalDate date; // 캘린더 설정 날짜
    @Column
    @Enumerated(EnumType.STRING)
    private Diary.Emotion emotion; // 감정
    @Column
    private String imageFileName;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user; // 일기 작성자

    @OneToMany(mappedBy = "diary")
    private List<ToDoEntity> toDoList;

    public DiaryEntity(String content, LocalDate date, Diary.Emotion emotion, String imageFileName, UserEntity user) {
        this.content = content;
        this.date = date;
        this.emotion = emotion;
        this.imageFileName = imageFileName;
        this.user = user;
    }

    public void diaryUpdate(long userId, DiaryUpdateRequest request) {
        validateUserIsWriter(userId);
        if (request.getContent() != null) {
            this.content = request.getContent();
        }
        if (request.getEmotion() != null) {
            this.emotion = request.getEmotion();
        }
        if (request.getImageFileName() != null) {
            this.imageFileName = request.getImageFileName();
        }
    }

    public void validateUserIsWriter(long userId) {
        if (!this.user.isWriter(userId)) {
            throw new ApplicationException(DiaryErrorCode.IS_NOT_WRITER);
        }
    }

}
