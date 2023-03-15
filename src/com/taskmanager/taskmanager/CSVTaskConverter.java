package com.taskmanager.taskmanager;

import com.taskmanager.tasks.*;


public class CSVTaskConverter {

    /* Достаем из строки и делим по типам таски */
    public static Object fromString(String value) {
        String[] fields = value.split(",");
        TaskType type = TaskType.valueOf(fields[1]);
        int id = Integer.parseInt(fields[0]);
        String title = fields[2];
        String description = fields[4];
        Task.Status status = Task.Status.valueOf(fields[3]);

        if (type.equals(TaskType.TASK)) {
            Task task = new Task(title, description);
            task.setId(id);
            task.setStatus(status);
            return task;
        }
        if (type.equals(TaskType.EPIC)) {
            Epic epic = new Epic(title, description);
            epic.setId(id);
            epic.setStatus(status);
            return epic;
        }
        if (type.equals(TaskType.SUBTASK)) {
            Subtask subtask = new Subtask(title, description, Integer.parseInt(fields[5]));
            subtask.setId(id);
            subtask.setStatus(status);
            return subtask;
        }
        return null;
    }

    /* Шаблон заголовка файла */
    public static String getHeader() {
        return "id,type,name,status,description,epic\n";
    }
}
