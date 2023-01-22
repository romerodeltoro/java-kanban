package com.taskmanager;

import java.util.Arrays;
import java.util.List;

public class Task {
    protected String title;
    protected String description;
    protected int id;
    static int counter = 1;
    protected String status;
    protected final List<String> statusValue = Arrays.asList("NEW", "IN_PROGRESS", "DONE");

    public Task(String title, String description) {
        this.title = title;
        this.description = description;
        this.id = counter++;
        this.status = this.statusValue.get(0);
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getStatusValue() {
        return statusValue;
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
