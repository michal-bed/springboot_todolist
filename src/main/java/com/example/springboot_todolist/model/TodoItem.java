package com.example.springboot_todolist.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder(toBuilder=true)
@Table(name = "todo_tasks")
public class TodoItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID todoItemId;
    @Column(name = "item_value", columnDefinition="text", nullable = false)
    private String itemValue;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone="GMT+2")
    @Column(name = "crated_on", nullable = false)
    private ZonedDateTime createdOn;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone="GMT+2")
    @Column(name = "last_edit_on")
    private ZonedDateTime lastEditOn = null;
//  another possible pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ" didn't work as well as this one
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone="GMT+2")
    @Column(name = "deadline")
    private ZonedDateTime deadline = null;
    @Column(name = "done")
    private Boolean done = false;
    @Column(name = "checked")
    private Boolean checked = false;
    @Column(name = "edited")
    private Boolean edited = false;
    @Column(name = "was_edited")
    private Boolean wasEdited = false;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TodoItem todoItem = (TodoItem) o;
        return Objects.equals(todoItemId, todoItem.todoItemId) && Objects.equals(itemValue, todoItem.itemValue) && Objects.equals(createdOn, todoItem.createdOn) && Objects.equals(lastEditOn, todoItem.lastEditOn) && Objects.equals(deadline, todoItem.deadline) && Objects.equals(done, todoItem.done) && Objects.equals(checked, todoItem.checked) && Objects.equals(edited, todoItem.edited) && Objects.equals(wasEdited, todoItem.wasEdited);
    }

    @Override
    public int hashCode() {
        return Objects.hash(todoItemId, itemValue, createdOn, lastEditOn, deadline, done, checked, edited, wasEdited);
    }

}
