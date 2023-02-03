package com.taskmanager;

import com.taskmanager.tasks.Task;

import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager{

    private LinkedList<Task> history = new LinkedList<>();

    // ���������� �������� � ������ ����������
    @Override
    public void add(Task task) {
        if (history.size() >= 10) {
            history.removeFirst();
            history.add(task);
        } else {
            history.add(task);
        }
    }

    // ������� ���������� �����
    @Override
    public List<Task> getHistory() {
        return history;
    }

}
