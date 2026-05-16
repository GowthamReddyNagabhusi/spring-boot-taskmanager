package com.myapp.taskmanager.service;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;

import com.myapp.taskmanager.model.Priority;
import com.myapp.taskmanager.persistence.FakeTaskRepository;
import com.myapp.taskmanager.persistence.TaskRepository;
import com.myapp.taskmanager.model.Task;
import com.myapp.taskmanager.model.TaskStatistics;


public class TaskServiceTest {
    private TaskRepository repository;
    private TaskService service;
    String futureDate;
    String pastDate;
    @BeforeEach
    public void setUp() {
        repository = new FakeTaskRepository();
        service = new TaskService(repository);
        futureDate = LocalDate.now().plusDays(1).toString();
        pastDate = LocalDate.now().minusDays(1).toString();
    }
    @Test
    public void testAddTask() {
        assertEquals(TaskAddResult.ADD_SUCCESS, service.addTask("New Task", 2, futureDate));
        assertEquals(TaskAddResult.INVALID_PRIORITY, service.addTask("Invalid Priority Task", 5, futureDate));
        assertEquals(TaskAddResult.INVALID_PRIORITY, service.addTask("Invalid Priority Task", -1, futureDate));
        assertEquals(TaskAddResult.ADD_SUCCESS, service.addTask("Past Due Date Task", 2, pastDate));
        assertEquals(TaskAddResult.DUE_DATE_INVALID_FORMAT, service.addTask("Invalid Date Format Task", 2, "invalid-date"));
        List<Task> tasks = service.getAllTasks();
        assertEquals(2, tasks.size());
        Task task1 = tasks.get(0);
        assertEquals("New Task", task1.getTitle());
        assertEquals(Priority.MEDIUM, task1.getPriority());
        assertEquals(LocalDate.parse(futureDate), task1.getDueDate());
        Task task2 = tasks.get(1);
        assertEquals("Past Due Date Task", task2.getTitle());
        assertEquals(Priority.MEDIUM, task2.getPriority());
        assertEquals(LocalDate.parse(pastDate), task2.getDueDate());
    }
    @Test
    public void testGetAllTasks() {
        service.addTask("Task 1", 1, futureDate);
        service.addTask("Task 2", 3, futureDate);
        List<Task> tasks = service.getAllTasks();
        Task task1 = tasks.get(0);
        Task task2 = tasks.get(1);
        assertEquals(2, tasks.size());
        assertEquals("Task 1", task1.getTitle());
        assertEquals(Priority.LOW, task1.getPriority());
        assertEquals(LocalDate.parse(futureDate), task1.getDueDate());
        assertEquals("Task 2", task2.getTitle());
        assertEquals(Priority.HIGH, task2.getPriority());
        assertEquals(LocalDate.parse(futureDate), task2.getDueDate());
    }
    @Test
    public void testDeleteTask() {
        service.addTask("Task to Delete", 2, futureDate);
        int taskId = service.getAllTasks().get(0).getId();
        boolean deleted = service.deleteTask(taskId);
        assertFalse(service.deleteTask(999)); 
        assertTrue(deleted);
        assertEquals(0, service.getAllTasks().size());
    }
    @Test
    public void testMarkTaskCompleted() {
        service.addTask("Task to Complete", 3, futureDate);
        int taskId = service.getAllTasks().get(0).getId();
        TaskCompletionResult result = service.markTaskCompleted(taskId);
        assertEquals(TaskCompletionResult.SUCCESS, result);
        assertTrue(service.getAllTasks().get(0).isCompleted());
        TaskCompletionResult secondResult = service.markTaskCompleted(taskId);
        assertEquals(TaskCompletionResult.ALREADY_COMPLETED, secondResult);
        assertEquals(TaskCompletionResult.NOT_FOUND, service.markTaskCompleted(999));
    }
    @Test
    public void testEditTask() {
        service.addTask("Original Task", 1, futureDate);
        int taskId = service.getAllTasks().get(0).getId();
        TaskEditResult result = service.editTask(taskId, "", 0, "");
        assertEquals(TaskEditResult.NO_CHANGES_PROVIDED, result);
        result = service.editTask(taskId, "Updated Task", 2, pastDate);
        assertEquals(TaskEditResult.DUE_DATE_IN_PAST, result);
        result = service.editTask(taskId, "Updated Task", 2, "invalid-date");
        assertEquals(TaskEditResult.DUE_DATE_INVALID_FORMAT, result);
        assertEquals(TaskEditResult.EDIT_SUCCESS, service.editTask(taskId, "Updated Task", 2, futureDate));
        Task editedTask = service.getAllTasks().get(0);
        assertEquals("Updated Task", editedTask.getTitle());
        assertEquals(Priority.MEDIUM, editedTask.getPriority());
        assertEquals(LocalDate.parse(futureDate), editedTask.getDueDate());
        assertEquals(TaskEditResult.TASK_NOT_FOUND, service.editTask(999, "Non-existent Task", 1, futureDate));
        assertEquals(TaskEditResult.INVALID_PRIORITY, service.editTask(taskId, "Updated Task", 5, futureDate));
        assertEquals(TaskEditResult.INVALID_PRIORITY, service.editTask(taskId, "Updated Task", -1, futureDate));
    }
    @Test
    public void testGetTaskStatistics() {
        service.addTask("Completed Task", 1, futureDate);
        service.addTask("Pending High Priority Task", 3, futureDate);
        service.addTask("Overdue Task", 2, pastDate);
        service.addTask("Another Overdue Task", 3, pastDate);
        int completedTaskId = service.getAllTasks().get(0).getId();
        service.markTaskCompleted(completedTaskId);
        TaskStatistics stats = service.getTaskStatistics();
        assertEquals(4, stats.getTotalTasks());
        assertEquals(1, stats.getCompletedTasks());
        assertEquals(3, stats.getPendingTasks());
        assertEquals(2, stats.getHighPriorityPendingTasks());
        assertEquals(2, stats.getOverdueTasks());
    }
    @Test
    public void testGetAllCompletedTasks() {
        service.addTask("Completed Task 1", 1, futureDate);
        service.addTask("Completed Task 2", 3, futureDate);
        service.addTask("Pending Task", 2, futureDate);
        int completedTaskId1 = service.getAllTasks().get(0).getId();
        int completedTaskId2 = service.getAllTasks().get(1).getId();
        service.markTaskCompleted(completedTaskId1);
        service.markTaskCompleted(completedTaskId2);
        List<Task> completedTasks = service.getAllCompletedTasks();
        assertEquals(2, completedTasks.size());
        assertTrue(completedTasks.stream().anyMatch(t -> t.getTitle().equals("Completed Task 1")));
        assertTrue(completedTasks.stream().anyMatch(t -> t.getTitle().equals("Completed Task 2")));
    }
    @Test
    public void testGetAllPendingTasks() {
        service.addTask("Pending Task 1", 1, futureDate);
        service.addTask("Pending Task 2", 3, futureDate);
        service.addTask("Completed Task", 2, futureDate);
        int completedTaskId = service.getAllTasks().get(2).getId();
        service.markTaskCompleted(completedTaskId);
        List<Task> pendingTasks = service.getAllPendingTasks();
        assertEquals(2, pendingTasks.size());
        assertTrue(pendingTasks.stream().anyMatch(t -> t.getTitle().equals("Pending Task 1")));
        assertTrue(pendingTasks.stream().anyMatch(t -> t.getTitle().equals("Pending Task 2")));
    }
    @Test
    public void testGetAllOverdueTasks() {
        service.addTask("Overdue Task 1", 1, pastDate);
        service.addTask("Overdue Task 2", 3, pastDate);
        service.addTask("Pending Task", 2, futureDate);
        List<Task> overdueTasks = service.getAllOverdueTasks();
        assertEquals(2, overdueTasks.size());
        assertTrue(overdueTasks.stream().anyMatch(t -> t.getTitle().equals("Overdue Task 1")));
        assertTrue(overdueTasks.stream().anyMatch(t -> t.getTitle().equals("Overdue Task 2")));
    }
    @Test
    public void testGetAllTasksSortedByPriority() {
        service.addTask("Low Priority Task", 1, futureDate);
        service.addTask("High Priority Task", 3, futureDate);
        service.addTask("Medium Priority Task", 2, futureDate);
        List<Task> sortedTasks = service.getAllTasksSortedByPriority();
        assertEquals(3, sortedTasks.size());
        assertEquals("High Priority Task", sortedTasks.get(0).getTitle());
        assertEquals("Medium Priority Task", sortedTasks.get(1).getTitle());
        assertEquals("Low Priority Task", sortedTasks.get(2).getTitle());
    }
    @Test
    public void testSearchTasks() {
        service.addTask("Buy groceries", 1, futureDate);
        service.addTask("Call Alice", 2, futureDate);
        service.addTask("Finish report", 3, futureDate);
        List<Task> searchResults = service.searchTasks("call");
        assertEquals(1, searchResults.size());
        assertEquals("Call Alice", searchResults.get(0).getTitle());
        searchResults = service.searchTasks("report");
        assertEquals(1, searchResults.size());
        assertEquals("Finish report", searchResults.get(0).getTitle());
        searchResults = service.searchTasks("nonexistent");
        assertEquals(0, searchResults.size());
        searchResults = service.searchTasks("BUY");
        assertEquals(1, searchResults.size());
        assertEquals("Buy groceries", searchResults.get(0).getTitle());

    }
}
