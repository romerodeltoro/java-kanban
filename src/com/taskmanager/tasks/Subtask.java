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

    public Subtask(String title, String description, int epicId) {
        super(title, description);
        this.epicId = epicId;
    }

    public Integer getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return String.format("%d,%s,%s,%s,%s,%d,%s,%s,%d\n",
                id, type, title, status, description, duration,
                startTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")),
                endTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")), epicId);
    }
}
