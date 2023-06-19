package com.example.springboot_todolist.controller;

import com.example.springboot_todolist.model.TodoItem;
import com.example.springboot_todolist.repository.TodoItemRepository;
import com.example.springboot_todolist.service.TodoItemService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin(origins = { "http://localhost:3000", "http://localhost:3001" })
public class TodoController {

    private final TodoItemService todoItemService;
    private final TodoItemRepository todoItemRepository;

    @Autowired
    public TodoController(TodoItemService todoItemService, TodoItemRepository todoItemRepository) {
        this.todoItemService = todoItemService;
        this.todoItemRepository = todoItemRepository;
    }

    @GetMapping(value = "/api/todo-item")
    private ResponseEntity<List<TodoItem>> getAllTodoItems() {
        var allItems = todoItemService.getAllTodoItems();
        if (allItems != null && !allItems.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(allItems);
        }
        else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }

    @GetMapping(value = "/api/todo-item/{id}")
    private ResponseEntity<TodoItem> getTodoItemById(@PathVariable String id) {
        var foundItem = todoItemService.findTodoItemById(UUID.fromString(id));
        if (foundItem.isPresent())
        {
            return ResponseEntity.status(HttpStatus.OK).body(foundItem.get());
        }
        else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }

    @GetMapping(value = "/api/todo-item/done")
    private ResponseEntity<List<TodoItem>> getAllDoneTodoItems() {
        var doneItems = todoItemService.findDoneTodoItems();
        if (!doneItems.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(doneItems);
        }
        else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }

    @GetMapping(value = "/api/todo-item/not-done")
    private ResponseEntity<List<TodoItem>> getAllNotDoneTodoItems() {
        var notDoneItems = todoItemService.findNotDoneTodoItems();
        if (!notDoneItems.isEmpty())
        {
            return ResponseEntity.status(HttpStatus.OK).body(notDoneItems);
        }
        else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }

    @PostMapping(value = "/api/todo-item/specified-deadline")
    private ResponseEntity<List<TodoItem>> getTodoItemsByDeadline(@RequestBody JsonNode body) {
        if (!body.has("deadline"))
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        var value = body.get("deadline").textValue();
        var deadline = ZonedDateTime.parse(value);
        var itemsByDeadline = todoItemService.findTodoItemsByDeadline(deadline);
        if (!itemsByDeadline.isEmpty())
        {
            return ResponseEntity.status(HttpStatus.OK).body(itemsByDeadline);
        }
        else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }

    @PostMapping(value = "/api/todo-item")
    private ResponseEntity<?> addTodoItem(@RequestBody TodoItem todoItem) {
        todoItemService.addTodoItem(todoItem);
        return ResponseEntity.status(HttpStatus.CREATED).body(todoItem);
    }

