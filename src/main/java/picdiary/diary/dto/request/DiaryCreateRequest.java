package picdiary.diary.dto.request;

import lombok.Builder;
import lombok.Data;
import picdiary.diary.domain.Diary;

@Data
@Builder
public final class DiaryCreateRequest {
    private String content;
    private String date;
    private Diary.Emotion emotion;
    private String imageFileName;

}
