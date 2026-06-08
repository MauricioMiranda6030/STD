package org.example.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.example.entity.Config;
import org.example.entity.Task;

import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PersistService {

    private final Gson gson =  new GsonBuilder().setPrettyPrinting().create();
    private final String appDataPath = System.getenv("APPDATA");
    private final File dir = new File(appDataPath, "Daily ToDo");

    private final File confFile = new File(dir, "conf.json");
    private final File tasksFile = new File(dir, "tasks.json");
    private final File resetFile = new File(dir, "reset.json");

    private final Type tasksType = new TypeToken<ArrayList<Task>>(){}.getType();

    private static PersistService instance;

    private PersistService(){
        createDir();
    }

    public List<Task> getTasks(){

        if(!tasksFile.exists())
            return new ArrayList<Task>();

        try {
            FileReader reader = new FileReader(tasksFile);
            List<Task> loaded = gson.fromJson(reader, tasksType);
            return loaded != null ? loaded : new ArrayList<>();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveTasks(List<Task> tasks){
        try {
            FileWriter writer = new FileWriter(tasksFile);
            gson.toJson(tasks, writer);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Config getConf(){

        if(!confFile.exists())
            return new Config(0,45,6,0);
        try {
            FileReader reader = new FileReader(confFile);
            Config loaded = gson.fromJson(reader, Config.class);
            return loaded != null ? loaded : new Config(0,45,6,0);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveConf(Config config){
        try {
            FileWriter writer = new FileWriter(confFile);
            gson.toJson(config, writer);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveLastResetDate(String lastReset){
        try{
            FileWriter writer = new FileWriter(resetFile);
            gson.toJson(lastReset, writer);
            writer.close();
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getLastResetDate(){

        Config conf = getConf();

        if(!resetFile.exists()){
            String firstTimeReset = LocalDateTime.now()
                    .withHour(conf.getResetHour())
                    .withMinute(conf.getResetMin())
                    .toString();

            saveLastResetDate(firstTimeReset);
            return firstTimeReset;
        }

        try{
            FileReader reader = new FileReader(resetFile);
            return gson.fromJson(reader, String.class);
        }catch (RuntimeException | FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void createDir(){
        if(!dir.exists())
            dir.mkdirs();
    }

    public static PersistService getInstance(){
        if(instance == null)
            instance = new PersistService();
        return instance;
    }

}
