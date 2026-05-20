package com.myapp.taskmanager.model;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import java.time.LocalDate;
@Entity
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;
    private boolean isCompleted;
    private Priority priority;
    private LocalDate dueDate;
    public Task(){
    }
    public Task(Integer id, String title, boolean isCompleted, Priority priority,LocalDate dueDate){
        this.id = id;
        this.title = title;
        this.isCompleted = isCompleted;
        this.priority = priority;
        this.dueDate = dueDate;
    }
    
    public int getId(){
        return id;
    }
    public String getTitle(){
        return title;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public Priority getPriority(){
        return priority;
    }
    public void setPriority(Priority priority){
        this.priority = priority;
    }
    public LocalDate getDueDate(){
        return dueDate;
    }
    public void setDueDate(LocalDate dueDate){
        this.dueDate = dueDate;
    }
    public boolean isCompleted() {
        return isCompleted;
    }
    public void markCompleted(){
        this.isCompleted = true;
    }
    public String toString(){
        return id + " | " + title + " | " + priority + " | " + dueDate + " | " + (isCompleted ? "Completed" : "Pending");
    }
}
