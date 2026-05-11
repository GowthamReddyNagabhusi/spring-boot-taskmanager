package com.myapp.taskmanager.model;


public class TaskStatistics {
    private int totalTasks = 0;
    private int completedTasks = 0;
    private int pendingTasks = 0;
    private int highPriorityPendingTasks = 0;
    private int overdueTasks = 0;
    public TaskStatistics(int totalTasks, int completedTasks, int pendingTasks, int highPriorityPendingTasks, int overdueTasks) {
        this.totalTasks = totalTasks;
        this.completedTasks = completedTasks;
        this.pendingTasks = pendingTasks;
        this.highPriorityPendingTasks = highPriorityPendingTasks;
        this.overdueTasks = overdueTasks;
    }
    public int getTotalTasks() {
        return totalTasks;
    }
    public int getCompletedTasks() {
        return completedTasks;
    }
    public int getPendingTasks() {
        return pendingTasks;
    }
    public int getHighPriorityPendingTasks() {
        return highPriorityPendingTasks;
    }
    public int getOverdueTasks() {
        return overdueTasks;
    }
    @Override
    public String toString(){
        return "Total Tasks: " + totalTasks + "\n" +
               "Completed Tasks: " + completedTasks + "\n" +
               "Pending Tasks: " + pendingTasks + "\n" +
               "High Priority Pending Tasks: " + highPriorityPendingTasks + "\n" +
               "Overdue Tasks: " + overdueTasks;
    }
    
}
