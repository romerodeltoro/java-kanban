package com.taskmanager;

import com.google.gson.Gson;
import com.taskmanager.taskmanager.FileBackedTasksManager;

import java.util.ArrayList;

public class HttpTaskManager extends FileBackedTasksManager {

    private final KVTaskClient kvTaskClient;
    private final Gson gson = new Gson();

    public HttpTaskManager(String url) {
        this.kvTaskClient = new KVTaskClient();
    }
    @Override
    public void save() {
    //    super.save();
        String tasksJson = gson.fromJson(new ArrayList<>(tasks.values()));
        kvTaskClient.put("tasks", tasksJson);
    }
}
