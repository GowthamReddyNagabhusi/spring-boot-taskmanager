package com.myapp.taskmanager.controller;

import com.myapp.taskmanager.model.Task;
import com.myapp.taskmanager.service.TaskService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import com.myapp.taskmanager.service.TaskAddResult;
import com.myapp.taskmanager.dto.CreateTaskRequest;
import com.myapp.taskmanager.service.TaskCompletionResult;
import org.springframework.web.bind.annotation.PutMapping;
import com.myapp.taskmanager.service.TaskEditResult;
import org.springframework.http.ResponseEntity;
import com.myapp.taskmanager.dto.UpdateTaskRequest;
import java.util.List;

@RestController
public class TaskController {
    private final TaskService taskService;
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }
    @GetMapping("/tasks")
    public List<Task> getTasks() {
        return taskService.getAllTasks();
    }
    @GetMapping("/tasks/{id}")
    public ResponseEntity<?> getTaskById(@PathVariable("id") int id){
        Task task = taskService.getTaskById(id);
        if(task != null){
            return ResponseEntity.ok(task);
        }else{
            return ResponseEntity.status(404)
                    .body("Task not found");
        }
    }
    
    @PostMapping("/tasks")
    public ResponseEntity<String> addTask(@RequestBody CreateTaskRequest request){
        TaskAddResult result =  taskService.addTask(request.getTitle(),
                                request.getPriority(),
                                request.getDueDate());
        switch(result){
            case ADD_SUCCESS:
                return ResponseEntity
                        .status(201)
                        .body("Task added successfully");
            case EMPTY_TITLE:
                return ResponseEntity
                        .badRequest()
                        .body("Title cannot be empty");
            case TOO_LONG_TITLE:
                return ResponseEntity
                        .badRequest()
                        .body("Title cannot exceed maximum length");
            case INVALID_PRIORITY:
                return ResponseEntity
                        .badRequest()
                        .body("Invalid priority");
            case DUE_DATE_INVALID_FORMAT:
                return ResponseEntity
                        .badRequest()
                        .body("Due date must be in format YYYY-MM-DD");
            default:
                return ResponseEntity
                        .internalServerError()
                        .body("Something went wrong");
        }
    }
    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable("id") int id){
        boolean deleted = taskService.deleteTask(id);
        if(deleted){
            return ResponseEntity.ok("Task deleted successfully");
        }else{
            return ResponseEntity.status(404).body("Task not found");
        }
    }
    @PatchMapping("/tasks/{id}/complete")
    public ResponseEntity<String> markTaskCompleted(@PathVariable("id") int id){
        TaskCompletionResult result =  taskService
                                        .markTaskCompleted(id);
        switch(result){
            case SUCCESS:
                return ResponseEntity
                        .ok("Task marked as completed");
            case NOT_FOUND:
                return ResponseEntity
                        .status(404)
                        .body("Task not found");
            case ALREADY_COMPLETED:
                return ResponseEntity
                        .badRequest()
                        .body("Task is already completed");
            default:
                return ResponseEntity
                        .internalServerError()
                        .body("Something went wrong");
        }
    }
    @PutMapping("/tasks/{id}")
    public ResponseEntity<String> updateTask(@PathVariable("id") int id, @RequestBody UpdateTaskRequest request){
        TaskEditResult result = taskService
                                    .editTask(id, request.getTitle(),
                                     request.getPriority(), 
                                     request.getDueDate());
        switch(result){
            case EDIT_SUCCESS:
                return ResponseEntity
                        .ok("Task updated successfully");
            case TASK_NOT_FOUND:
                return ResponseEntity
                        .status(404)
                        .body("Task not found");
            case TOO_LONG_TITLE:
                return ResponseEntity
                        .badRequest()
                        .body("Title cannot exceed maximum length");
            case NO_CHANGES_PROVIDED:
                return ResponseEntity
                        .badRequest()
                        .body("No changes provided");
            case INVALID_PRIORITY:
                return ResponseEntity
                        .badRequest()
                        .body("Invalid priority");
            case DUE_DATE_IN_PAST:
                return ResponseEntity
                        .badRequest()
                        .body("Due date cannot be in the past");
            case DUE_DATE_INVALID_FORMAT:
                return ResponseEntity
                        .badRequest()
                        .body("Due date must be in format YYYY-MM-DD");
            default:
                return ResponseEntity
                        .internalServerError()
                        .body("Something went wrong");
        }
    }
}
