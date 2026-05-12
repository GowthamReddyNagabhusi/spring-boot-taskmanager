package com.myapp.taskmanager.ui;
import java.util.Scanner;
import com.myapp.taskmanager.persistence.FileHandler;
import com.myapp.taskmanager.service.TaskService;
import java.util.ArrayList;
import com.myapp.taskmanager.model.Task;
import com.myapp.taskmanager.service.TaskCompletionResult;
import com.myapp.taskmanager.service.TaskEditResult;
import com.myapp.taskmanager.service.TaskAddResult;
import java.util.List;
public class Main{
    private static void printPriorityMenu() {
        System.out.println("--- Priority Menu---");
        System.out.println("1. Low");
        System.out.println("2. Medium");
        System.out.println("3. High");
    }
    private static void printTasks(List<Task> tasks, String emptyMessage){
        if(tasks.isEmpty()){
            System.out.println(emptyMessage);
        }else{
            for(Task task : tasks){
                System.out.println(task);
            }
        }
    }
    public static void main(String[] args){

        TaskService service = new TaskService(new FileHandler());
        ArrayList<Task> tasks;
        try (Scanner scanner = new Scanner(System.in)) {
            while(true){
            System.out.println("--- Task Manager ---");
            System.out.println("1. Add Task");
            System.out.println("2. View Tasks");
            System.out.println("3. Delete Task");
            System.out.println("4. Mark Task Completed");
            System.out.println("5. Edit Tasks");
            System.out.println("6. Search Tasks");
            System.out.println("7. View Completed Tasks");
            System.out.println("8. View Pending Tasks");
            System.out.println("9. View Tasks Sorted By Priority");
            System.out.println("10. View Overdue Tasks");
            System.out.println("11. View Task Statistics");
            System.out.println("12. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            switch(choice){
                case 1:
                    System.out.print("Enter Task Title: ");
                    scanner.nextLine();
                    String title = scanner.nextLine();
                    printPriorityMenu();
                    System.out.print("Enter Task Priority: ");
                    int priority = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter Due Date (YYYY-MM-DD): ");
                    String dueDate = scanner.nextLine();
                    TaskAddResult result = service.addTask(title, priority, dueDate);
                    switch(result){
                        case ADD_SUCCESS:
                            System.out.println("Task added successfully.");
                            break;
                        case INVALID_PRIORITY:
                            System.out.println("Invalid priority choice. Task not added.");
                            break;
                        case DUE_DATE_INVALID_FORMAT:
                            System.out.println("Invalid due date format. Task not added.");
                            break;
                    }
                    System.out.println();
                    break;
                case 2:
                    tasks = service.getAllTasks();
                    printTasks(tasks, "No tasks found.");
                    System.out.println();
                    break;
                case 3:
                    System.out.print("Enter Task Id: ");
                    int id = scanner.nextInt();
                    boolean deleted = service.deleteTask(id);
                    if(!deleted){
                        System.out.println("Task not found. Deletion failed.");
                    }else{
                        System.out.println("Task deleted successfully.");
                    }
                    break;
                case 4:
                    System.out.print("Enter Task Id: ");
                    id = scanner.nextInt();
                    TaskCompletionResult taskCompletionResult = service.markTaskCompleted(id);
                    switch(taskCompletionResult){
                        case SUCCESS:
                            System.out.println("Task marked as completed.");
                            break;
                        case ALREADY_COMPLETED:
                            System.out.println("Task is already completed.");
                            break;
                        case NOT_FOUND:
                            System.out.println("Task not found.");
                            break;
                    }
                    System.out.println();
                    break;
                case 5:
                    System.out.print("Enter Task Id: ");
                    id = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter new title (leave blank to keep unchanged): ");
                    String newTitle = scanner.nextLine();
                    printPriorityMenu();
                    System.out.print("Enter new priority (0 to keep unchanged): ");
                    int newPriorityChoice = scanner.nextInt();  
                    scanner.nextLine();
                    System.out.print("Enter new due date (YYYY-MM-DD, leave blank to keep unchanged): ");
                    String newDueDateChoice = scanner.nextLine();
                    TaskEditResult taskEditResult = service.editTask(id, newTitle, newPriorityChoice, newDueDateChoice);
                    switch(taskEditResult){
                        case EDIT_SUCCESS:
                            System.out.println("Task edited successfully.");
                            break;
                        case TASK_NOT_FOUND:
                            System.out.println("Task not found.");
                            break;
                        case INVALID_PRIORITY:
                            System.out.println("Invalid priority choice. Task not edited.");
                            break;
                        case DUE_DATE_IN_PAST:
                            System.out.println("Due date cannot be in the past. Task not edited.");
                            break;
                        case DUE_DATE_INVALID_FORMAT:
                            System.out.println("Invalid due date format. Task not edited.");
                            break;
                        case NO_CHANGES_PROVIDED:
                            System.out.println("No changes provided. Task not edited.");
                            break;
                    }
                    System.out.println();
                    break;
                case 6:
                    scanner.nextLine();
                    System.out.print("Enter keyword: ");
                    String keyword = scanner.nextLine();
                    tasks = service.searchTasks(keyword);
                    printTasks(tasks, "No tasks found matching the keyword.");
                    System.out.println();
                    break;
                case 7:
                    tasks = service.getAllCompletedTasks();
                    printTasks(tasks, "No Completed tasks found.");
                    System.out.println();
                    break;
                case 8:
                    tasks = service.getAllPendingTasks();
                    printTasks(tasks, "No Pending tasks found.");
                    System.out.println();
                    break;
                case 9:
                    tasks = service.getAllTasksSortedByPriority();
                    printTasks(tasks, "No tasks found.");
                    System.out.println();
                    break;
                case 10:
                    tasks = service.getAllOverdueTasks();
                    printTasks(tasks, "No Overdue tasks found.");
                    System.out.println();
                    break;
                case 11:
                    System.out.println(service.getTaskStatistics());
                    System.out.println();
                    break;
                case 12:
                    System.out.println("Exiting....");
                    System.out.println();
                    return;
                default:
                    System.out.println("Invalid choice");
                    System.out.println();
                }
            }
        }
    }
}

