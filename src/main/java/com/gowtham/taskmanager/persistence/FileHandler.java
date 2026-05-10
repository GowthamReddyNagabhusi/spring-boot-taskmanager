package com.gowtham.taskmanager.persistence;
import java.io.File;
import java.util.ArrayList;

import com.gowtham.taskmanager.model.Priority;
import com.gowtham.taskmanager.model.Task;

import java.time.LocalDate;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileReader;
public class FileHandler {
    public void saveTasks(ArrayList<Task> tasks){
        File myFile = new File("tasks.txt");
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(myFile))){
            for(Task task : tasks){
                writer.write(task.getId() + "," + task.getTitle() + "," + task.isCompleted() + "," + task.getPriority() + "," + task.getDueDate());
                writer.newLine();
            }
        }catch(IOException e){
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }
    public ArrayList<Task> loadTasks(){
        File myFile = new File("tasks.txt");
        ArrayList<Task> tasks = new ArrayList<>();
        if(!myFile.exists()){
            return tasks;
        } 
        try(BufferedReader reader = new BufferedReader(new FileReader(myFile))){
            String line;
            while((line = reader.readLine()) != null){
                String[] parts = line.split(",");
                if(parts.length != 5){
                    continue;
                }
                int id = Integer.parseInt(parts[0]);
                String title = parts[1];
                boolean isCompleted = Boolean.parseBoolean(parts[2]);
                Priority priority = Priority.valueOf(parts[3]);
                LocalDate dueDate = LocalDate.parse(parts[4]);
                Task task = new Task(id, title, isCompleted, priority, dueDate);
                tasks.add(task);
            }
        }catch(Exception e){
            System.out.println("Error reading file: " + e.getMessage());  
        }
        return tasks;
    }
}
