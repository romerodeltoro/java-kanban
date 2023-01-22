package com.taskmanager;

import java.util.Collection;
import java.util.HashMap;

public class Manager {

    public HashMap<Integer, Task> tasks = new HashMap<>();
    public HashMap<Integer, Epic> epics = new HashMap<>();
    public HashMap<Integer, Subtask> subtasks = new HashMap<>();


    // ��������� ������ �����
    public Collection<Task> getTasks() {

        return tasks.values();
    }

    // ��������� ������ ������
    public void getEpics() {

        for (Epic epic : epics.values()) {
            System.out.println(epic);
        }
    }

    // ��������� ������ ��������
    public void getSubTasks() {

        for (Subtask subtask : subtasks.values()) {
            System.out.println(subtask);
        }
    }

    // ������� ��� �����
    public void deleteAllTasks() {
        tasks.clear();
    }

    // ������� ��� �����
    public void deleteAllEpics() {
        epics.clear();
    }

    // ������� ��� ���������
    public void deleteAllSubtasks() {
        subtasks.clear();
    }

    // ��������� ����� �� ID
    public void getTask(int id) {

        if (tasks.containsKey(id)) {
            System.out.println(tasks.get(id));
        } else {
            System.out.println("������ � ����� ID ��� � ����.");
        }
    }

    // ��������� ����� �� ID
    public void getEpic(int id) {

        if (epics.containsKey(id)) {
            System.out.println(epics.get(id));
        } else {
            System.out.println("������ � ����� ID ��� � ����.");
        }
    }

    // ��������� ��������� �� ID
    public void getSubtask(int id) {

        if (subtasks.containsKey(id)) {
            System.out.println(subtasks.get(id));
        } else {
            System.out.println("������ � ����� ID ��� � ����.");
        }
    }

    // ������� ����
    public void makeTask(Task task) {

        tasks.put(task.getId(), task);
    }

    // ���������� �����
    public void setTask(Integer id, Task task) {

        tasks.get(id).setTitle(task.title);
        tasks.get(id).setDescription(task.description);
    }

    // ���������� ������� �����
    public void updateTaskStatus(Task task) {

        task.setStatus(Task.Status.DONE);
    }

    // �������� �����
    public void makeEpic(Epic epic) {

        epics.put(epic.getId(), epic);
    }

    // ���������� �����
    public void setEpic(Integer id, Epic epic) {

        epics.get(id).setTitle(epic.title);
        epics.get(id).setDescription(epic.description);
    }

    // �������� ���������
    public void makeSubtask(Subtask subtask) {

        subtasks.put(subtask.getId(), subtask);
        epics.get(subtask.epicId).setEpicManager(subtask.getId(), subtask);
    }

    // ���������� ���������
    public void setSubtask(Integer id, Subtask subtask) {

        subtasks.get(id).setTitle(subtask.title);
        subtasks.get(id).setDescription(subtask.description);
    }

    // ���������� ������� ���������
    public void updateSubtask(Subtask subtask) {

        subtask.setStatus(Task.Status.DONE);

        updateEpicStatus(epics.get(subtask.epicId));
    }

    // ���������� ������� �����
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

    // �������� ����� �� ID
    public void deleteTask(int id) {

        if (tasks.containsKey(id)) {
            tasks.remove(id);
        } else {
            System.out.println("������ � ����� ID ��� � ����.\n");
        }
    }

    // �������� ����� �� ID
    public void deleteEpic(int id) {

        if (epics.containsKey(id)) {
            for (Subtask subtask : subtasks.values()) {
                if (subtask.epicId == id) {
                    subtasks.remove(subtask.getId());
                }
            }
            epics.remove(id);
        } else {
            System.out.println("������ � ����� ID ��� � ����.\n");
        }
    }

    // �������� ��������� �� ID
    public void deleteSubtask(int id) {

        if (subtasks.containsKey(id)) {
            epics.get(subtasks.get(id).epicId).subtasks.remove(id);
            subtasks.remove(id);
        } else {
            System.out.println("������ � ����� ID ��� � ����.\n");
        }
    }

    // ��������� ������ ��������
    public void printEpicSubtask(Epic epic) {

        for (Subtask subtask : epic.subtasks.values()) {
            System.out.println(subtask);
        }
    }

    // ���������� ������ ���� ������
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

