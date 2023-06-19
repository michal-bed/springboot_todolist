package com.example.springboot_todolist.controller;

import com.example.springboot_todolist.config.CustomConfiguration;
import com.example.springboot_todolist.model.TodoItem;
import com.example.springboot_todolist.repository.TodoItemRepository;
import com.example.springboot_todolist.service.TodoItemService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@AutoConfigureMockMvc
@WebMvcTest(TodoController.class)
@Import(TodoController.class)
@ContextConfiguration(classes={CustomConfiguration.class})
public class TodoControllerTests {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TodoItemService todoService;

    @MockBean
    private TodoItemRepository todoRepository;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private ObjectMapper objectMapper;

    // needed to parse date to custom form | required for comparing values
    private static final DateTimeFormatter dateTimeFormatter =
            DateTimeFormatter.ofPattern( "yyyy-MM-dd'T'HH:mm:ss.SSSXXX" )
                    .withZone(ZoneId.of("Europe/Warsaw"));
    private static final UUID predefinedUUID = UUID
            .fromString("550e8400-e29b-41d4-a716-446655440000");

    private TodoItem createTodoItem(String value) {
        return TodoItem.builder()
                .todoItemId(UUID.randomUUID())
                .itemValue(value)
                .createdOn(ZonedDateTime.now())
                .lastEditOn(null)
                .deadline(null)
                .done(false)
                .checked(false)
                .edited(false)
                .wasEdited(false)
                .build();
    }

    private void verifyResult(TodoItem todoItem, ResultActions response, ResultMatcher status) throws Exception {
        // then - verify the result or output using assert statements
        response.andDo(print()).
                andExpect(status)
                .andExpect(jsonPath("$.todoItemId",
                        is(todoItem.getTodoItemId().toString())))
                .andExpect(jsonPath("$.itemValue",
                        is(todoItem.getItemValue())))
                .andExpect(jsonPath("$.createdOn",
                        is((todoItem.getCreatedOn().format(dateTimeFormatter)))))
                .andExpect(jsonPath("$.lastEditOn",
                        is(todoItem.getLastEditOn())))
                .andExpect(jsonPath("$.deadline",
                        is(todoItem.getDeadline())))
                .andExpect(jsonPath("$.done",
                        is(todoItem.getDone())))
                .andExpect(jsonPath("$.checked",
                        is(todoItem.getChecked())))
                .andExpect(jsonPath("$.edited",
                        is(todoItem.getEdited())))
                .andExpect(jsonPath("$.wasEdited",
                        is(todoItem.getWasEdited())));
    }

    @Test
    public void givenTodoItemObject_whenCreateTodoItem_thenReturnSavedTodoItem() throws Exception{

        // given - precondition or setup
        TodoItem todoItem = createTodoItem("test case");

        System.out.println("cratedOn" + todoItem.getCreatedOn());
        given(todoRepository.save(any(TodoItem.class)))
                .willAnswer((invocation)-> invocation.getArgument(0));

        // when - action or behaviour that we are going test
        ResultActions response = mockMvc.perform(post("/api/todo-item")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(todoItem)));

