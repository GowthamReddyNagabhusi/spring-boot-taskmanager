package com.myapp.taskmanager.service;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import com.myapp.taskmanager.model.Priority;
import com.myapp.taskmanager.model.Task;
import com.myapp.taskmanager.model.TaskStatistics;
import com.myapp.taskmanager.persistence.TaskRepository;
import java.time.LocalDate;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
@Service
public class TaskService {
    private List<Task> tasks;
    private int idCounter;
    private TaskRepository repository;
    private Task findTaskById(int id){
        return tasks.stream()
        .filter(task -> task.getId() == id)
        .findFirst()
        .orElse(null);
    }
    private ValidationResult validateTitle(String title, boolean isEdit) {
        if(title == null || title.trim().isEmpty()){
            return isEdit ? ValidationResult.NO_INPUT : ValidationResult.EMPTY_TITLE;
        }
        if(title.length() > 100){
            return ValidationResult.TOO_LONG_TITLE;
        }
        return ValidationResult.VALID;
    }
    private ValidationResult validateDueDate(String dueDateString, boolean isEdit) {
        if(dueDateString == null || dueDateString.trim().isEmpty()){
            return isEdit ? ValidationResult.NO_INPUT : ValidationResult.INVALID_DATE;
        }
        try{
            LocalDate dueDate = LocalDate.parse(dueDateString);
            if(dueDate.isBefore(LocalDate.now())){
                return ValidationResult.IN_PAST;
            }
            return ValidationResult.VALID;
        }catch(Exception e){
            return ValidationResult.INVALID_DATE;
        }
    }
        
    private ValidationResult validatePriority(Integer priorityInt, boolean isEdit) {
        if (priorityInt == null || priorityInt == 0) {
            return isEdit ? ValidationResult.NO_INPUT : ValidationResult.INVALID_PRIORITY;
        }
        if (priorityInt < 1 || priorityInt > 3) {
            return ValidationResult.INVALID_PRIORITY;
        }
        return ValidationResult.VALID;
    }

    private void saveTasks(){
        repository.saveTasks(tasks);
    }
    public TaskService(TaskRepository repository){
        this.repository = repository;
        tasks = repository.loadTasks();
        idCounter = 1;
        for(Task task : tasks){
            int taskId = task.getId();
            idCounter = Math.max(idCounter, taskId + 1);
        }
    }
    public TaskAddResult addTask(String title, Integer priorityInt, 
        String dueDateString) {
        ValidationResult titleValidation = validateTitle(title, false);
        if (titleValidation == ValidationResult.EMPTY_TITLE) return TaskAddResult.EMPTY_TITLE;
        if (titleValidation == ValidationResult.TOO_LONG_TITLE) return TaskAddResult.TOO_LONG_TITLE;

        ValidationResult priorityValidation = validatePriority(priorityInt, false);
        if (priorityValidation == ValidationResult.INVALID_PRIORITY) return TaskAddResult.INVALID_PRIORITY;

        ValidationResult dueDateValidation = validateDueDate(dueDateString, false);
        if (dueDateValidation == ValidationResult.INVALID_DATE) return TaskAddResult.DUE_DATE_INVALID_FORMAT;

        Priority priority = Priority.fromChoice(priorityInt);
        LocalDate dueDate = LocalDate.parse(dueDateString);
        Task newTask = new Task(idCounter++, title, 
            false, priority, dueDate);
        tasks.add(newTask);
        saveTasks();
        return TaskAddResult.ADD_SUCCESS;  
    }
    
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks);
    }
    public Task getTaskById(int id){
        Task task = findTaskById(id);
        if(task != null){
            return task;
        }
        return null;
    }
    public boolean deleteTask(int id) {
        Iterator<Task> iterator = tasks.iterator();

        while(iterator.hasNext()) {
            Task task = iterator.next();
            if(task.getId() == id) {
                iterator.remove();
                saveTasks();
                return true;
            }
        }

        return false;
    }
    public TaskCompletionResult markTaskCompleted(int id){
        Task task = findTaskById(id);
        if(task == null){
            return TaskCompletionResult.NOT_FOUND;
        }
        if(task.isCompleted()){
            return TaskCompletionResult.ALREADY_COMPLETED;
        }
        task.markCompleted();
        saveTasks();
        return TaskCompletionResult.SUCCESS;
    }
    public List<Task> searchTasks(String keyword){
        String lowerKeyword = keyword.toLowerCase();
        return tasks.stream()
                .filter(task -> task.getTitle()
                .toLowerCase().contains(lowerKeyword))
                .collect(Collectors.toList());
    }
    public List<Task> getAllCompletedTasks(){
        return tasks.stream()
                .filter(Task::isCompleted)
                .collect(Collectors.toList());
    }
    public List<Task> getAllPendingTasks(){
        return tasks.stream()
                .filter(task -> !task.isCompleted())
                .collect(Collectors.toList());
    }
    public List<Task> getAllTasksSortedByPriority(){
        return tasks.stream()
        .sorted((task1, task2) ->
                task2.getPriority().compareTo(task1.getPriority()))
        .collect(Collectors.toList());
    }
    public List<Task> getAllOverdueTasks(){
        LocalDate today = LocalDate.now();
        return tasks.stream()
        .filter(task -> !task.isCompleted() 
        && task.getDueDate().isBefore(today))
        .collect(Collectors.toList());
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
        return new TaskStatistics(totalTasks, completedTasks, 
            pendingTasks, highPriorityPendingTasks, overdueTasks);
    }
    public TaskEditResult editTask(int id,
                     String newTitle,
                     Integer newPriority,
                     String newDueDate) {
        Task task = findTaskById(id);
        if(task == null){
            return TaskEditResult.TASK_NOT_FOUND;
        }

        ValidationResult validationTitle = validateTitle(newTitle, true);
        if (validationTitle == ValidationResult.TOO_LONG_TITLE) return TaskEditResult.TOO_LONG_TITLE;

        ValidationResult validationPriority = validatePriority(newPriority, true);
        if (validationPriority == ValidationResult.INVALID_PRIORITY) return TaskEditResult.INVALID_PRIORITY;

        ValidationResult validationDueDate = validateDueDate(newDueDate, true);
        if (validationDueDate == ValidationResult.IN_PAST) return TaskEditResult.DUE_DATE_IN_PAST;
        if (validationDueDate == ValidationResult.INVALID_DATE) return TaskEditResult.DUE_DATE_INVALID_FORMAT;
        
        if (validationTitle == ValidationResult.NO_INPUT && 
            validationPriority == ValidationResult.NO_INPUT && 
            validationDueDate == ValidationResult.NO_INPUT) {
            return TaskEditResult.NO_CHANGES_PROVIDED;
        }

        if(validationTitle == ValidationResult.VALID){
            task.setTitle(newTitle);
        }
        if(validationPriority == ValidationResult.VALID){
            task.setPriority(Priority.fromChoice(newPriority));
        }
        if(validationDueDate == ValidationResult.VALID){
            task.setDueDate(LocalDate.parse(newDueDate));
        }
        
        saveTasks();
        return TaskEditResult.EDIT_SUCCESS;
    }
}
