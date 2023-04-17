package com.taskmanager.tasks;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class Epic extends Task {

    protected TaskType type = TaskType.EPIC;
    protected LocalDateTime endTime;

    protected HashMap<Integer, Subtask> subtasks = new HashMap<>();

    public Epic(String title, String description) {
        super(title, description);
        setEpicStartEndTime();
        setEpicDuration();

    }

    public HashMap<Integer, Subtask> getSubtasks() {
        return subtasks;
    }

    public void addSubtask(Integer id, Subtask subtask) {
        this.subtasks.put(id, subtask);
        subtask.setEpicId(getId());
    }

    public void setEpicStartEndTime() {
        startTime = LocalDateTime.MAX;
        endTime = LocalDateTime.MIN;

        if (!subtasks.isEmpty()) {

            for (Subtask subtask : subtasks.values()) {
                if (subtask.getStartTime().isBefore(startTime)) {
                    startTime = subtask.getStartTime();
                }
                if (subtask.getEndTime().isAfter(endTime)) {
                    endTime = subtask.getEndTime();
                }
            }
        } else {
            startTime = null;
            endTime = null;
        }
    }

    public void setEpicDuration() {
        duration = 0;
        if (!subtasks.isEmpty()) {
            for (Subtask subtask : subtasks.values()) {
                duration += subtask.getDuration();
            }
        }
    }

    @Override
    public String toString() {
        return String.format("%d,%s,%s,%s,%s,%d,%s,%s\n",
                id, type, title, status, description, duration,
                startTime == null ? null : startTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")),
                endTime == null ? null : endTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
    }
}
