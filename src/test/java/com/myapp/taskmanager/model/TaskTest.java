package com.myapp.taskmanager.model;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
public class TaskTest {
    //test markCompleted method
    @Test
    void markCompletedMarksTaskAsDone() {
        Task task = new Task(1,"Test Task", false, Priority.MEDIUM, LocalDate.of(2024, 6, 30));
        task.markCompleted();
        assertTrue(task.isCompleted());
    }
    //test toString method
    @Test
    void toStringReturnsCorrectFormat() {
        Task task = new Task(1,"Test Task", false, Priority.MEDIUM, LocalDate.of(2024, 6, 30));
        String expected = "1 | Test Task | MEDIUM | " + LocalDate.of(2024, 6, 30) + " | Pending";
        assertEquals(expected, task.toString());
    }
    // test constructor and getters
    @Test
    void constructorAndGettersWorkCorrectly() {
        LocalDate dueDate = LocalDate.of(2024, 6, 30);
        Task task = new Task(1,"Test Task", false, Priority.HIGH, dueDate);
        assertEquals(1, task.getId());
        assertEquals("Test Task", task.getTitle());
        assertFalse(task.isCompleted());
        assertEquals(Priority.HIGH, task.getPriority());
        assertEquals(dueDate, task.getDueDate());
    }
    // test setters
    @Test
    void settersWorkCorrectly() {
        Task task = new Task(1,"Test Task", false, Priority.LOW, LocalDate.of(2024, 6, 30));
        task.setTitle("Updated Task");
        task.setPriority(Priority.HIGH);
        LocalDate newDueDate = LocalDate.of(2024, 6, 30).plusDays(7);
        task.setDueDate(newDueDate);
        assertEquals("Updated Task", task.getTitle());
        assertEquals(Priority.HIGH, task.getPriority());
        assertEquals(newDueDate, task.getDueDate());
    }
}
