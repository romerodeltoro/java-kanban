package com.taskmanager.managers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.taskmanager.client.KVTaskClient;
import com.taskmanager.exceptions.ManagerSaveException;
import com.taskmanager.taskmanager.FileBackedTasksManager;
import com.taskmanager.tasks.Epic;
import com.taskmanager.tasks.Subtask;
import com.taskmanager.tasks.Task;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HttpTaskManager extends FileBackedTasksManager {

    private final KVTaskClient client;
    private final Gson gson = Managers.getGson();

    public HttpTaskManager() throws IOException {
        super(null);
        this.client = new KVTaskClient();
    //    load();
    }

    @Override
    public void save() {
    //    super.save();
        try {
            String tasksJson = gson.toJson(new ArrayList<>(tasks.values()));
            String epicsJson = gson.toJson(new ArrayList<>(epics.values()));
            String subtasksJson = gson.toJson(new ArrayList<>(subtasks.values()));
            String historyJson = gson.toJson(new ArrayList<>(getHistory()));
            client.put("tasks", tasksJson);
            client.put("epics", epicsJson);
            client.put("subtasks", subtasksJson);
            client.put("history", historyJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void load() {

        String loadTasks = String.valueOf(client.load("tasks"));
        Type taskType = new TypeToken<ArrayList<Task>>() {}.getType();
        List<Task> tasksFromJson = gson.fromJson(replace(loadTasks), taskType);
        tasksFromJson.forEach(task -> tasks.put(task.getId(), task));

        String loadEpics = String.valueOf(client.load("epics"));
        Type epicType = new TypeToken<ArrayList<Epic>>() {}.getType();
        List<Epic> epicFromJson = gson.fromJson(replace(loadEpics), epicType);
        epicFromJson.forEach(epic -> epics.put(epic.getId(), epic));

        String loadSubTasks = String.valueOf(client.load("subtasks"));
        Type subtaskType = new TypeToken<ArrayList<Subtask>>() {}.getType();
        List<Subtask> subtaskFromJson = gson.fromJson(replace(loadSubTasks), subtaskType);
        subtaskFromJson.forEach(subtask -> subtasks.put(subtask.getId(), subtask));

        String loadHistory = String.valueOf(client.load("history"));
        deserializedHistory(loadHistory);
    }

    public String replace(String s) {
        s = s.replaceAll("\\\\n", "");
        s = s.replaceAll("\\\\", "");
        s = s.substring(1, s.length() - 1);
        return s;
    }

    public void deserializedHistory(String s) {

        Type taskType = new TypeToken<ArrayList<Task>>() {}.getType();
        List<Task> fromJson = gson.fromJson(replace(s), taskType);
        List<Integer> listId = fromJson.stream().map(Task::getId).collect(Collectors.toList());
        Collections.reverse(listId);
        try {
            for (int id : listId) {
                if (tasks.containsKey(id)) {
                    getTask(id);
                } else if (epics.containsKey(id)) {
                    getEpic(id);
                } else if (subtasks.containsKey(id)) {
                    getSubtask(id);
                }
            }
        } catch (NullPointerException e) {
            throw new ManagerSaveException("Задачи с таким id нет");
        }

    }
}
