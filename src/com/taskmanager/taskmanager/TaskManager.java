package com.taskmanager.taskmanager;

import com.taskmanager.tasks.*;

import java.util.Collection;
import java.util.List;

public interface TaskManager{

    // ��������� ������ �����
    Collection<Task> getTasks();

    // ��������� ������ ������
    Collection<Epic> getEpics();

    // ��������� ������ ��������
    Collection<Subtask> getSubTasks();

    // ������� ��� �����
    void deleteAllTasks();

    // ������� ��� �����
    void deleteAllEpics();

    // ������� ��� ���������
    void deleteAllSubtasks();

    // ��������� ����� �� ID
    void getTask(int id);

    // ��������� ����� �� ID
    void getEpic(int id);

    // ��������� ��������� �� ID
    void getSubtask(int id);

    // �������� �����
    void createTask(Task task);

    // ���������� �����
    void updateTask(Integer id, Task task);

    // �������� �����
    void createEpic(Epic epic);

    // ���������� �����
    void updateEpic(Integer id, Epic epic);

    // �������� ���������
    void createSubtask(Subtask subtask);

    // ���������� ���������
    void updateSubtask(Integer id, Subtask subtask);

    // ���������� ������� �����
    void updateEpicStatus(Epic epic);

    // �������� ����� �� ID
    void deleteTask(int id);

    // �������� ����� �� ID
    void deleteEpic(int id);

    // �������� ��������� �� ID
    void deleteSubtask(int id);

    // ��������� ������ ��������
    void printEpicSubtask(Epic epic);

    List<Task> getHistory();

    void printTasksList();



}
