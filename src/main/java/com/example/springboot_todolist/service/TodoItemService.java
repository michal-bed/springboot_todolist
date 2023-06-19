package com.example.springboot_todolist.service;

import com.example.springboot_todolist.model.TodoItem;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.ZonedDateTime;
import java.util.*;

public interface TodoItemService {
    void addTodoItem(TodoItem item);
    void removeTodoItem(TodoItem item);
    Optional<TodoItem> setTodoItemValue(UUID itemId, String value);
    void setTodoItemDeadline(UUID itemId, ZonedDateTime deadline);
    void setTodoItemDone(UUID itemId, boolean isDone);
    void setTodoItemEdited(UUID itemId, boolean isEdited);
    void setTodoItemChecked(UUID itemId, boolean Checked);
    Optional<TodoItem> findTodoItemById(UUID itemId);
    List<TodoItem> getAllTodoItems();
    List<TodoItem> findTodoItemsByValue(String value);
    List<TodoItem> findTodoItemsByDeadline(ZonedDateTime deadline);
    List<TodoItem> findTodoItemsByDeadlineDate(LocalDate deadline);
    List<TodoItem> findTodoItemsByDeadlineMonth(Month deadline);
    List<TodoItem> findTodoItemsByDeadlineYear(Year deadline);
    List<TodoItem> findTodoItemsByCreatedOn(ZonedDateTime createOn);
    List<TodoItem> findTodoItemsByCreatedOnDate(LocalDate createdOn);
    List<TodoItem> findTodoItemsByCreatedOnMonth(Month createdOn);
    List<TodoItem> findTodoItemsByCreatedOnYear(Year createdOn);
    List<TodoItem> findTodoItemsByLastEditOn(ZonedDateTime lastEditOn);
    List<TodoItem> findTodoItemsByLastEditOnDate(LocalDate lastEditOn);
    List<TodoItem> findTodoItemsByLastEditOnMonth(Month lastEditOn);
    List<TodoItem> findTodoItemsByLastEditOnYear(Year lastEditOn);
    List<TodoItem> findDoneTodoItems();
    List<TodoItem> findNotDoneTodoItems();
    List<TodoItem> findCheckedTodoItems();
    List<TodoItem> findNotCheckedTodoItems();
    List<TodoItem> findWereEditedTodoItems();
    List<TodoItem> findWereNotEditedTodoItems();
}
