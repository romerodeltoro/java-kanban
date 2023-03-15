package com.taskmanager.tasks;

public class Task {
    protected String title;
    protected String description;
    protected int id;
    protected static int counter;
    protected Status status;
    protected TaskType type = TaskType.TASK;

    public enum Status {
        NEW, IN_PROGRESS, DONE
    }

    public Task(String title, String description) {
        this.title = title;
        this.description = description;
        this.id = ++counter;
        this.status = Status.NEW;
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


    @Override
    public String toString() {
        return String.format("%d,%s,%s,%s,%s,\n", getId(), type, getTitle(), getStatus(), getDescription());
    }
}
