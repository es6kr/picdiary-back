package picdiary.todo.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import picdiary.diary.repository.DiaryEntity;
import picdiary.diary.service.DiaryService;
import picdiary.global.dto.response.ApplicationResponse;
import picdiary.todo.dto.request.ToDoCreateRequest;
import picdiary.todo.dto.request.ToDoUpdateRequest;
import picdiary.todo.repository.ToDoEntity;
import picdiary.todo.service.ToDoService;
import picdiary.user.repository.UserEntity;

import java.util.List;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "basicAuth")
public class ToDoController {

    private final DiaryService diaryService;
    private final ToDoService toDoService;

    /**
     * 투두리스트 작성
     */
    @PostMapping("/todos")
    public ResponseEntity<ApplicationResponse<Long>> createToDo(UserEntity user, @RequestBody ToDoCreateRequest request) {
        Long toDoId = toDoService.createToDo(user.getId(), request);
        return ApplicationResponse.success(toDoId, "투두리스트가 생성되었습니다.");
    }

    /**
     * 투두리스트 정보 조회
     */
    @GetMapping("/diaries/{date}/todos")
    public ResponseEntity<ApplicationResponse<List<ToDoEntity>>> getToDoList(UserEntity user, @PathVariable("date") String date) {
        DiaryEntity diary = diaryService.getDiary(user.getId(), date);
        return ApplicationResponse.data(diary.getToDoList());
    }

    /**
     * 투두리스트 수정
     */
    @PatchMapping("/todos/{toDoId}")
    public ResponseEntity<ApplicationResponse<ToDoEntity>> updateToDo(UserEntity user, @PathVariable("toDoId") Long toDoId, @RequestBody ToDoUpdateRequest request) {
        ToDoEntity toDo = toDoService.updateToDo(user.getId(), toDoId, request);
        return ApplicationResponse.data(toDo, "투두리스트가 수정되었습니다.", null);
    }

    /**
     * 투두리스트 삭제
     */
    @DeleteMapping("/todos/{toDoId}")
    public ResponseEntity<ApplicationResponse<Long>> deleteToDo(UserEntity user, @PathVariable("toDoId") Long toDoId) {
        toDoService.deleteToDo(user.getId(), toDoId);
        return ApplicationResponse.success(toDoId, "투두리스트가 삭제되었습니다.");
    }
}
