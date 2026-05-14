package com.myapp.taskmanager.persistence;
import java.util.ArrayList;
import java.util.List;
import com.myapp.taskmanager.model.Task;

public class FakeTaskRepository implements TaskRepository {
    private List<Task> tasks = new ArrayList<>();
    @Override
    public void saveTasks(List<Task> tasks) {
        this.tasks = tasks;
    }
    @Override
    public List<Task> loadTasks() {
        return this.tasks;
    }
    
}
