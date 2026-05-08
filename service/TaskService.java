package service;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.function.Predicate;

import model.Task;
import persistence.FileHandler;

public class TaskService {
    private ArrayList<Task> tasks;
    private int idCounter;
    private FileHandler fileHandler;
    public TaskService(){
        fileHandler = new FileHandler();
        tasks = fileHandler.loadTasks();
        idCounter = 1;
        for(Task task : tasks){
            int taskId = task.getId();
            idCounter = Math.max(idCounter, taskId + 1);
        }
    }
    public void addTask(String title) {
        Task task = new Task(idCounter++, title, false);
        tasks.add(task);
        fileHandler.saveTasks(tasks);
    }
    
    public void viewTasks() {
        if(tasks.isEmpty()){
            System.out.println("No tasks available");
            return;
        }
        for(Task task : tasks) {
            System.out.println(task);
        }
    }
    public void deleteTask(int id) {
        boolean found = false;
        Iterator<Task> iterator = tasks.iterator();

        while(iterator.hasNext()) {
            Task task = iterator.next();
            if(task.getId() == id) {
                found = true;
                iterator.remove();
                System.out.println("Task deleted successfully");
                fileHandler.saveTasks(tasks);
                break;
            }
        }
        if(!found){
            System.out.println("Task not found");
        }
    }
    public void markTaskCompleted(int id){
        boolean found = false;
        for(Task task : tasks){
            if(task.getId() == id){
                if(!task.isCompleted()){
                    task.markCompleted();
                    fileHandler.saveTasks(tasks);
                    System.out.println("Task marked completed");
                    found = true;
                    break;
                }else{
                    System.out.println("Task is already completed");
                    found = true;
                    break;
                }
            }
        }
        if(!found){
            System.out.println("Task not found");
        }
    }
    private void filterTasks(Predicate<Task> condition, String emptyMessage){
        boolean found = false;
        for(Task task : tasks){
            if(condition.test(task)){
                System.out.println(task);
                found = true;
            }
        }
        if(!found) System.out.println(emptyMessage);
    }
    public void searchTasks(String keyword){
        String lowerKeyword = keyword.toLowerCase();
        filterTasks(task -> task.getTitle().toLowerCase().contains(lowerKeyword), "No matching tasks");
    }
    public void viewCompletedTasks(){
        filterTasks(task -> task.isCompleted(), "No completed tasks");
    }
    public void viewPendingTasks(){
        filterTasks(task -> !task.isCompleted(), "No pending tasks");
    }
    
}
