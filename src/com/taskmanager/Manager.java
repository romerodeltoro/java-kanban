package com.taskmanager;

import java.util.Collection;
import java.util.HashMap;

public class Manager {

    public HashMap<Integer, Task> tasks = new HashMap<>();
    public HashMap<Integer, Epic> epics = new HashMap<>();
    public HashMap<Integer, Subtask> subtasks = new HashMap<>();


    // Получение списка задач
    public Collection<Task> getTasks() {
        return tasks.values();
    }

    // Получение списка эпиков
    public Collection<Epic> getEpics() {
        return epics.values();
    }

    // Получение списка подзадач
    public Collection<Subtask> getSubTasks() {
        return subtasks.values();
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
    public Task getTask(int id) {

        if (tasks.containsKey(id)) {
            return tasks.get(id);
        } else {
            System.out.println("Задачи с таким ID нет в базе.");
            return null;
        }
    }

    // Получение эпика по ID
    public Epic getEpic(int id) {

        if (epics.containsKey(id)) {
            return epics.get(id);
        } else {
            System.out.println("Задачи с таким ID нет в базе.");
            return null;
        }
    }

    // Получение подзадачи по ID
    public Subtask getSubtask(int id) {

        if (subtasks.containsKey(id)) {
            return subtasks.get(id);
        } else {
            System.out.println("Задачи с таким ID нет в базе.");
            return null;
        }
    }

    // Создаем таск
    public Task createTask(Task task) {

        tasks.put(task.getId(), task);
        return task;
    }

    // Обновление таска
    public void updateTask(Integer id, Task task) {

        tasks.get(id).setTitle(task.title);
        tasks.get(id).setDescription(task.description);
        if (tasks.get(id).getStatus() == Task.Status.NEW) {
            tasks.get(id).setStatus(Task.Status.IN_PROGRESS);
        } else if (tasks.get(id).getStatus() == Task.Status.IN_PROGRESS) {
            tasks.get(id).setStatus(Task.Status.DONE);
        }
    }


    // Создание Эпика
    public Epic createEpic(Epic epic) {

        epics.put(epic.getId(), epic);
        return epic;
    }

    // Обновление эпика
    public void updateEpic(Integer id, Epic epic) {

        epics.get(id).setTitle(epic.title);
        epics.get(id).setDescription(epic.description);
    }

    // Создание подзадачи
    public Subtask createSubtask(Subtask subtask) {

        subtasks.put(subtask.getId(), subtask);
        epics.get(subtask.epicId).addSubtask(subtask.getId(), subtask);
        return subtask;
    }

    // Обновление подзадачи
    public void updateSubtask(Integer id, Subtask subtask) {

        subtasks.get(id).setTitle(subtask.title);
        subtasks.get(id).setDescription(subtask.description);
        if (subtasks.get(id).getStatus() == Task.Status.NEW) {
            subtasks.get(id).setStatus(Task.Status.IN_PROGRESS);
        } else if (subtasks.get(id).getStatus() == Task.Status.IN_PROGRESS) {
            subtasks.get(id).setStatus(Task.Status.DONE);
        }

        updateEpicStatus(epics.get(subtask.epicId));
    }

    // Обновление статуса эпика
    private void updateEpicStatus(Epic epic) {

        for (Subtask subtask : epic.subtasks.values()) {
            if (subtask.status == Task.Status.IN_PROGRESS || subtask.status == Task.Status.NEW) {
                epic.setStatus(Task.Status.IN_PROGRESS);
                break;
            }
            if (subtask.status == Task.Status.DONE) {
                epic.setStatus(Task.Status.DONE);
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
            epics.get(subtasks.get(id).epicId).subtasks.remove(id);
            subtasks.remove(id);
        } else {
            System.out.println("Задачи с таким ID нет в базе.\n");
        }
    }

    // Получение списка подзадач
    public void printEpicSubtask(Epic epic) {

        for (Subtask subtask : epic.subtasks.values()) {
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
}

