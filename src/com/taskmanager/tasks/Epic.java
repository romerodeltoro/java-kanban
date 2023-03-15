package com.taskmanager.tasks;

import java.util.HashMap;

public class Epic extends Task {

    protected TaskType type = TaskType.EPIC;

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
        return String.format("%d,%s,%s,%s,%s,\n", getId(), type, getTitle(), getStatus(), getDescription());
    }
}
