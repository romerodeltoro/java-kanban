package com.taskmanager.taskmanager;

import com.taskmanager.tasks.*;

import java.util.Collection;
import java.util.List;

public interface TaskManager{

    void updateSubtaskStatus(Subtask subtask);

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
    List<Subtask> getEpicSubtask(int id);

    List<Task> getHistory();

    void printTasksList();

    List<Task> getPrioritizedTasks();

}