        // then - verify the result or output using assert statements
        verifyResult(todoItem, response, status().isCreated());
    }

    // JUnit test for Get All to do items REST API
    @Test
    public void givenListOfTodoItems_whenGetAllTodoItems_thenReturnTodoItemList() throws Exception{
        // given - precondition or setup
        List<TodoItem> todoItemList = new ArrayList<>();
        todoItemList.add(createTodoItem("test case 1"));
        todoItemList.add(createTodoItem("test case 2"));
        given(todoService.getAllTodoItems()).willReturn(todoItemList);

        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(get("/api/todo-item"));

        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()",
                        is(todoItemList.size())));
    }

    // JUnit test for Get All to do items REST API
    @Test
    public void givenNoTodoItems_whenGetAllTodoItems_thenReturnNoContentResponse() throws Exception{
        // given - precondition or setup
        given(todoService.getAllTodoItems()).willReturn(Collections.emptyList());

        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(get("/api/todo-item"));

        // then - verify the output
        response.andExpect(status().isNoContent())
                .andDo(print())
                .andExpect(jsonPath("$").doesNotExist());
    }

    // positive scenario - valid todoitem id
    // JUnit test for GET todoitem by id REST API
    @Test
    public void givenTodoItemId_whenGetTodoItemById_thenReturnTodoItemObject() throws Exception {
        // given - precondition or setup
        TodoItem todoItem = createTodoItem("test case");
        UUID todoItemId = todoItem.getTodoItemId();
        given(todoService.findTodoItemById(todoItemId)).willReturn(Optional.of(todoItem));

        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(get("/api/todo-item/{id}", todoItemId));

        // then - verify the output
        verifyResult(todoItem, response, status().isOk());

    }

    // negative scenario - invalid employee id
    // JUnit test for GET employee by id REST API
    @Test
    public void givenInvalidEmployeeId_whenGetEmployeeById_thenReturnEmpty() throws Exception{
        // given - precondition or setup
        TodoItem todoItem = createTodoItem("test case");
        UUID todoItemId = todoItem.getTodoItemId();
        given(todoService.findTodoItemById(todoItemId)).willReturn(Optional.empty());

        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", "invalid_id"));

        // then - verify the output
        response.andExpect(status().isNotFound())
                .andDo(print())
                .andExpect(jsonPath("$").doesNotExist());;
    }

    // JUnit test for update todoitem value REST API - positive scenario
    @Test
    public void givenValidUpdatedValueBody_whenUpdateTodoItemValue_thenReturnTodoItemWithUpdatedValue() throws Exception{

        // given - precondition or setup
        TodoItem todoItem = createTodoItem("test case");
        todoItem.setTodoItemId(predefinedUUID);
        TodoItem updatedTodoItem = objectMapper.convertValue(todoItem, TodoItem.class);
        String updatedValue = "updated test case";
        updatedTodoItem.setItemValue(updatedValue);

        given(todoRepository.findTodoItemByTodoItemId(predefinedUUID)).willReturn(Optional.of(todoItem));
        given(todoService.setTodoItemValue(predefinedUUID, updatedValue))
                .willReturn(Optional.of(updatedTodoItem));
        given(todoRepository.save(any(TodoItem.class)))
                .willAnswer((invocation)-> invocation.getArgument(0));

        // when -  action or the behaviour that we are going test
        String requestBody = String.format("{\"id\": \"%s\", \"value\": \"%s\"}",
                updatedTodoItem.getTodoItemId().toString(), updatedTodoItem.getItemValue());
        ResultActions response = mockMvc.perform(patch("/api/todo-item/value")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));
//        objectMapper.readTree(requestBody).asText()

        // then - verify the result or output using assert statements
        verifyResult(updatedTodoItem, response, status().isCreated());
    }

    // JUnit test for update todoitem value REST API - negative scenario #1 (request body was not valid
    // or incomplete)
    @Test
    public void givenIncompleteUpdatedValueBody_whenUpdateTodoItemValue_thenReturn409() throws Exception{

        // given - precondition or setup
        TodoItem todoItem = createTodoItem("test case");
        todoItem.setTodoItemId(predefinedUUID);
        TodoItem updatedTodoItem = objectMapper.convertValue(todoItem, TodoItem.class);
        String updatedValue = "updated test case";
        updatedTodoItem.setItemValue(updatedValue);

        given(todoRepository.findTodoItemByTodoItemId(predefinedUUID)).willReturn(Optional.empty());
        given(todoService.setTodoItemValue(predefinedUUID, updatedValue))
                .willReturn(Optional.of(updatedTodoItem));
        given(todoRepository.save(any(TodoItem.class)))
                .willAnswer((invocation)-> invocation.getArgument(0));

        // when -  action or the behaviour that we are going test
        String requestBody = String.format("{\"id\": \"%s\"}",
                updatedTodoItem.getTodoItemId().toString());
        ResultActions response = mockMvc.perform(patch("/api/todo-item/value")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));
//        objectMapper.readTree(requestBody).asText()

        // then - verify the result or output using assert statements
        response.andExpect(status().isConflict())
                .andDo(print())
                .andExpect(jsonPath("$").doesNotExist());
    }

    // JUnit test for update todoitem checked REST API - positive scenario
    @Test
    public void givenValidCheckedValueBody_whenUpdateTodoItemChecked_thenUpdateTodoItemChecked() throws Exception{

        // given - precondition or setup
        TodoItem todoItem = createTodoItem("test case");
        todoItem.setTodoItemId(predefinedUUID);
        TodoItem updatedTodoItem = objectMapper.convertValue(todoItem, TodoItem.class);
        updatedTodoItem.setChecked(!todoItem.getChecked());
        var updatedChecked = updatedTodoItem.getChecked();

        given(todoRepository.findTodoItemByTodoItemId(predefinedUUID)).willReturn(Optional.of(todoItem));
        willDoNothing().given(todoService).setTodoItemChecked(predefinedUUID, updatedChecked);
        given(todoRepository.save(any(TodoItem.class)))
                .willAnswer((invocation)-> invocation.getArgument(0));

        // when -  action or the behaviour that we are going test
        String requestBody = String.format("{\"id\": \"%s\", \"checked\": \"%s\"}",
                updatedTodoItem.getTodoItemId().toString(), updatedChecked);
        ResultActions response = mockMvc.perform(patch("/api/todo-item/is-checked")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));
