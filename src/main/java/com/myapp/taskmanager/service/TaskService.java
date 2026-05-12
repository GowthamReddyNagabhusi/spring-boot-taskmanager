package com.myapp.taskmanager.service;
import java.util.ArrayList;
import java.util.Iterator;
import com.myapp.taskmanager.model.Priority;
import com.myapp.taskmanager.model.Task;
import com.myapp.taskmanager.model.TaskStatistics;
import com.myapp.taskmanager.persistence.TaskRepository;
import java.time.LocalDate;
import java.util.stream.Collectors;

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
    public TaskAddResult addTask(String title, int priorityInt, String dueDateString) {
        if(priorityInt < 1 || priorityInt > 3){
            return TaskAddResult.INVALID_PRIORITY;
        }
        Priority priority = Priority.fromChoice(priorityInt);
        LocalDate dueDate;
        try{
            dueDate = LocalDate.parse(dueDateString);
        }catch(Exception e){
            return TaskAddResult.DUE_DATE_INVALID_FORMAT;
        }

        Task task = new Task(idCounter++, title, false, priority, dueDate);
        tasks.add(task);
        repository.saveTasks(tasks);
        return TaskAddResult.ADD_SUCCESS;
    }
    
    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasks);
    }
    public boolean deleteTask(int id) {
        boolean found = false;
        Iterator<Task> iterator = tasks.iterator();

        while(iterator.hasNext()) {
            Task task = iterator.next();
            if(task.getId() == id) {
                found = true;
                iterator.remove();
                repository.saveTasks(tasks);
                break;
            }
        }

        return found;
    }
    public TaskCompletionResult markTaskCompleted(int id){
        for(Task task : tasks){
            if(task.getId() == id){
                if(!task.isCompleted()){
                    task.markCompleted();
                    repository.saveTasks(tasks);
                    return TaskCompletionResult.SUCCESS;
                }else{
                    return TaskCompletionResult.ALREADY_COMPLETED;
                }
            }
        }
        return TaskCompletionResult.NOT_FOUND;
    }
    public ArrayList<Task> searchTasks(String keyword){
        String lowerKeyword = keyword.toLowerCase();
        return tasks.stream()
                .filter(task -> task.getTitle().toLowerCase().contains(lowerKeyword))
                .collect(Collectors.toCollection(ArrayList::new));
    }
    public ArrayList<Task> getAllCompletedTasks(){
        return tasks.stream()
                .filter(Task::isCompleted)
                .collect(Collectors.toCollection(ArrayList::new));
    }
    public ArrayList<Task> getAllPendingTasks(){
        return tasks.stream()
                .filter(task -> !task.isCompleted())
                .collect(Collectors.toCollection(ArrayList::new));
    }
    public ArrayList<Task> getAllTasksSortedByPriority(){
        return tasks.stream()
        .sorted((task1, task2) ->
                task2.getPriority().compareTo(task1.getPriority()))
        .collect(Collectors.toCollection(ArrayList::new));
    }
    public ArrayList<Task> getAllOverdueTasks(){
        LocalDate today = LocalDate.now();
        return tasks.stream()
        .filter(task -> !task.isCompleted() && task.getDueDate().isBefore(today))
        .collect(Collectors.toCollection(ArrayList::new));
    }
    public TaskStatistics getTaskStatistics(){
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
        return new TaskStatistics(totalTasks, completedTasks, pendingTasks, highPriorityPendingTasks, overdueTasks);
    }
    public TaskEditResult editTask(int id,
                     String newTitle,
                     int newPriority,
                     String newDueDate){
        boolean found = false;
        if(newPriority < 0 || newPriority > 3){
            return TaskEditResult.INVALID_PRIORITY;
        }
        Priority priority = null;
        if(newPriority != 0){
            priority = Priority.fromChoice(newPriority);
        }
        LocalDate dueDate = null;
        try{
            if(newDueDate != null && !newDueDate.isEmpty()){
                dueDate = LocalDate.parse(newDueDate);
                if(dueDate.isBefore(LocalDate.now())){
                    return TaskEditResult.DUE_DATE_IN_PAST;
                }
            }
        }
        catch(Exception e){
            return TaskEditResult.DUE_DATE_INVALID_FORMAT;
        }
        if(newTitle.isEmpty() && priority == null && dueDate == null){
            return TaskEditResult.NO_CHANGES_PROVIDED;
        }
        for(Task task : tasks){
            if(task.getId() == id){
                if(!newTitle.isEmpty()){
                    task.setTitle(newTitle);
                }
                if(priority != null){
                    task.setPriority(priority);
                }
                if(dueDate != null){
                    task.setDueDate(dueDate);
                }
                found = true;
            }
        }
        if(!found){
            return TaskEditResult.TASK_NOT_FOUND;
        }
        repository.saveTasks(tasks);
        return TaskEditResult.EDIT_SUCCESS;
    }
}
