package com.taskmanager;

import java.util.HashMap;

public class Epic extends Task {

    protected HashMap<Integer, Subtask> subtasks = new HashMap<>();

    public Epic(String title, String description) {
        super(title, description);
    }

    public HashMap<Integer, Subtask> getSubtasks() {
        return subtasks;
    }

    public void addSubtask(Integer id, Subtask subtask) {
        this.subtasks.put(id, subtask);
    }

    @Override
    public String toString() {
        return "Epic{" +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", id='" + id + '\'' +
                ", status='" + status + '\'' +
                ", subtasks=\n\t" + subtasks +
                '}';
    }
}
