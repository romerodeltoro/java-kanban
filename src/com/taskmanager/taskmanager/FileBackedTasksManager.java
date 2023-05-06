package com.taskmanager.taskmanager;

import com.taskmanager.exceptions.ManagerSaveException;
import com.taskmanager.tasks.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.HashMap;


public class FileBackedTasksManager extends InMemoryTaskManager {

    private File file;

    public FileBackedTasksManager(File file) {
        this.file = file;
    }


    /* ���������� ��� ����� � ������� � ����, ���� ��� ��� - ������� */
    public void save() {

        try {
            if (!file.exists()) {
                Files.createDirectory(Path.of("src/com/taskmanager/resources/"));
                Files.createFile(file.toPath());
            }
        } catch (IOException e) {
            throw new ManagerSaveException("�� ������� ������� ���� ��� ������", e);
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8))) {

            writer.write(CSVTaskConverter.getHeader());
            for (Task task : getTasks()) {
                writer.write(task.toString());
            }
            for (Epic epic : getEpics()) {
                writer.write(epic.toString());
            }
            for (Subtask subtask : getSubTasks()) {
                writer.write(subtask.toString());
            }
            writer.newLine();
            writer.write(CSVTaskConverter.historyToString(historyManager));

        } catch (IOException e) {
            throw new ManagerSaveException("�� ������� ��������� � ����", e);
        }
    }


    /* �������� ���� ������ �� ����� � ���� � ������� */
    public static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager manager = new FileBackedTasksManager(file);

        try (BufferedReader reader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            reader.readLine();
            while (reader.ready()) {
                String line = reader.readLine();

                if (line.isBlank()) {
                    line = reader.readLine();

                    try {
                        for (Integer number : CSVTaskConverter.historyFromString(line)) {

                            if (manager.tasks.containsKey(number)) {
                                try {
                                    manager.getTask(number);
                                } catch (NullPointerException e) {
                                    throw new ManagerSaveException("����� � ����� ID ��� � � ����");
                                }
                            } else if (manager.epics.containsKey(number)) {
                                try {
                                    manager.getEpic(number);
                                } catch (NullPointerException e) {
                                    throw new ManagerSaveException("����� � ����� ID ��� � � ����");
                                }
                            } else if (manager.subtasks.containsKey(number)) {
                                try {
                                    manager.getSubtask(number);
                                } catch (NullPointerException e) {
                                    throw new ManagerSaveException("�������� � ����� ID ��� � � ����");
                                }
                            }
                        }
                    } catch (RuntimeException e) {

                    }
                    break;
                }

                String[] fields = line.split(",");
                TaskType type = TaskType.valueOf(fields[1]);

                switch (type) {
                    case TASK:
                        manager.createTask(CSVTaskConverter.taskFromString(line));
                        break;
                    case EPIC:
                        manager.createEpic(CSVTaskConverter.epicFromString(line));
                        break;
                    case SUBTASK:
                        manager.createSubtask(CSVTaskConverter.subtaskFromString(line));
                        break;
                    default:
                        break;
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("�� ������� ������� ������ �� �����", e);
        }
        return manager;
    }

    @Override
    public Task createTask(Task task) {
        super.createTask(task);
        save();
        return task;
    }

    @Override
    public Epic createEpic(Epic epic) {
        super.createEpic(epic);
        save();
        return epic;
    }

    @Override
    public Subtask createSubtask(Subtask subtask) {
        super.createSubtask(subtask);
        save();
        return subtask;
    }

    @Override
    public void updateTask(Integer id, Task task) {
        super.updateTask(id, task);
        save();
    }

    @Override
    public void updateEpic(Integer id, Epic epic) {
        super.updateEpic(id, epic);
        save();
    }

    @Override
    public void updateSubtask(Integer id, Subtask subtask) {
        super.updateSubtask(id, subtask);
        save();
    }

    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
        save();
    }


    @Override
    public void deleteEpic(int id) {
        super.deleteEpic(id);
        save();
    }


    @Override
    public void deleteSubtask(int id) {
        super.deleteSubtask(id);
        save();
    }

    @Override
    public Task getTask(int id) {
        super.getTask(id);
        save();
        return tasks.get(id);
    }

    @Override
    public Epic getEpic(int id) {
        super.getEpic(id);
        save();
        return epics.get(id);
    }

    @Override
    public Subtask getSubtask(int id) {
        super.getSubtask(id);
        save();
        return subtasks.get(id);
    }

    @Override
    public void deleteAllTasks() {
        tasks.clear();
        save();
    }

    @Override
    public void deleteAllEpics() {
        epics.clear();
        save();
    }

    @Override
    public void deleteAllSubtasks() {
        epics.values().stream().map(Epic::getSubtasks).forEach(HashMap::clear);
        subtasks.clear();
        save();
    }

}
