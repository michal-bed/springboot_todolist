package com.example.springboot_todolist.service;

import com.example.springboot_todolist.model.TodoItem;
import com.example.springboot_todolist.repository.TodoItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TodoItemServiceImplementation implements TodoItemService {

    private final TodoItemRepository todoItemRepository;

    @Autowired
    public TodoItemServiceImplementation(TodoItemRepository todoItemRepository) {
        this.todoItemRepository = todoItemRepository;
    }

    @Override
    public void addTodoItem(TodoItem item) {
        todoItemRepository.save(item);
    }

    @Override
    public void removeTodoItem(TodoItem item) {
        todoItemRepository.delete(item);
    }

    @Override
    public Optional<TodoItem> setTodoItemValue(UUID itemId, String value) {
        var todoItem = todoItemRepository.findTodoItemByTodoItemId(itemId);
        if (todoItem.isPresent())
        {
            var item = todoItem.get();
            if(!item.getItemValue().equals(value)) {
                item.setItemValue(value);
                handleEditValues(item);
                todoItemRepository.save(item);
            }
            return todoItem;
        }

        return Optional.empty();
    }


    private void handleEditValues(TodoItem item) {
        if (!item.getWasEdited()) {
            item.setWasEdited(true);
        }
        item.setLastEditOn(ZonedDateTime.now());
    }

    @Override
    public void setTodoItemDeadline(UUID itemId, ZonedDateTime deadline) {
        var todoItem = todoItemRepository.findTodoItemByTodoItemId(itemId);
        if (todoItem.isPresent())
        {
            var item = todoItem.get();
            item.setDeadline(deadline);
            handleEditValues(item);
            todoItemRepository.save(item);
        }
    }

    @Override
    public void setTodoItemDone(UUID itemId, boolean isDone) {
        var todoItem = todoItemRepository.findTodoItemByTodoItemId(itemId);
        if (todoItem.isPresent())
        {
            var item = todoItem.get();
            item.setDone(isDone);
            handleEditValues(item);
            todoItemRepository.save(item);
        }
    }

    @Override
    public void setTodoItemEdited(UUID itemId, boolean isEdited) {
        var todoItem = todoItemRepository.findTodoItemByTodoItemId(itemId);
        if (todoItem.isPresent())
        {
            var item = todoItem.get();
            item.setEdited(isEdited);
//            handleEditValues(item);
            todoItemRepository.save(item);
        }
    }

    @Override
    public void setTodoItemChecked(UUID itemId, boolean isChecked) {
        var todoItem = todoItemRepository.findTodoItemByTodoItemId(itemId);
        if (todoItem.isPresent())
        {
            var item = todoItem.get();
            item.setChecked(isChecked);
//            handleEditValues(item);
            todoItemRepository.save(item);
        }
    }


    @Override
    public Optional<TodoItem> findTodoItemById(UUID itemId) {
        return todoItemRepository.findTodoItemByTodoItemId(itemId);
    }

    @Override
    public List<TodoItem> getAllTodoItems() {
        return todoItemRepository.getAllBy();
    }

    @Override
    public List<TodoItem> findTodoItemsByValue(String value) {
        return todoItemRepository.findTodoItemsByItemValue(value);
    }

    @Override
    public List<TodoItem> findTodoItemsByDeadline(ZonedDateTime deadline) {
        return todoItemRepository.findTodoItemsByDeadline(deadline);
    }

    @Override
    public List<TodoItem> findTodoItemsByDeadlineDate(LocalDate deadline) {
        return todoItemRepository.findTodoItemsByDeadlineDate(deadline.getDayOfMonth(), deadline.getMonth().getValue());
    }

    @Override
    public List<TodoItem> findTodoItemsByDeadlineMonth(Month deadline) {
        return todoItemRepository.findTodoItemsByDeadlineMonth(deadline.getValue());
    }

    @Override
    public List<TodoItem> findTodoItemsByDeadlineYear(Year deadline) {
        return todoItemRepository.findTodoItemsByDeadlineYear(deadline.getValue());
    }

    @Override
    public List<TodoItem> findTodoItemsByCreatedOn(ZonedDateTime createdOn) {
        return todoItemRepository.findTodoItemsByCreatedOn(createdOn);
    }

    @Override
    public List<TodoItem> findTodoItemsByCreatedOnDate(LocalDate createdOn) {
        return todoItemRepository
                .findTodoItemsByCreatedOnDate(createdOn.getDayOfMonth(),
                        createdOn.getMonth().getValue());

    }

    @Override
    public List<TodoItem> findTodoItemsByCreatedOnMonth(Month createdOn) {
        return todoItemRepository.findTodoItemsByCreatedOnMonth(createdOn.getValue());
    }

    @Override
    public List<TodoItem> findTodoItemsByCreatedOnYear(Year createdOn) {
        return todoItemRepository.findTodoItemsByCreatedOnMonth(createdOn.getValue());
    }

    @Override
    public List<TodoItem> findTodoItemsByLastEditOn(ZonedDateTime lastEditOn) {
        return todoItemRepository.findTodoItemsByLastEditOn(lastEditOn);
    }

    @Override
    public List<TodoItem> findTodoItemsByLastEditOnDate(LocalDate lastEditOn) {
        return todoItemRepository
                .findTodoItemsByLastEditOnDate(lastEditOn.getDayOfMonth(),
                        lastEditOn.getMonth().getValue());
    }

    @Override
    public List<TodoItem> findTodoItemsByLastEditOnMonth(Month lastEditOn) {
        return todoItemRepository.findTodoItemsByLastEditOnMonth(lastEditOn.getValue());
    }

    @Override
    public List<TodoItem> findTodoItemsByLastEditOnYear(Year lastEditOn) {
        return todoItemRepository.findTodoItemsByLastEditOnYear(lastEditOn.getValue());
    }

    @Override
    public List<TodoItem> findDoneTodoItems() {
        return todoItemRepository.findTodoItemsByDoneIsTrue();
    }

    @Override
    public List<TodoItem> findNotDoneTodoItems() {
        return todoItemRepository.findTodoItemsByDoneIsFalse();
    }

    @Override
    public List<TodoItem> findCheckedTodoItems() {
        return todoItemRepository.findTodoItemsByCheckedIsTrue();
    }

    @Override
    public List<TodoItem> findNotCheckedTodoItems() {
        return todoItemRepository.findTodoItemsByCheckedIsFalse();
    }

    @Override
    public List<TodoItem> findWereEditedTodoItems() {
        return todoItemRepository.findTodoItemsByWasEditedIsTrue();
    }

    @Override
    public List<TodoItem> findWereNotEditedTodoItems() {
        return todoItemRepository.findTodoItemsByWasEditedIsFalse();
    }
}
