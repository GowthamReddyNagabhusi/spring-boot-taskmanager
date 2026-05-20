package com.myapp.taskmanager.persistence;
import java.util.List;
import com.myapp.taskmanager.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
public interface TaskRepository
        extends JpaRepository<Task, Integer> {
    
}