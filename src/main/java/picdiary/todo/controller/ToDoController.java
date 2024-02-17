package picdiary.todo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import picdiary.diary.repository.DiaryEntity;
import picdiary.diary.service.DiaryService;
import picdiary.global.domain.CustomUserDetails;
import picdiary.global.dto.response.ApplicationResponse;
import picdiary.todo.dto.request.ToDoCreateRequest;
import picdiary.todo.dto.request.ToDoUpdateRequest;
import picdiary.todo.repository.ToDoEntity;
import picdiary.todo.service.ToDoService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ToDoController {

    private final DiaryService diaryService;
    private final ToDoService toDoService;

    /**
     * 투두리스트 작성
     */
    @PostMapping("/todos")
    public ResponseEntity<ApplicationResponse<Long>> createToDo(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody ToDoCreateRequest request) {
        Assert.notNull(userDetails, "로그인이 필요합니다.");
        Long toDoId = toDoService.createToDo(userDetails.userEntity().getId(), request);
        return ApplicationResponse.success(toDoId, "투두리스트가 생성되었습니다.");
    }

    /**
     * 투두리스트 정보 조회
     */
    @GetMapping("/diaries/{date}/todos")
    public ResponseEntity<ApplicationResponse<List<ToDoEntity>>> getToDoList(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable("date") String date) {
        Assert.notNull(userDetails, "로그인이 필요합니다.");
        DiaryEntity diary = diaryService.getDiary(userDetails.userEntity().getId(), date);
        return ApplicationResponse.data(diary.getToDoList());
    }

   /**
    * 투두리스트 수정
    */
   @PatchMapping("/todos/{toDoId}")
   public ResponseEntity<ApplicationResponse<ToDoEntity>> updateToDo(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable("toDoId") Long toDoId, @RequestBody ToDoUpdateRequest request) {
       ToDoEntity toDo = toDoService.updateToDo(userDetails.userEntity().getId(), toDoId, request);
       return ApplicationResponse.data(toDo, "투두리스트가 수정되었습니다.", null);
   }

    /**
     * 투두리스트 삭제
     */
    @DeleteMapping("/todos/{toDoId}")
    public ResponseEntity<ApplicationResponse<Long>> deleteToDo(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable("toDoId") Long toDoId) {
        toDoService.deleteToDo(userDetails.userEntity().getId(), toDoId);
        return ApplicationResponse.success(toDoId, "투두리스트가 삭제되었습니다.");
    }
}
