package picdiary.diary.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import picdiary.diary.domain.Diary;

@Data
@Builder
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public final class DiaryCreateRequest extends DiaryUpdateRequest {
    private String content;
    private String date;
    private Diary.Emotion emotion;
}
