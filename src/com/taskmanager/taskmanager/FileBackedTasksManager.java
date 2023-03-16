package com.taskmanager.taskmanager;

import com.taskmanager.exceptions.ManagerSaveException;
import com.taskmanager.tasks.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;


public class FileBackedTasksManager extends InMemoryTaskManager {

    private File file;

    public FileBackedTasksManager(File file) {
        this.file = file;
    }

    public static void main(String[] args) {

        test();
    }

    public static void test() {
        FileBackedTasksManager manager = new FileBackedTasksManager(new File("src/com/taskmanager/resources/saveFile.csv"));

        manager.createTask(new Task("task1", "Купить автомобиль"));
        manager.createEpic(new Epic("new Epic1", "Новый Эпик"));
        manager.createSubtask(new Subtask("New Subtask", "Подзадача", 2));
        manager.createSubtask(new Subtask("New Subtask2", "Подзадача2", 2));
        manager.getTask(1);
        manager.getEpic(2);
        manager.getSubtask(3);
        System.out.println(manager.getTasks());
        System.out.println(manager.getEpics());
        System.out.println(manager.getSubTasks());
        manager.getHistory().forEach(System.out::print);

        System.out.println("\n\n" + "new" + "\n\n");

        FileBackedTasksManager manager2 = loadFromFile(new File("src/com/taskmanager/resources/saveFile.csv"));
        System.out.println(manager2.getTasks());
        System.out.println(manager2.getEpics());
        System.out.println(manager2.getSubTasks());
        manager2.getHistory().forEach(System.out::print);
    }

    /* Записываем все таски и историю в файл, если его нет - создаем */
    public void save() {

        try {
            if (!file.exists()) {
                Files.createDirectory(Path.of("src/com/taskmanager/resources/"));
                Files.createFile(file.toPath());
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Не удалось создать файл для записи", e);
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
            throw new ManagerSaveException("Не удалось сохранить в файл", e);
        }
    }


    /* Выгрузка всех данных из файла в мапы и историю */
    public static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager manager = new FileBackedTasksManager(file);

        try (BufferedReader reader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            reader.readLine();
            while (reader.ready()) {
                String line = reader.readLine();

                if (line.isBlank()) {
                    line = reader.readLine();

                    for (Integer number : CSVTaskConverter.historyFromString(line)) {

                        if (manager.tasks.containsKey(number)) {
                            try {
                                manager.getTask(number);
                            } catch (NullPointerException e) {
                                throw new ManagerSaveException("Таска с таким ID нет в в мапе");
                            }
                        } else if (manager.epics.containsKey(number)) {
                            try {
                                manager.getEpic(number);
                            } catch (NullPointerException e) {
                                throw new ManagerSaveException("Эпика с таким ID нет в в мапе");
                            }
                        } else if (manager.subtasks.containsKey(number)) {
                            try {
                                manager.getSubtask(number);
                            } catch (NullPointerException e) {
                                throw new ManagerSaveException("Субтаска с таким ID нет в в мапе");
                            }
                        }
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
            throw new ManagerSaveException("Не удалось считать данные из файла", e);
        }
        return manager;
    }

    @Override
    public void createTask(Task task) {
        super.createTask(task);
        save();
    }

    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        save();
    }

    @Override
    public void createSubtask(Subtask subtask) {
        super.createSubtask(subtask);
        save();
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
    public void getTask(int id) {
        super.getTask(id);
        save();
    }

    @Override
    public void getEpic(int id) {
        super.getEpic(id);
        save();
    }

    @Override
    public void getSubtask(int id) {
        super.getSubtask(id);
        save();
    }


}
