package com.taskmanager.taskmanager;

import com.taskmanager.Managers;
import com.taskmanager.history.HistoryManager;
import com.taskmanager.tasks.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class InMemoryTaskManager implements TaskManager{

    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();
    protected HistoryManager historyManager = Managers.getDefaultHistory();

   public HistoryManager getHistoryManager() {
        return this.historyManager;
    }


    // Получение списка задач
    @Override
    public Collection<Task> getTasks() {
        return tasks.values();
    }

    // Получение списка эпиков
    @Override
    public Collection<Epic> getEpics() {
        return epics.values();
    }

    // Получение списка подзадач
    @Override
    public Collection<Subtask> getSubTasks() {
        return subtasks.values();
    }

    // Удалить все таски
    @Override
    public void deleteAllTasks() {
        tasks.clear();
    }

    // Удалить все эпики
    @Override
    public void deleteAllEpics() {
        epics.clear();
    }

    // Удалить все подзадачи
    @Override
    public void deleteAllSubtasks() {
        subtasks.clear();
    }

    // Получение таска
    @Override
    public void getTask(int id) {
        if (tasks.containsKey(id)) {
            historyManager.add(tasks.get(id));

        } else {
            System.out.println("Задачи с таким ID нет в базе.");

        }
    }

    // Получение эпика по ID
    @Override
    public void getEpic(int id) {
        if (epics.containsKey(id)) {
            historyManager.add(epics.get(id));

        } else {
            System.out.println("Задачи с таким ID нет в базе.");

        }
    }

    // Получение подзадачи по ID
    @Override
    public void getSubtask(int id) {
        if (subtasks.containsKey(id)) {
            historyManager.add(subtasks.get(id));

        } else {
            System.out.println("Задачи с таким ID нет в базе.");

        }
    }

    // Создание таска
    @Override
    public void createTask(Task task) {
        tasks.put(task.getId(), task);

    }

    // Обновление таска
    @Override
    public void updateTask(Integer id, Task task) {
        tasks.get(id).setTitle(task.getTitle());
        tasks.get(id).setDescription(task.getDescription());
        if (tasks.get(id).getStatus() == Task.Status.NEW) {
            tasks.get(id).setStatus(Task.Status.IN_PROGRESS);
        } else if (tasks.get(id).getStatus() == Task.Status.IN_PROGRESS) {
            tasks.get(id).setStatus(Task.Status.DONE);
        }
    }

    // Создание Эпика
    @Override
    public void createEpic(Epic epic) {
        epics.put(epic.getId(), epic);

    }

    // Обновление эпика
    @Override
    public void updateEpic(Integer id, Epic epic) {
        epics.get(id).setTitle(epic.getTitle());
        epics.get(id).setDescription(epic.getDescription());
    }

    // Создание подзадачи
    @Override
    public void createSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
        epics.get(subtask.getEpicId()).addSubtask(subtask.getId(), subtask);

    }

    // Обновление подзадачи
    @Override
    public void updateSubtask(Integer id, Subtask subtask) {
        subtasks.get(id).setTitle(subtask.getTitle());
        subtasks.get(id).setDescription(subtask.getDescription());
        if (subtasks.get(id).getStatus() == Task.Status.NEW) {
            subtasks.get(id).setStatus(Task.Status.IN_PROGRESS);
        } else if (subtasks.get(id).getStatus() == Task.Status.IN_PROGRESS) {
            subtasks.get(id).setStatus(Task.Status.DONE);
        }
        updateEpicStatus(epics.get(subtask.getEpicId()));
    }

    // Обновление статуса эпика
    @Override
    public void updateEpicStatus(Epic epic) {
        for (Subtask subtask : epic.getSubtasks().values()) {
            if (subtask.getStatus() == Task.Status.IN_PROGRESS || subtask.getStatus() == Task.Status.NEW) {
                epic.setStatus(Task.Status.IN_PROGRESS);
                break;
            }
            if (subtask.getStatus() == Task.Status.DONE) {
                epic.setStatus(Task.Status.DONE);
            } else {
                break;
            }
        }
    }

    // Удаление таска по ID
    @Override
    public void deleteTask(int id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
            historyManager.remove(id);
        } else {
            System.out.println("Задачи с таким ID нет в базе.\n");
        }
    }

    // Удаление эпика по ID
    @Override
    public void deleteEpic(int id) {
        if (epics.containsKey(id)) {
            Iterator<Subtask> iterator = subtasks.values().iterator();
            while(iterator.hasNext()) {
                Subtask subtask = iterator.next();
                if (subtask.getEpicId() == id) {
                    iterator.remove();
                    historyManager.remove(subtask.getId());
                }
            }
            epics.remove(id);
            historyManager.remove(id);
        } else {
            System.out.println("Задачи с таким ID нет в базе.\n");
        }
    }

    // Удаление подзадачи по ID
    @Override
    public void deleteSubtask(int id) {
        if (subtasks.containsKey(id)) {
            epics.get(subtasks.get(id).getEpicId()).getSubtasks().remove(id);
            subtasks.remove(id);
            historyManager.remove(id);
        } else {
            System.out.println("Задачи с таким ID нет в базе.\n");
        }
    }

    // Получение списка подзадач
    @Override
    public void printEpicSubtask(Epic epic) {
        for (Subtask subtask : epic.getSubtasks().values()) {
            System.out.println(subtask);
        }
    }

    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    // Напечатать список всех тасков
    @Override
    public  void printTasksList() {

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
