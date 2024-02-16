package picdiary.diary.repository;

import picdiary.diary.exception.TodoFolderErrorCode;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import picdiary.global.exception.ApplicationException;
import picdiary.global.repository.BaseEntity;
import picdiary.user.repository.UserEntity;

import java.time.LocalDateTime;
@Getter
@Entity
@Table(name = "diary")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class DiaryEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 다이어리 아이디

    @Column
    private String content; // 일기 내용

    @Column
    private LocalDateTime date; // 캘린더 설정 날짜

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user; // 일기 작성자

    public DiaryEntity(String content, LocalDateTime date, UserEntity user) {
        this.content = content;
        this.date = date;
        this.user = user;
    }

    public void diaryUpdate(UserEntity user, String content) {
        validateUserIsWriter(user);
        this.content = content;
    }

    public void validateUserIsWriter(UserEntity user) {
        if (!this.user.isWriter(user)) {
            throw new ApplicationException(TodoFolderErrorCode.IS_NOT_WRITER);
        }
    }

}
