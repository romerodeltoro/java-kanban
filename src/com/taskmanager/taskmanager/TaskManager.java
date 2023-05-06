package com.taskmanager.taskmanager;

import com.taskmanager.tasks.*;

import java.util.Collection;
import java.util.List;

public interface TaskManager{

    void updateSubtaskStatus(Subtask subtask);

    // Получение списка задач
    Collection<Task> getTasks();

    // Получение списка эпиков
    Collection<Epic> getEpics();

    // Получение списка подзадач
    Collection<Subtask> getSubTasks();

    // Удалить все таски
    void deleteAllTasks();

    // Удалить все эпики
    void deleteAllEpics();

    // Удалить все подзадачи
    void deleteAllSubtasks();

    // Получение таска по ID
    Task getTask(int id);

    // Получение эпика по ID
    Epic getEpic(int id);

    // Получение подзадачи по ID
    Subtask getSubtask(int id);

    // Создание таска
    Task createTask(Task task);

    // Обновление таска
    void updateTask(Integer id, Task task);

    // Создание Эпика
    Epic createEpic(Epic epic);

    // Обновление эпика
    void updateEpic(Integer id, Epic epic);

    // Создание подзадачи
    Subtask createSubtask(Subtask subtask);

    // Обновление подзадачи
    void updateSubtask(Integer id, Subtask subtask);

    // Обновление статуса эпика
    void updateEpicStatus(Epic epic);

    // Удаление таска по ID
    void deleteTask(int id);

    // Удаление эпика по ID
    void deleteEpic(int id);

    // Удаление подзадачи по ID
    void deleteSubtask(int id);

    // Получение списка подзадач
    List<Subtask> getEpicSubtask(int id);

    List<Task> getHistory();

    void printTasksList();

    List<Task> getPrioritizedTasks();

}
