import java.util.ArrayList;
import java.util.Iterator;

public class TaskService {
    private ArrayList<Task> tasks;
    private int idCounter = 1;
    public TaskService(){
        tasks = new ArrayList<>();
    }
    
    public void addTask(String title) {
        Task task = new Task(idCounter++, title, false);
        tasks.add(task);
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
                break;
            }
        }
        if(!found){
            System.out.println("Task not found");
        }
    }
}