    @PatchMapping(value = "/api/todo-item/value")
    private ResponseEntity<?> setTodoItemValue(@RequestBody JsonNode body) {
        if (body.has("id") && body.has("value")) {
            var itemId = body.get("id").asText();
            var value = body.get("value").asText();
            var updatedTodoItem = todoItemService.setTodoItemValue(UUID.fromString(itemId), value);
            return ResponseEntity.status(HttpStatus.CREATED).body(updatedTodoItem);
        }
        else if (body.has("id"))
        {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        return ResponseEntity.status(HttpStatus.CONFLICT).build();
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PatchMapping(value = "/api/todo-item/deadline")
    private ResponseEntity<?> setTodoItemDeadline(@RequestBody JsonNode body) {
        if (body.has("id") && body.has("deadline")) {
            var itemId = body.get("id").asText();
            var value = body.get("deadline").asText();
            var deadline = ZonedDateTime.parse(value);
            todoItemService.setTodoItemDeadline(UUID.fromString(itemId), deadline);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        else if (body.has("id"))
        {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        return ResponseEntity.status(HttpStatus.CONFLICT).build();
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PatchMapping(value = "/api/todo-item/is-done")
    private ResponseEntity<?> setTodoItemIsDone(@RequestBody JsonNode body) {
        if (body.has("id") && body.has("done")) {
            var itemId = body.get("id").asText();
            var value = body.get("done").asText();
            var done = Boolean.parseBoolean(value);
            todoItemService.setTodoItemDone(UUID.fromString(itemId), done);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @PatchMapping(value = "/api/todo-item/is-edited")
    private ResponseEntity<?> setTodoItemIsEdited(@RequestBody JsonNode body) {
        if (body.has("id") && body.has("edited")) {
            var itemId = body.get("id").asText();
            var value = body.get("edited").asText();
            var edited = Boolean.parseBoolean(value);
            todoItemService.setTodoItemEdited(UUID.fromString(itemId), edited);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @PatchMapping(value = "/api/todo-item/is-checked")
    private ResponseEntity<?> setTodoItemIsChecked(@RequestBody JsonNode body) {
        if (body.has("id") && body.has("checked")) {
            var itemId = body.get("id").asText();
            var value = body.get("checked").asText();
            var checked = Boolean.parseBoolean(value);
            todoItemService.setTodoItemChecked(UUID.fromString(itemId), checked);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @DeleteMapping(value = "/api/todo-item/{id}")
    private ResponseEntity<TodoItem> removeTodoItemById(@PathVariable String id)
    {
        var foundItem = todoItemService.findTodoItemById(UUID.fromString(id));
        if (foundItem.isPresent()) {
            todoItemService.removeTodoItem(foundItem.get());
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PatchMapping(value = "/api/todo-item/{id}/value")
    private ResponseEntity<TodoItem> setTodoItemValue(@PathVariable String id, @RequestBody JsonNode body)
    {
        var foundItem = todoItemService.findTodoItemById(UUID.fromString(id));
        if (foundItem.isPresent()) {
            var item = foundItem.get();
            if (body.has("value")) {
                var value = body.get("value").textValue();
                item.setItemValue(value);
                todoItemRepository.save(item);
            }
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PatchMapping(value = "/api/todo-item/{id}/deadline")
    private ResponseEntity<TodoItem> setTodoItemDeadline(@PathVariable String id, @RequestBody JsonNode body)
    {
        var foundItem = todoItemService.findTodoItemById(UUID.fromString(id));
        if (foundItem.isPresent()) {
            var item = foundItem.get();
            if (body.has("deadline")) {
                var deadline = body.get("deadline").textValue();
                item.setDeadline(ZonedDateTime.parse(deadline));
                todoItemRepository.save(item);
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PatchMapping(value = "/api/todo-item/{id}/done")
    private ResponseEntity<TodoItem> setTodoItemIsDone(@PathVariable String id, @RequestBody JsonNode body)
    {
        var foundItem = todoItemService.findTodoItemById(UUID.fromString(id));
        if (foundItem.isPresent()) {
            var item = foundItem.get();
            if (body.has("done")) {
                var done = body.get("done").textValue();
                item.setDone(Boolean.parseBoolean(done));
                todoItemRepository.save(item);
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PatchMapping(value = "/api/todo-item/{id}/edited")
    private ResponseEntity<TodoItem> setTodoItemIsEdited(@PathVariable String id, @RequestBody JsonNode body)
    {
        var foundItem = todoItemService.findTodoItemById(UUID.fromString(id));
        if (foundItem.isPresent()) {
            var item = foundItem.get();
            if (body.has("edited")) {
                var edited = body.get("edited").textValue();
                item.setEdited(Boolean.parseBoolean(edited));
                todoItemRepository.save(item);
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PatchMapping(value = "/api/todo-item/{id}/checked")
    private ResponseEntity<TodoItem> setTodoItemIsChecked(@PathVariable String id, @RequestBody JsonNode body)
    {
        var foundItem = todoItemService.findTodoItemById(UUID.fromString(id));
        if (foundItem.isPresent()) {
            var item = foundItem.get();
            if (body.has("checked")) {
                var checked = body.get("checked").textValue();
                item.setChecked(Boolean.parseBoolean(checked));
                todoItemRepository.save(item);
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
