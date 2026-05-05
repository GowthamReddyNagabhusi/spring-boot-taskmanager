import java.util.Scanner;
public class Main{
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        TaskService service = new TaskService();
        while(true){
            System.out.println("--- Task Manager ---");
            System.out.println("1. Add Task");
            System.out.println("2. View Tasks");
            System.out.println("3. Delete Task");
            System.out.println("4. Exit");
            System.out.println("Enter your choice: ");
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

