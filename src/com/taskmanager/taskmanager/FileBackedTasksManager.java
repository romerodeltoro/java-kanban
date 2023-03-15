package com.taskmanager.taskmanager;

import com.taskmanager.ManagerSaveException;
import com.taskmanager.history.HistoryManager;
import com.taskmanager.tasks.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class FileBackedTasksManager extends InMemoryTaskManager {

    private static File file;

    private HistoryManager historyManager = getHistoryManager();

    private static Map<Integer, Task> restoredTasks = new HashMap<>();

    public FileBackedTasksManager(File file) {

        this.file = file;
    }

    public static void main(String[] args) {

        test();

        FileBackedTasksManager manager = loadFromFile(new File("src/com/taskmanager/resources/saveFile.csv"));
        manager.createTask(new Task("Task3", "Description"));
    }

    public static void test() {
        FileBackedTasksManager manager = new FileBackedTasksManager(new File("src/com/taskmanager/resources/saveFile.csv"));

        manager.createTask(new Task("Task", "Description"));
        manager.createEpic(new Epic("Эпик 1", "описание"));
        manager.createSubtask(new Subtask("Подзадача 1", "описание", 2));
        manager.updateTask(1, new Task("Task", "Description_UPDATE"));
        manager.createTask(new Task("Task2", "Description"));
        manager.getTask(1);
        manager.getTask(5);
        manager.getEpic(2);
        manager.getSubtask(3);
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
            writer.write(historyToString(historyManager));

        } catch (IOException e) {
            throw new ManagerSaveException("Не удалось сохранить в файл", e);
        }
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

    /* Выгрузка всех данных из файла в мапы и историю */
    public static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager manager = new FileBackedTasksManager(file);

        try (BufferedReader reader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            reader.readLine();
            while (reader.ready()) {
                String line = reader.readLine();

                if (line.isBlank()) {
                    line = reader.readLine();
                    for (Integer number : historyFromString(line)) {
                        manager.addTaskToHistory(number);
                    }
                    break;
                }

                String[] fields = line.split(",");
                TaskType type = TaskType.valueOf(fields[1]);

                switch (type) {
                    case TASK:
                        manager.createTask((Task) CSVTaskConverter.fromString(line));
                        manager.restoredTasks.put(((Task) CSVTaskConverter.fromString(line)).getId(), (Task) CSVTaskConverter.fromString(line));
                        break;
                    case EPIC:
                        manager.createEpic((Epic) CSVTaskConverter.fromString(line));
                        manager.restoredTasks.put(((Epic) CSVTaskConverter.fromString(line)).getId(), (Epic) CSVTaskConverter.fromString(line));
                        break;
                    case SUBTASK:
                        manager.createSubtask((Subtask) CSVTaskConverter.fromString(line));
                        manager.restoredTasks.put(((Subtask) CSVTaskConverter.fromString(line)).getId(), (Subtask) CSVTaskConverter.fromString(line));
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

    /* Парсинг String-овой истории из файла в List<Integer> */
    public static List<Integer> historyFromString(String value) {
        List<Integer> history = new ArrayList<Integer>();
        for (String number : value.split(",")) {
            history.add(Integer.parseInt(number));
        }
        Collections.reverse(history);
        return history;
    }


    /* Добавление восстановленных из файла таксов в историю */
    public void addTaskToHistory(int id) {

        if (restoredTasks.containsKey(id)) {
            historyManager.add(restoredTasks.get(id));
        } else {
            System.out.println("Задачи с таким ID нет в мапе.");
        }
        save();
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
