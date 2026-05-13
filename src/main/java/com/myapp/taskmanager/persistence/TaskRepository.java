package com.myapp.taskmanager.persistence;
import java.util.List;
import com.myapp.taskmanager.model.Task;
public interface TaskRepository {
    void saveTasks(List<Task> tasks);
    List<Task> loadTasks();
}