//        objectMapper.readTree(requestBody).asText()

        // then - verify the result or output using assert statements
        response.andExpect(status().isCreated())
                .andDo(print())
                .andExpect(jsonPath("$").doesNotExist());
    }

    // JUnit test for update todoitem checked REST API - negative scenario #1 (request body was not valid
    // or incomplete)
    @Test
    public void givenIncompleteUpdatedCheckedBody_whenUpdateTodoItemChecked_thenReturn409() throws Exception{

        // given - precondition or setup
        TodoItem todoItem = createTodoItem("test case");
        todoItem.setTodoItemId(predefinedUUID);
        TodoItem updatedTodoItem = objectMapper.convertValue(todoItem, TodoItem.class);
        updatedTodoItem.setChecked(!todoItem.getChecked());
        var updatedChecked = updatedTodoItem.getChecked();

        given(todoRepository.findTodoItemByTodoItemId(predefinedUUID)).willReturn(Optional.of(todoItem));
        willDoNothing().given(todoService).setTodoItemChecked(predefinedUUID, updatedChecked);
        given(todoRepository.save(any(TodoItem.class)))
                .willAnswer((invocation)-> invocation.getArgument(0));

        // when -  action or the behaviour that we are going test
        String requestBody = String.format("{\"id\": \"%s\"}",
                updatedTodoItem.getTodoItemId().toString());
        ResultActions response = mockMvc.perform(patch("/api/todo-item/is-checked")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));
//        objectMapper.readTree(requestBody).asText()

        // then - verify the result or output using assert statements
        response.andExpect(status().isConflict())
                .andDo(print())
                .andExpect(jsonPath("$").doesNotExist());
    }

    // JUnit test for update todoitem edited REST API - positive scenario
    @Test
    public void givenValidEditedValueBody_whenUpdateTodoItemEdited_thenUpdateTodoItemEdited() throws Exception{

        // given - precondition or setup
        TodoItem todoItem = createTodoItem("test case");
        todoItem.setTodoItemId(predefinedUUID);
        TodoItem updatedTodoItem = objectMapper.convertValue(todoItem, TodoItem.class);
        updatedTodoItem.setEdited(!todoItem.getEdited());
        var updatedEdited = updatedTodoItem.getEdited();

        given(todoRepository.findTodoItemByTodoItemId(predefinedUUID)).willReturn(Optional.of(todoItem));
        willDoNothing().given(todoService).setTodoItemEdited(predefinedUUID, updatedEdited);
        given(todoRepository.save(any(TodoItem.class)))
                .willAnswer((invocation)-> invocation.getArgument(0));

        // when -  action or the behaviour that we are going test
        String requestBody = String.format("{\"id\": \"%s\", \"edited\": \"%s\"}",
                updatedTodoItem.getTodoItemId().toString(), updatedEdited);
        ResultActions response = mockMvc.perform(patch("/api/todo-item/is-edited")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));
//        objectMapper.readTree(requestBody).asText()

        // then - verify the result or output using assert statements
        response.andExpect(status().isCreated())
                .andDo(print())
                .andExpect(jsonPath("$").doesNotExist());
    }


    // JUnit test for update todoitem edited REST API - negative scenario #1 (request body was not valid
    // or incomplete)
    @Test
    public void givenIncompleteUpdatedEditedBody_whenUpdateTodoItemEdited_thenReturn409() throws Exception{

// given - precondition or setup
        TodoItem todoItem = createTodoItem("test case");
        todoItem.setTodoItemId(predefinedUUID);
        TodoItem updatedTodoItem = objectMapper.convertValue(todoItem, TodoItem.class);
        updatedTodoItem.setEdited(!todoItem.getEdited());
        var updatedEdited = updatedTodoItem.getEdited();

        given(todoRepository.findTodoItemByTodoItemId(predefinedUUID)).willReturn(Optional.of(todoItem));
        willDoNothing().given(todoService).setTodoItemEdited(predefinedUUID, updatedEdited);
        given(todoRepository.save(any(TodoItem.class)))
                .willAnswer((invocation)-> invocation.getArgument(0));

        // when -  action or the behaviour that we are going test
        String requestBody = String.format("{\"id\": \"%s\", \"checked\": \"%s\"}",
                updatedTodoItem.getTodoItemId().toString(), updatedEdited);
        ResultActions response = mockMvc.perform(patch("/api/todo-item/is-edited")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));
