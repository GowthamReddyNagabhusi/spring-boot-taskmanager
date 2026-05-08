package ui;
import java.util.Scanner;

import service.TaskService;
public class Main{
    public static void main(String[] args){
        TaskService service = new TaskService();
        try (Scanner scanner = new Scanner(System.in)) {
            while(true){
            System.out.println("--- Task Manager ---");
            System.out.println("1. Add Task");
            System.out.println("2. View Tasks");
            System.out.println("3. Delete Task");
            System.out.println("4. Mark Task Completed");
            System.out.println("5. Search Tasks");
            System.out.println("6. View Completed Tasks");
            System.out.println("7. View Pending Tasks");
            System.out.println("8. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            switch(choice){
                case 1:
                    System.out.print("Enter Task Title: ");
                    scanner.nextLine();
                    String title = scanner.nextLine();
                    service.addTask(title);
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
                    scanner.nextLine();
                    System.out.print("Enter keyword: ");
                    String keyword = scanner.nextLine();
                    service.searchTasks(keyword);
                    System.out.println();
                    break;
                case 6:
                    service.viewCompletedTasks();
                    System.out.println();
                    break;
                case 7:
                    service.viewPendingTasks();
                    System.out.println();
                    break;
                case 8:
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

