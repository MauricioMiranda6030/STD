package org.example.service;

import lombok.Getter;
import org.example.entity.Task;
import java.util.List;

public class TaskService{

    private static TaskService instance;

    private final static PersistService persistService = PersistService.getInstance();

    @Getter
    List<Task> tasks;

    private TaskService(){
        tasks = persistService.getTasks();
    }

    public static TaskService getInstance(){
        if(instance == null)
            instance = new TaskService();
        return instance;
    }

    public void addTask(Task task){
        tasks.add(task);
        persistService.saveTasks(tasks);
    }

    public void deleteTask(Task task){
        tasks.remove(task);
        persistService.saveTasks(tasks);
    }

    public void updateStatus(int row){
        tasks.get(row).switchStatus();
        persistService.saveTasks(tasks);
    }

    public void resetTaskStatus(){
        tasks.forEach(t -> {
            if(t.getStatus())
                t.switchStatus();
        });
        persistService.saveTasks(tasks);
    }
}