//        objectMapper.readTree(requestBody).asText()

        // then - verify the result or output using assert statements
        response.andExpect(status().isConflict())
                .andDo(print())
                .andExpect(jsonPath("$").doesNotExist());
    }

    // JUnit test for update todoitem done REST API - positive scenario
    @Test
    public void givenValidDoneValueBody_whenUpdateTodoItemDone_thenUpdateTodoItemDone() throws Exception{

        // given - precondition or setup
        TodoItem todoItem = createTodoItem("test case");
        todoItem.setTodoItemId(predefinedUUID);
        TodoItem updatedTodoItem = objectMapper.convertValue(todoItem, TodoItem.class);
        updatedTodoItem.setDone(!todoItem.getDone());
        var updatedDone = updatedTodoItem.getDone();

        given(todoRepository.findTodoItemByTodoItemId(predefinedUUID)).willReturn(Optional.of(todoItem));
        willDoNothing().given(todoService).setTodoItemDone(predefinedUUID, updatedDone);
        given(todoRepository.save(any(TodoItem.class)))
                .willAnswer((invocation)-> invocation.getArgument(0));

        // when -  action or the behaviour that we are going test
        String requestBody = String.format("{\"id\": \"%s\", \"done\": \"%s\"}",
                updatedTodoItem.getTodoItemId().toString(), updatedDone);
        ResultActions response = mockMvc.perform(patch("/api/todo-item/is-done")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));
//        objectMapper.readTree(requestBody).asText()

        // then - verify the result or output using assert statements
        response.andExpect(status().isCreated())
                .andDo(print())
                .andExpect(jsonPath("$").doesNotExist());
    }


    // JUnit test for update todoitem done REST API - negative scenario #1 (request body was not valid
    // or incomplete)
    @Test
    public void givenIncompleteUpdatedDoneBody_whenUpdateTodoItemDone_thenReturn409() throws Exception{

// given - precondition or setup
        TodoItem todoItem = createTodoItem("test case");
        todoItem.setTodoItemId(predefinedUUID);
        TodoItem updatedTodoItem = objectMapper.convertValue(todoItem, TodoItem.class);
        updatedTodoItem.setDone(!todoItem.getDone());
        var updatedDone = updatedTodoItem.getDone();

        given(todoRepository.findTodoItemByTodoItemId(predefinedUUID)).willReturn(Optional.of(todoItem));
        willDoNothing().given(todoService).setTodoItemDone(predefinedUUID, updatedDone);
        given(todoRepository.save(any(TodoItem.class)))
                .willAnswer((invocation)-> invocation.getArgument(0));

        // when -  action or the behaviour that we are going test
        String requestBody = String.format("{\"done\": \"%s\"}", updatedDone);
        ResultActions response = mockMvc.perform(patch("/api/todo-item/is-done")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));
//        objectMapper.readTree(requestBody).asText()

        // then - verify the result or output using assert statements
        response.andExpect(status().isConflict())
                .andDo(print())
                .andExpect(jsonPath("$").doesNotExist());
    }

    // JUnit test for update todoitem deadline REST API - positive scenario
    @Test
    public void givenValidDeadlineValueBody_whenUpdateTodoItemDeadline_thenUpdateTodoItemDeadline() throws Exception{

        // given - precondition or setup
        TodoItem todoItem = createTodoItem("test case");
        todoItem.setTodoItemId(predefinedUUID);
        TodoItem updatedTodoItem = objectMapper.convertValue(todoItem, TodoItem.class);
        updatedTodoItem.setDeadline(ZonedDateTime.now());
        var updatedDeadline = updatedTodoItem.getDeadline();

        given(todoRepository.findTodoItemByTodoItemId(predefinedUUID))
                .willReturn(Optional.of(todoItem));
        willDoNothing().given(todoService).setTodoItemDeadline(predefinedUUID, updatedDeadline);
        given(todoRepository.save(any(TodoItem.class)))
                .willAnswer((invocation)-> invocation.getArgument(0));

        // when -  action or the behaviour that we are going test
        String requestBody = String.format("{\"id\": \"%s\", \"deadline\": \"%s\"}",
                updatedTodoItem.getTodoItemId().toString(), updatedDeadline.format(dateTimeFormatter));
        ResultActions response = mockMvc.perform(patch("/api/todo-item/deadline")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));
