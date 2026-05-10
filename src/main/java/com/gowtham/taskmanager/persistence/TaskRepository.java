package com.gowtham.taskmanager.persistence;
import java.util.ArrayList;
import com.gowtham.taskmanager.model.Task;
public interface TaskRepository {
    public void saveTasks(ArrayList<Task> tasks);
    public ArrayList<Task> loadTasks();
}