package com.gowtham.taskmanager.ui;
import java.time.LocalDate;
import java.util.Scanner;

import com.gowtham.taskmanager.model.Priority;
import com.gowtham.taskmanager.service.TaskService;
import com.gowtham.taskmanager.persistence.FileHandler;
public class Main{
    public static void main(String[] args){

        TaskService service = new TaskService(new FileHandler());
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
                    System.out.println("--- Priority Menu---");
                    System.out.println("1. Low");
                    System.out.println("2. Medium");
                    System.out.println("3. High");
                    System.out.print("Enter Task Priority: ");
                    int priorityChoice = scanner.nextInt();
                    Priority priority = Priority.fromChoice(priorityChoice);
                    scanner.nextLine();
                    System.out.print("Enter Due Date (YYYY-MM-DD): ");
                    String dateInput = scanner.nextLine();
                    try{
                        LocalDate dueDate = LocalDate.parse(dateInput);
                        service.addTask(title, priority, dueDate);
                    }catch(Exception e){
                        System.out.println("Invalid date format. Task not added.");
                    }
                    System.out.println();
                    break;
                case 2:
                    service.viewTasks();
                    System.out.println();
                    break;
                case 3:
                    System.out.print("Enter Task Id: ");
                    int id = scanner.nextInt();
                    service.deleteTask(id);
                    System.out.println();
                    break;
                case 4:
                    System.out.print("Enter Task Id: ");
                    id = scanner.nextInt();
                    service.markTaskCompleted(id);
                    System.out.println();
                    break;
                case 5:
                    System.out.print("Enter Task Id: ");
                    id = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter new title (leave blank to keep unchanged): ");
                    String newTitle = scanner.nextLine();
                    System.out.println("--- Priority Menu---");
                    System.out.println("1. Low");
                    System.out.println("2. Medium");
                    System.out.println("3. High");
                    System.out.print("Enter new priority (0 to keep unchanged): ");
                    int newPriorityChoice = scanner.nextInt();
                    if(newPriorityChoice < 0 || newPriorityChoice > 3){
                        System.out.println("Invalid priority choice. Priority not changed.");
                        newPriorityChoice = 0;
                    }
                    Priority newPriority = newPriorityChoice == 0 ? null : Priority.fromChoice(newPriorityChoice);
                    scanner.nextLine();
                    System.out.print("Enter new due date (YYYY-MM-DD, leave blank to keep unchanged): ");
                    String newDateInput = scanner.nextLine();
                    LocalDate newDueDate = null;
                    if(!newDateInput.isEmpty()){
                        try{
                            newDueDate = LocalDate.parse(newDateInput);
                        }catch(Exception e){
                            System.out.println("Invalid date format. Due date not changed.");
                        }
                    }
                    service.editTask(id, newTitle, newPriority, newDueDate);
                    System.out.println();
                    break;
                case 6:
                    scanner.nextLine();
                    System.out.print("Enter keyword: ");
                    String keyword = scanner.nextLine();
                    service.searchTasks(keyword);
                    System.out.println();
                    break;
                case 7:
                    service.viewCompletedTasks();
                    System.out.println();
                    break;
                case 8:
                    service.viewPendingTasks();
                    System.out.println();
                    break;
                case 9:
                    service.viewTasksSortedByPriority();
                    System.out.println();
                    break;
                case 10:
                    service.viewOverdueTasks();
                    System.out.println();
                    break;
                case 11:
                    service.viewTaskStatistics();
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

