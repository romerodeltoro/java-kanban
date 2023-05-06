package com.taskmanager.tasks;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Task {
    protected String title;
    protected String description;
    protected int id;
    protected static int counter;
    protected Status status;
    protected TaskType taskType = TaskType.TASK;
    protected int duration;
    protected LocalDateTime startTime;

    protected LocalDateTime endTime;

    public enum Status {
        NEW, IN_PROGRESS, DONE
    }

    public Task(String title, String description, int duration, LocalDateTime localDateTime) {
        this.title = title;
        this.description = description;
        this.id = ++counter;
        this.status = Status.NEW;
        this.duration = duration;
        this.startTime = localDateTime;
        this.endTime = getEndTime();
    }
    public Task(int id, String title, String description, int duration, LocalDateTime localDateTime) {
        this.title = title;
        this.description = description;
        this.id = id;
        this.status = Status.NEW;
        this.duration = duration;
        this.startTime = localDateTime;
        this.endTime = getEndTime();
    }

    public Task(String title, String description) {
        this.title = title;
        this.description = description;
        this.id = ++counter;
        this.status = Status.NEW;
    }
    public Task() {
        this.status = Status.NEW;
    }



    public static void setCounter(int c) {
        counter = c;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Task fromString(String value) {
        return null;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public int getDuration() {
        return duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime = startTime.plusMinutes(duration);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && duration == task.duration
                && title.equals(task.title)
                && description.equals(task.description)
                && status == task.status
                && taskType == task.taskType
                && Objects.equals(startTime, task.startTime)
                && Objects.equals(endTime, task.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, description, id, status);
    }

    @Override
    public String toString() {
        return String.format("%d,%s,%s,%s,%s,%d,%s,%s\n",
                id, taskType, title, status, description, duration,
                startTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")),
                endTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
    }
}
