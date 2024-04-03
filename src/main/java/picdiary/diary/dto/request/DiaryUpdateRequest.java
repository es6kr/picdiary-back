package picdiary.diary.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import picdiary.diary.domain.Diary;

@Data
@NoArgsConstructor
public class DiaryUpdateRequest {
    private String content;
    protected Diary.Emotion emotion;
    private MultipartFile imageFile;
    private String imageFileName;

    public DiaryUpdateRequest(String content, Diary.Emotion emotion) {
        this.content = content;
        this.emotion = emotion;
    }
}
