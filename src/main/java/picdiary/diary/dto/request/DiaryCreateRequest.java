package picdiary.diary.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public final class DiaryCreateRequest {
    private  String content;
    private  String date;
    private  String imageFileName;
}
