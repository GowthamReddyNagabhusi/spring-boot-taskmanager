package com.gowtham.taskmanager.service;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.function.Predicate;

import com.gowtham.taskmanager.model.Priority;
import com.gowtham.taskmanager.model.Task;
import com.gowtham.taskmanager.persistence.FileHandler;
import com.gowtham.taskmanager.persistence.TaskRepository;

import java.time.LocalDate;

public class TaskService {
    private ArrayList<Task> tasks;
    private int idCounter;
    private TaskRepository repository;
    public TaskService(TaskRepository repository){
        this.repository = repository;
        tasks = repository.loadTasks();
        idCounter = 1;
        for(Task task : tasks){
            int taskId = task.getId();
            idCounter = Math.max(idCounter, taskId + 1);
        }
    }
    public void addTask(String title, Priority priority, LocalDate dueDate) {
        Task task = new Task(idCounter++, title, false, priority, dueDate);
        tasks.add(task);
        repository.saveTasks(tasks);
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
                repository.saveTasks(tasks);
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
                    repository.saveTasks(tasks);
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
    public void viewTasksSortedByPriority(){
        ArrayList<Task> sortedTasks = new ArrayList<>(tasks);
        sortedTasks.sort((task1, task2) -> task2.getPriority().compareTo(task1.getPriority()));
        for(Task task : sortedTasks){
            System.out.println(task);
        }
    }
    public void viewOverdueTasks(){
        LocalDate today = LocalDate.now();
        filterTasks(task -> !task.isCompleted() && task.getDueDate().isBefore(today), "No overdue tasks");
    }
    public void viewTaskStatistics(){
        int totalTasks = tasks.size();
        int completedTasks = 0;
        int pendingTasks = 0;
        int highPriorityPendingTasks = 0;
        int overdueTasks = 0;
        LocalDate today = LocalDate.now();
        for(Task task : tasks){
            if(task.isCompleted()){
                completedTasks++;
            }else{
                pendingTasks++;
                if(task.getPriority() == Priority.HIGH){
                    highPriorityPendingTasks++;
                }
                if(task.getDueDate().isBefore(today)){
                    overdueTasks++;
                }
            }
        }
        System.out.println("Total Tasks: " + totalTasks);
        System.out.println("Completed Tasks: " + completedTasks);
        System.out.println("Pending Tasks: " + pendingTasks);
        System.out.println("High Priority Pending Tasks: " + highPriorityPendingTasks);
        System.out.println("Overdue Tasks: " + overdueTasks);
    }
    public void editTask(int id,
                     String newTitle,
                     Priority newPriority,
                     LocalDate newDueDate){
        for(Task task : tasks){
            if(task.getId() == id){
                if(!newTitle.isEmpty()) task.setTitle(newTitle);
                if(newPriority != null)task.setPriority(newPriority);
                if(newDueDate != null)task.setDueDate(newDueDate);
                repository.saveTasks(tasks);
                System.out.println("Task updated successfully");
                return;
            }
        }
        System.out.println("Task not found");
    }
}
