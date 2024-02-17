package picdiary.todo.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import picdiary.diary.repository.DiaryEntity;
import picdiary.diary.service.DiaryService;
import picdiary.global.exception.ApplicationException;
import picdiary.todo.dto.request.ToDoCreateRequest;
import picdiary.todo.dto.request.ToDoUpdateRequest;
import picdiary.todo.exception.TodoErrorCode;
import picdiary.todo.repository.ToDoEntity;
import picdiary.todo.repository.ToDoJpaRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class ToDoService {
    private final DiaryService diaryService;
    private final ToDoJpaRepository toDoRepository;

    public long createToDo(long userId, ToDoCreateRequest request) {
        DiaryEntity diary = diaryService.getDiary(userId, request.getDate());
        ToDoEntity todoEntity = new ToDoEntity(request.getContent(), diary);
        return toDoRepository.save(todoEntity).getId();
    }

    public void deleteToDo(long userId, long toDoId) {
        ToDoEntity todo = findToDoById(toDoId);
        todo.getDiary().validateUserIsWriter(userId);
        toDoRepository.deleteById(toDoId);
    }

    public ToDoEntity findToDoById(long diaryId) {
        return toDoRepository.findById(diaryId).orElseThrow(() -> new ApplicationException(TodoErrorCode.NOT_FOUND_TODO));
    }

    public ToDoEntity updateToDo(long userId, long toDoId, ToDoUpdateRequest request) {
        ToDoEntity toDo = findToDoById(toDoId);
        toDo.update(userId, request.content());
        return toDoRepository.save(toDo);
    }
}
