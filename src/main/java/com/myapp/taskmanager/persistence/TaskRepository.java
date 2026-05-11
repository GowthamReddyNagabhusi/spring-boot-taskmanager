package com.myapp.taskmanager.persistence;
import java.util.ArrayList;

import com.myapp.taskmanager.model.Task;
public interface TaskRepository {
    public void saveTasks(ArrayList<Task> tasks);
    public ArrayList<Task> loadTasks();
}