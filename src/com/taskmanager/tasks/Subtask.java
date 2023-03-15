package com.taskmanager.tasks;

public class Subtask extends Task{

    protected int epicId;

    protected TaskType type = TaskType.SUBTASK;

    public Subtask(String title, String description, int epicId) {
        super(title, description);
        this.epicId = epicId;
    }

    public Integer getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return String.format("%d,%s,%s,%s,%s,%d\n", getId(), type, getTitle(), getStatus(), getDescription(), getEpicId());
    }
}
