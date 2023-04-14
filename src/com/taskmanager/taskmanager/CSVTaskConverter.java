package com.taskmanager.taskmanager;

import com.taskmanager.exceptions.ManagerSaveException;
import com.taskmanager.history.HistoryManager;
import com.taskmanager.tasks.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class CSVTaskConverter {




    /* Достаем из строки и создаем таск */
    public static Task taskFromString(String value) {
        String[] fields = value.split(",");
        int id = Integer.parseInt(fields[0]);
        String title = fields[2];
        String description = fields[4];
        int duration = Integer.parseInt(fields[5]);
        LocalDateTime startTime = LocalDateTime.parse(fields[6], DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
        LocalDateTime endTime = LocalDateTime.parse(fields[7], DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
        Task.Status status = Task.Status.valueOf(fields[3]);

        Task task = new Task(title, description, duration, startTime);
        task.setId(id);
        task.setStatus(status);
        task.setDuration(duration);
        task.setStartTime(startTime);
        return task;
    }

    /* Достаем из строки и создаем эпик */
    public static Epic epicFromString(String value) {
        String[] fields = value.split(",");
        int id = Integer.parseInt(fields[0]);
        String title = fields[2];
        String description = fields[4];
        int duration = Integer.parseInt(fields[5]);

        Task.Status status = Task.Status.valueOf(fields[3]);
        LocalDateTime startTime = null;

        if (!fields[6].equals("null")) {
            startTime = LocalDateTime.parse(fields[6], DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
        } else {
            startTime = null;
        }
        if (!fields[7].equals("null")) {
            LocalDateTime endTime = LocalDateTime.parse(fields[7], DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
        } else {
            LocalDateTime endTime = null;
        }

        Epic epic = new Epic(title, description);
        epic.setId(id);
        epic.setStatus(status);
        epic.setDuration(duration);
        epic.setStartTime(startTime);
        return epic;
    }

    /* Достаем из строки и создаем субтаск */
    public static Subtask subtaskFromString(String value) {
        String[] fields = value.split(",");
        int id = Integer.parseInt(fields[0]);
        String title = fields[2];
        String description = fields[4];
        Task.Status status = Task.Status.valueOf(fields[3]);
        int duration = Integer.parseInt(fields[5]);
        LocalDateTime startTime = LocalDateTime.parse(fields[6], DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
        LocalDateTime endTime = LocalDateTime.parse(fields[7], DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));

        Subtask subtask = new Subtask(title, description, Integer.parseInt(fields[8]));
        subtask.setId(id);
        subtask.setStatus(status);
        subtask.setDuration(duration);
        subtask.setStartTime(startTime);
        return subtask;
    }

    /* Преобразуем историю в строку */
    public static String historyToString(HistoryManager manager) {

        StringBuilder builder = new StringBuilder("");
        try {
            for (Task task : manager.getHistory()) {
                builder.append(String.valueOf(task.getId()) + ",");
            }
            return builder.toString();
        } catch (NullPointerException e) {
            throw new ManagerSaveException("История просмотров пуста");
        }
    }

    /* Парсинг String-овой истории из файла в List<Integer> */
    public static List<Integer> historyFromString(String value) {
        List<Integer> history = new ArrayList<Integer>();

        for (String number : value.split(",")) {
            history.add(Integer.parseInt(number));
        }
        Collections.reverse(history);
        return history;
    }

    /* Шаблон заголовка файла */
    public static String getHeader() {
        return "id,type,name,status,description,duration,start,end,epic\n";
    }
}
