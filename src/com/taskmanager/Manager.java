package com.taskmanager;

import java.util.HashMap;

public class Manager {

    public HashMap<Integer, Task> tasks = new HashMap<>();
    public HashMap<Integer, Epic> epics = new HashMap<>();
    public HashMap<Integer, Subtask> subtasks = new HashMap<>();


    // Получение списка задач
    public void getTasks() {

        for (Task task : tasks.values()) {
            System.out.println(task);
        }
    }

    // Получение списка эпиков
    public void getEpics() {

        for (Epic epic : epics.values()) {
            System.out.println(epic);
        }
    }

    // Получение списка подзадач
    public void getSubTasks() {

        for (Subtask subtask : subtasks.values()) {
            System.out.println(subtask);
        }
    }

    // Удалить все таски
    public void deleteAllTasks() {
        tasks.clear();
    }

    // Удалить все эпики
    public void deleteAllEpics() {
        epics.clear();
    }

    // Удалить все подзадачи
    public void deleteAllSubtasks() {
        subtasks.clear();
    }

    // Получение таска по ID
    public void getTask(int id) {

        if (tasks.containsKey(id)) {
            System.out.println(tasks.get(id));
        } else {
            System.out.println("Задачи с таким ID нет в базе.");
        }
    }

    // Получение эпика по ID
    public void getEpic(int id) {

        if (epics.containsKey(id)) {
            System.out.println(epics.get(id));
        } else {
            System.out.println("Задачи с таким ID нет в базе.");
        }
    }

    // Получение подзадачи по ID
    public void getSubtask(int id) {

        if (subtasks.containsKey(id)) {
            System.out.println(subtasks.get(id));
        } else {
            System.out.println("Задачи с таким ID нет в базе.");
        }
    }

    // Создаем таск
    public void makeTask(Task task) {

        tasks.put(task.getId(), task);
    }

    // Обновление таска
    public void setTask(Integer id, Task task) {

        tasks.get(id).setTitle(task.title);
        tasks.get(id).setDescription(task.description);
    }

    // Обновление статуса таска
    public void updateTaskStatus(Task task) {

        task.setStatus(task.getStatusValue().get(2));
    }

    // Создание Эпика
    public void makeEpic(Epic epic) {

        epics.put(epic.getId(), epic);
    }

    // Обновление эпика
    public void setEpic(Integer id, Epic epic) {

        epics.get(id).setTitle(epic.title);
        epics.get(id).setDescription(epic.description);
    }

    // Создание подзадачи
    public void makeSubtask(Subtask subtask) {

        subtasks.put(subtask.getId(), subtask);
        epics.get(subtask.epicId).setEpicManager(subtask.getId(), subtask);
    }

    // Обновление подзадачи
    public void setSubtask(Integer id, Subtask subtask) {

        subtasks.get(id).setTitle(subtask.title);
        subtasks.get(id).setDescription(subtask.description);
    }

    // Обновление статуса подзадачи
    public void updateSubtask(Subtask subtask) {

        int statusId = subtask.getStatusValue().indexOf(subtask.status);
        subtask.setStatus(subtask.getStatusValue().get(++statusId));

        updateEpicStatus(epics.get(subtask.epicId));
    }

    // Обновление статуса эпика
    public void updateEpicStatus(Epic epic) {

        for (Subtask subtask : epic.epicManager.values()) {
            if (subtask.status.equals("IN_PROGRESS") || subtask.status.equals("NEW")) {
                epic.setStatus("IN_PROGRESS");
                break;
            }
            if (subtask.status.equals("DONE")) {
                epic.setStatus("DONE");
            } else {
                break;
            }
        }
    }

    // Удаление таска по ID
    public void deleteTask(int id) {

        if (tasks.containsKey(id)) {
            tasks.remove(id);
        } else {
            System.out.println("Задачи с таким ID нет в базе.\n");
        }
    }

    // Удаление эпика по ID
    public void deleteEpic(int id) {

        if (epics.containsKey(id)) {
            for (Subtask subtask : subtasks.values()) {
                if (subtask.epicId == id) {
                    subtasks.remove(subtask.getId());
                }
            }
            epics.remove(id);
        } else {
            System.out.println("Задачи с таким ID нет в базе.\n");
        }
    }

    // Удаление подзадачи по ID
    public void deleteSubtask(int id) {

        if (subtasks.containsKey(id)) {
            epics.get(subtasks.get(id).epicId).epicManager.remove(id);
            subtasks.remove(id);
        } else {
            System.out.println("Задачи с таким ID нет в базе.\n");
        }
    }

    // Получение списка подзадач
    public void printEpicSubtask(Epic epic) {

        for (Subtask subtask : epic.epicManager.values()) {
            System.out.println(subtask);
        }
    }

    // Напечатать список всех тасков
    public void printTasksList() {

        for (Task task : tasks.values()) {
            System.out.println(task);
        }
        System.out.println();
        for (Epic epic : epics.values()) {
            System.out.println(epic);
        }
        System.out.println();
        for (Subtask subtask : subtasks.values()) {
            System.out.println(subtask);
        }
        System.out.println();
    }

    // Тест
    public void printTest() {

        makeTask(new Task("Задача 1", "описание"));
        makeTask(new Task("Задача 2", "описание"));
        makeEpic(new Epic("Эпик 1", "описание"));
        makeSubtask(new Subtask("Подзадача 1", "описание", 3));
        makeSubtask(new Subtask("Подзадача 2", "описание", 3));

        makeEpic(new Epic("Эпик 2", "описание"));
        makeSubtask(new Subtask("Подзадача 3", "описание", 6));

        printTasksList();
        System.out.println("****************\n");


        updateTaskStatus(tasks.get(2));
        updateSubtask(subtasks.get(4));
        updateSubtask(subtasks.get(4));
        updateSubtask(subtasks.get(5));

        printTasksList();
        System.out.println("****************\n");

        deleteTask(2);
        deleteEpic(6);

        printTasksList();
    }
}

