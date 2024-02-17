package picdiary.todo.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public final class ToDoCreateRequest {
    private String content;
    private String date;
}
