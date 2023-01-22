package com.taskmanager;

import java.util.HashMap;

public class Epic extends Task{

    protected HashMap<Integer, Subtask> epicManager = new HashMap<>();

    public Epic(String title, String description) {
        super(title, description);
    }

    public HashMap<Integer, Subtask> getEpicManager() {
        return epicManager;
    }

    public void setEpicManager(Integer id, Subtask subtask) {
        this.epicManager.put(id, subtask);
    }

    @Override
    public String toString() {
        return "Epic{" +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status='" + status + '\'' +
                ", epicManager=\n\t" + epicManager +
                '}';
    }
}
