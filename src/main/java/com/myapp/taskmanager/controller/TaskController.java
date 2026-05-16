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
    @PostMapping("/tasks")
    public TaskAddResult addTask(@RequestBody CreateTaskRequest request){
        return taskService.addTask(request.getTitle(), 
                request.getPriority(),
                request.getDueDate());
    }
    @DeleteMapping("/tasks/{id}/complete")
    public boolean deleteTask(@PathVariable("id") int id){
        return taskService.deleteTask(id);
    }
    @PatchMapping("/tasks/{id}")
    public TaskCompletionResult markTaskCompleted(@PathVariable("id") int id){
        return taskService.markTaskCompleted(id);
    }
    @PutMapping("/tasks/{id}")
    public TaskEditResult updateTask(@PathVariable("id") int id, @RequestBody CreateTaskRequest request){
        return taskService.editTask(id, request.getTitle(), request.getPriority(), request.getDueDate());
    }
}
