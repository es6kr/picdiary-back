package picdiary.user.repository;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import picdiary.global.repository.BaseEntity;

@Data
@Entity
@EqualsAndHashCode(callSuper=false)
@Table(name = "user", uniqueConstraints = {@UniqueConstraint(columnNames = {"email"})})
public class UserEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 사용자 아이디

    @Column
    private String email;

    @Column
    private String password; // 사용자 암호

    public boolean isWriter(long userId) {
        return id.equals(userId);
    }

}
