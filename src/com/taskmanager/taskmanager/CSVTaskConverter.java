package com.taskmanager.taskmanager;

import com.taskmanager.exceptions.ManagerSaveException;
import com.taskmanager.history.HistoryManager;
import com.taskmanager.tasks.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class CSVTaskConverter {




    /* ������� �� ������ � ������� ���� */
    public static Task taskFromString(String value) {
        String[] fields = value.split(",");
        int id = Integer.parseInt(fields[0]);
        String title = fields[2];
        String description = fields[4];
        Task.Status status = Task.Status.valueOf(fields[3]);

        Task task = new Task(title, description);
        task.setId(id);
        task.setStatus(status);
        return task;
    }

    /* ������� �� ������ � ������� ���� */
    public static Epic epicFromString(String value) {
        String[] fields = value.split(",");
        int id = Integer.parseInt(fields[0]);
        String title = fields[2];
        String description = fields[4];
        Task.Status status = Task.Status.valueOf(fields[3]);

        Epic epic = new Epic(title, description);
        epic.setId(id);
        epic.setStatus(status);
        return epic;
    }

    /* ������� �� ������ � ������� ������� */
    public static Subtask subtaskFromString(String value) {
        String[] fields = value.split(",");
        int id = Integer.parseInt(fields[0]);
        String title = fields[2];
        String description = fields[4];
        Task.Status status = Task.Status.valueOf(fields[3]);

        Subtask subtask = new Subtask(title, description, Integer.parseInt(fields[5]));
        subtask.setId(id);
        subtask.setStatus(status);
        return subtask;
    }

    /* ����������� ������� � ������ */
    public static String historyToString(HistoryManager manager) {

        StringBuilder builder = new StringBuilder("");
        try {
            for (Task task : manager.getHistory()) {
                builder.append(String.valueOf(task.getId()) + ",");
            }
            return builder.toString();
        } catch (NullPointerException e) {
            throw new ManagerSaveException("������� ���������� �����");
        }
    }

    /* ������� String-���� ������� �� ����� � List<Integer> */
    public static List<Integer> historyFromString(String value) {
        List<Integer> history = new ArrayList<Integer>();
        for (String number : value.split(",")) {
            history.add(Integer.parseInt(number));
        }
        Collections.reverse(history);
        return history;
    }

    /* ������ ��������� ����� */
    public static String getHeader() {
        return "id,type,name,status,description,epic\n";
    }
}
