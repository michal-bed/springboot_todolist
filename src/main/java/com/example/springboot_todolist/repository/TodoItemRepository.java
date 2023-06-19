package com.example.springboot_todolist.repository;

import com.example.springboot_todolist.model.TodoItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TodoItemRepository extends JpaRepository<TodoItem, UUID> {
    Optional<TodoItem> findTodoItemByTodoItemId(UUID todoItemId);
    List<TodoItem> getAllBy();
    List<TodoItem> findTodoItemsByItemValue(String itemValue);
    List<TodoItem> findTodoItemsByItemValueIgnoreCase(String itemValue);
    List<TodoItem> findTodoItemByItemValueContaining(String itemValue);
    List<TodoItem> findTodoItemByItemValueContainingIgnoreCase(String itemValue);
    List<TodoItem> findTodoItemsByDeadline(ZonedDateTime deadline);
    List<TodoItem> findTodoItemByDeadlineAfter(ZonedDateTime after);
    List<TodoItem> findTodoItemByDeadlineBefore(ZonedDateTime before);
    List<TodoItem> findTodoItemByDeadlineBetween(ZonedDateTime since, ZonedDateTime till);
    List<TodoItem> findTodoItemsByCreatedOn(ZonedDateTime createdOn);
    List<TodoItem> findTodoItemsByCreatedOnAfter(ZonedDateTime after);
    List<TodoItem> findTodoItemsByCreatedOnBefore(ZonedDateTime before);
    List<TodoItem> findTodoItemsByCreatedOnBetween(ZonedDateTime since, ZonedDateTime till);
    List<TodoItem> findTodoItemsByLastEditOn(ZonedDateTime createdOn);
    List<TodoItem> findTodoItemsByLastEditOnAfter(ZonedDateTime after);
    List<TodoItem> findTodoItemsByLastEditOnBefore(ZonedDateTime before);
    List<TodoItem> findTodoItemsByLastEditOnBetween(ZonedDateTime since, ZonedDateTime till);
    List<TodoItem> findTodoItemsByDoneIsTrue();
    List<TodoItem> findTodoItemsByDoneIsFalse();
    List<TodoItem> findTodoItemsByCheckedIsTrue();
    List<TodoItem> findTodoItemsByCheckedIsFalse();
    List<TodoItem> findTodoItemsByEditedIsTrue();
    List<TodoItem> findTodoItemsByEditedIsFalse();
    List<TodoItem> findTodoItemsByWasEditedIsTrue();
    List<TodoItem> findTodoItemsByWasEditedIsFalse();
    @Query("SELECT i from TodoItem i WHERE day(i.deadline) = ?1 AND month(i.deadline) = ?2")
    List<TodoItem> findTodoItemsByDeadlineDate(int dayOfMonth, int month);
    @Query("SELECT i from TodoItem i WHERE day(i.createdOn) = ?1 AND month(i.createdOn) = ?2")
    List<TodoItem> findTodoItemsByCreatedOnDate(int dayOfMonth, int month);
    @Query("SELECT i from TodoItem i WHERE day(i.lastEditOn) = ?1 AND month(i.lastEditOn) = ?2")
    List<TodoItem> findTodoItemsByLastEditOnDate(int dayOfMonth, int month);
    @Query("SELECT i from TodoItem i WHERE month(i.deadline) = ?1")
    List<TodoItem> findTodoItemsByDeadlineMonth(int month);
    @Query("SELECT i from TodoItem i WHERE month(i.createdOn) = ?1")
    List<TodoItem> findTodoItemsByCreatedOnMonth(int month);
    @Query("SELECT i from TodoItem i WHERE year(i.lastEditOn) = ?1")
    List<TodoItem> findTodoItemsByLastEditOnMonth(int month);
    @Query("SELECT i from TodoItem i WHERE year(i.deadline) = ?1")
    List<TodoItem> findTodoItemsByDeadlineYear(int year);
    @Query("SELECT i from TodoItem i WHERE year(i.createdOn) = ?1")
    List<TodoItem> findTodoItemsByCreatedOnYear(int year);
    @Query("SELECT i from TodoItem i WHERE year(i.lastEditOn) = ?1")
    List<TodoItem> findTodoItemsByLastEditOnYear(int year);
}
