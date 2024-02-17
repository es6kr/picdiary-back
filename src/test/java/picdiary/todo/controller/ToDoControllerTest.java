package picdiary.todo.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import picdiary.diary.dto.request.DiaryCreateRequest;
import picdiary.diary.service.DiaryService;
import picdiary.global.dto.response.ApplicationResponse;
import picdiary.todo.dto.request.ToDoCreateRequest;
import picdiary.todo.repository.ToDoEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.MethodName.class)
public class ToDoControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private DiaryService diaryService;
    @Autowired
    private ObjectMapper objectMapper;

    final long userId = 1;
    private Long diaryId;
    private Long todoId;
    final ToDoCreateRequest toDo = new ToDoCreateRequest("Test content", "20240214");
    final HttpHeaders headers = new HttpHeaders();

    @BeforeAll
    public void setup() {
        headers.add(HttpHeaders.AUTHORIZATION, "Basic YWRtaW46MTIzNA==");
        var request = DiaryCreateRequest.builder().content("test todo").date(toDo.getDate()).build();
        diaryId = diaryService.createDiary(userId, request);
    }

    @AfterAll
    public void tearDown() {
        diaryService.deleteDiary(userId, diaryId);
    }

    @Test
    public void test1CreateTodo() throws Exception {

        headers.add(HttpHeaders.AUTHORIZATION, "Basic YWRtaW46MTIzNA==");
        var typeReference = new TypeReference<ApplicationResponse<Long>>() {
        };

        var payload = objectMapper.writeValueAsBytes(toDo);
        var requestBuilder = post("/todos").content(payload).contentType(MediaType.APPLICATION_JSON).headers(headers);
        var actions = mockMvc.perform(requestBuilder).andExpect(status().isOk()).andReturn();
        var bytes = actions.getResponse().getContentAsByteArray();
        todoId = objectMapper.readValue(bytes, typeReference).getData();
        assertNotNull(todoId, "투두리스트 작성");
    }

    @Test
    public void test2GetTodo() throws Exception {

        var date = toDo.getDate();
        var getTodoActions = mockMvc.perform(get( "/diaries/{date}/todos", date).headers(headers)).andExpect(status().isOk()).andReturn();
        var getTodoBytes = getTodoActions.getResponse().getContentAsByteArray();

        var typeReference = new TypeReference<ApplicationResponse<List<ToDoEntity>>>() {
        };
        var todoDetails = objectMapper.readValue(getTodoBytes, typeReference);
        var first = todoDetails.getData().stream().findFirst().orElseThrow();
        assertEquals(todoId, first.getId());
        assertEquals(toDo.getContent(), first.getContent());
    }

    @Test
    public void test3DeleteTodo() throws Exception {
        mockMvc.perform(delete("/todos/" + todoId).headers(headers)).andExpect(status().isOk()).andReturn();
    }
}
