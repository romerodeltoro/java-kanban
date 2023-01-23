package com.taskmanager;

public class Task {
    protected String title;
    protected String description;
    protected int id;
    protected static int counter = 1;
    protected Status status;

    protected enum Status {
        NEW, IN_PROGRESS, DONE
    }

    public Task(String title, String description) {
        this.title = title;
        this.description = description;
        this.id = counter++;
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Task{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
