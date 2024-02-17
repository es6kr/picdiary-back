package picdiary.diary.repository;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import picdiary.diary.exception.DiaryErrorCode;
import picdiary.global.exception.ApplicationException;
import picdiary.global.repository.BaseEntity;
import picdiary.user.repository.UserEntity;

import java.time.LocalDate;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.JOINED)
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
    private String imageFileName;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user; // 일기 작성자

    public DiaryEntity(String content, LocalDate date, String imageFileName, UserEntity user) {
        this.content = content;
        this.date = date;
        this.imageFileName = imageFileName;
        this.user = user;
    }

    public void diaryUpdate(long userId, String content) {
        validateUserIsWriter(userId);
        this.content = content;
    }

    public void validateUserIsWriter(long userId) {
        if (!this.user.isWriter(userId)) {
            throw new ApplicationException(DiaryErrorCode.IS_NOT_WRITER);
        }
    }

}