//        objectMapper.readTree(requestBody).asText()

        // then - verify the result or output using assert statements
        response.andExpect(status().isCreated())
                .andDo(print())
                .andExpect(jsonPath("$").doesNotExist());
    }


    // JUnit test for update todoitem deadline REST API - negative scenario #1 (request body was not valid
    // or incomplete)
    @Test
    public void givenIncompleteUpdatedDeadlineBody_whenUpdateTodoItemDeadline_thenReturn409() throws Exception{

        // given - precondition or setup
        TodoItem todoItem = createTodoItem("test case");
        todoItem.setTodoItemId(predefinedUUID);
        TodoItem updatedTodoItem = objectMapper.convertValue(todoItem, TodoItem.class);
        updatedTodoItem.setDeadline(ZonedDateTime.now());
        var updatedDeadline = updatedTodoItem.getDeadline();

        given(todoRepository.findTodoItemByTodoItemId(predefinedUUID))
                .willReturn(Optional.of(todoItem));
        willDoNothing().given(todoService).setTodoItemDeadline(predefinedUUID, updatedDeadline);
        given(todoRepository.save(any(TodoItem.class)))
                .willAnswer((invocation)-> invocation.getArgument(0));

        // when -  action or the behaviour that we are going test
        String requestBody = "{}";
        ResultActions response = mockMvc.perform(patch("/api/todo-item/deadline")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));
         //        objectMapper.readTree(requestBody).asText()

        // then - verify the result or output using assert statements
        response.andExpect(status().isConflict())
                .andDo(print())
                .andExpect(jsonPath("$").doesNotExist());
    }

    // JUnit test for update todoitem deadline REST API - negative scenario #2 (request body was not valid
    // or incomplete)
    @Test
    public void givenInvalidUpdatedDeadlineBody_whenUpdateTodoItemDeadline_thenReturn409() throws Exception{

        // given - precondition or setup
        TodoItem todoItem = createTodoItem("test case");
        todoItem.setTodoItemId(predefinedUUID);
        TodoItem updatedTodoItem = objectMapper.convertValue(todoItem, TodoItem.class);
        updatedTodoItem.setDeadline(ZonedDateTime.now());
        var updatedDeadline = updatedTodoItem.getDeadline();

        given(todoRepository.findTodoItemByTodoItemId(predefinedUUID))
                .willReturn(Optional.of(todoItem));
        willDoNothing().given(todoService).setTodoItemDeadline(predefinedUUID, updatedDeadline);
        given(todoRepository.save(any(TodoItem.class)))
                .willAnswer((invocation)-> invocation.getArgument(0));

        // when -  action or the behaviour that we are going test
        String requestBody = "";
        ResultActions response = mockMvc.perform(patch("/api/todo-item/deadline")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));
        //        objectMapper.readTree(requestBody).asText()

        // then - verify the result or output using assert statements
        response.andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(jsonPath("$").doesNotExist());
    }

    // JUnit test for delete todoItem REST API - positive scenario
    @Test
    public void givenTodoItemIdThatExists_whenDeleteTodoItem_thenReturn200() throws Exception{
        // given - precondition or setup
        var todoItem = createTodoItem("removal case");
        var todoItemId = todoItem.getTodoItemId();

        // when -  action or the behaviour that we are going test
        given(todoService.findTodoItemById(todoItemId)).willReturn(Optional.of(todoItem));
        doNothing().when(todoService).removeTodoItem(todoItem);
        ResultActions response = mockMvc.perform(delete("/api/todo-item/{id}",
                todoItemId.toString()));

        // then - verify the output
        response.andExpect(status().isNoContent())
                .andDo(print())
                .andExpect(jsonPath("$").doesNotExist());
    }

    // JUnit test for delete todoItem REST API - negative scenario
    @Test
    public void givenTodoItemIdThatDoesNotExist_whenDeleteTodoItem_thenReturn404() throws Exception{
        // given - precondition or setup
        var todoItem = createTodoItem("removal case");
        willDoNothing().given(todoService).removeTodoItem(todoItem);
        var todoItemId = todoItem.getTodoItemId();

        // when -  action or the behaviour that we are going test
        given(todoService.findTodoItemById(predefinedUUID)).willReturn(Optional.empty());
        ResultActions response = mockMvc.perform(delete("/api/todo-item/{id}",
                todoItemId.toString()));

        // then - verify the output
        response.andExpect(status().isNotFound())
                .andDo(print())
                .andExpect(jsonPath("$").doesNotExist());
    }
}
