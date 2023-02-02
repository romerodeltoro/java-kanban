package com.taskmanager;

import com.taskmanager.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager{

    private List<Task> history = new ArrayList<>();

    // Добавление элемента в список просмотров
    @Override
    public void add(Task task) {
        if (history.size() >= 10) {
            history.remove(0);
            history.add(task);
        } else {
            history.add(task);
        }
    }

    // История просмотров задач
    @Override
    public List<Task> getHistory() {
        return history;
    }

}
