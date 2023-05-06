package com.taskmanager.managers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.taskmanager.history.HistoryManager;
import com.taskmanager.history.InMemoryHistoryManager;
import com.taskmanager.taskmanager.TaskManager;
import java.io.IOException;
import java.time.LocalDateTime;


public class Managers {

    public static TaskManager getDefault() throws IOException {
       // return new InMemoryTaskManager();
       // return new FileBackedTasksManager(new File("src/com/taskmanager/resources/saveFile.csv"));
        return new HttpTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static Gson getGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.serializeNulls();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        gsonBuilder.setPrettyPrinting();
        return gsonBuilder.create();
    }

}


