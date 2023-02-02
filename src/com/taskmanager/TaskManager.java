package com.taskmanager;

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
    Task getTask(int id);

    // ��������� ����� �� ID
    Epic getEpic(int id);

    // ��������� ��������� �� ID
    Subtask getSubtask(int id);

    // �������� �����
    Task createTask(Task task);

    // ���������� �����
    void updateTask(Integer id, Task task);

    // �������� �����
    Epic createEpic(Epic epic);

    // ���������� �����
    void updateEpic(Integer id, Epic epic);

    // �������� ���������
    Subtask createSubtask(Subtask subtask);

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

    List<Task> history();

    void printTasksList();



}
