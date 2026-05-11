package com.myapp.taskmanager.persistence;
import java.util.ArrayList;
import com.myapp.taskmanager.model.Task;

public class FakeTaskRepository implements TaskRepository {
    private ArrayList<Task> tasks = new ArrayList<>();
    @Override
    public void saveTasks(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }
    @Override
    public ArrayList<Task> loadTasks() {
        return this.tasks;
    }
    
}
