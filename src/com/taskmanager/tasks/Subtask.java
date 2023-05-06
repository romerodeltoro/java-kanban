package com.taskmanager.tasks;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Subtask extends Task{

    protected int epicId;

    protected TaskType type = TaskType.SUBTASK;

    public Subtask(String title, String description, int epicId, int duration, LocalDateTime localDateTime) {
        super(title, description, duration, localDateTime);
        this.epicId = epicId;
    }

    public Subtask(int id, String title, String description, int epicId, int duration, LocalDateTime localDateTime) {
        super(title, description, duration, localDateTime);
        this.epicId = epicId;
        this.id = id;
    }

    public Subtask(String title, String description, int duration, LocalDateTime localDateTime) {
        super(title, description, duration, localDateTime);
    }

    public Subtask(String title, String description, int epicId) {
        super(title, description);
        this.epicId = epicId;
    }
    public Subtask() {
    }


    public Integer getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return String.format("%d,%s,%s,%s,%s,%d,%s,%s,%d\n",
                id, type, title, status, description, duration,
                startTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")),
                endTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")), epicId);
    }
}
