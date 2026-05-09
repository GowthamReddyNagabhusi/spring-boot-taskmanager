package model;
import java.time.LocalDate;
public class Task {
    private int id;
    private String title;
    private boolean isCompleted;
    private Priority priority;
    private LocalDate dueDate;
    public Task(int id, String title, boolean isCompleted, Priority priority,LocalDate dueDate){
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
