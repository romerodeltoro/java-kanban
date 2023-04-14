package com.taskmanager.taskmanager;

import com.taskmanager.Managers;
import com.taskmanager.history.HistoryManager;
import com.taskmanager.tasks.*;

import java.util.*;

public class InMemoryTaskManager implements TaskManager{

    protected HashMap<Integer, Task> tasks = new HashMap<>();
    protected HashMap<Integer, Epic> epics = new HashMap<>();
    protected HashMap<Integer, Subtask> subtasks = new HashMap<>();
    protected TreeSet<Task> tasksSet = new TreeSet<>(Comparator.comparing(Task::getStartTime));
    protected HistoryManager historyManager = Managers.getDefaultHistory();


    public void setHistoryManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
    }

    public void addTaskToTree(Task task) {
        if (task.getStartTime() != null) {
            tasksSet.add(task);
        } else {
            throw new RuntimeException();
        }
    }

    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(tasksSet);
    }

    public boolean checkIntersection(Task task) {
        if (getPrioritizedTasks().isEmpty()) {
            return true;
        }
        for (Task prioritizedTask : getPrioritizedTasks()) {
            if ((task.getStartTime().isAfter(prioritizedTask.getStartTime()) &&
                    task.getStartTime().isBefore(prioritizedTask.getEndTime())) ||
                    (task.getEndTime().isAfter(prioritizedTask.getStartTime()) &&
                            task.getEndTime().isBefore(prioritizedTask.getEndTime()))) {
                return false;
            } else if (task.getStartTime().equals(prioritizedTask.getStartTime()) ||
                    task.getEndTime().equals(prioritizedTask.getEndTime())) {
                return false;
            }
        }
        return true;
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
    public Task getTask(int id) {
        if (tasks.containsKey(id)) {
            historyManager.add(tasks.get(id));
            return tasks.get(id);
        } else {
            throw new NullPointerException("Задачи с таким ID нет в базе.");
        }
    }

    // Получение эпика по ID
    @Override
    public Epic getEpic(int id) {
        if (epics.containsKey(id)) {
            historyManager.add(epics.get(id));
            return epics.get(id);
        } else {
            throw new NullPointerException("Задачи с таким ID нет в базе.");

        }
    }

    // Получение подзадачи по ID
    @Override
    public Subtask getSubtask(int id) {
        if (subtasks.containsKey(id)) {
            historyManager.add(subtasks.get(id));
            return subtasks.get(id);
        } else {
            throw new NullPointerException("Задачи с таким ID нет в базе.");
        }
    }

    // Создание таска
    @Override
    public Task createTask(Task task) {
        if (checkIntersection(task)) {
            tasks.put(task.getId(), task);
            addTaskToTree(task);
        } else {
            throw new RuntimeException("Время выволнения задачи пересекается с уже существующими");
        }
        return task;
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
    public Epic createEpic(Epic epic) {
        epics.put(epic.getId(), epic);
        return epic;
    }

    // Обновление эпика
    @Override
    public void updateEpic(Integer id, Epic epic) {
        epics.get(id).setTitle(epic.getTitle());
        epics.get(id).setDescription(epic.getDescription());
    }

    // Создание подзадачи
    @Override
    public Subtask createSubtask(Subtask subtask) {
        if (checkIntersection(subtask)) {
            subtasks.put(subtask.getId(), subtask);
            epics.get(subtask.getEpicId()).addSubtask(subtask.getId(), subtask);
            epics.get(subtask.getEpicId()).setEpicStartEndTime();
            epics.get(subtask.getEpicId()).setEpicDuration();
            addTaskToTree(subtask);
            return subtask;
        } else {
            throw new RuntimeException("Время выволнения задачи пересекается с уже существующими");
        }

    }

    // Обновление подзадачи
    @Override
    public void updateSubtask(Integer id, Subtask subtask) {
        subtasks.get(id).setTitle(subtask.getTitle());
        subtasks.get(id).setDescription(subtask.getDescription());
        updateSubtaskStatus(subtasks.get(id));
    }

    @Override
    public void updateSubtaskStatus(Subtask subtask) {
        if (subtask.getStatus() == Task.Status.NEW) {
            subtask.setStatus(Task.Status.IN_PROGRESS);
        } else if (subtask.getStatus() == Task.Status.IN_PROGRESS) {
            subtask.setStatus(Task.Status.DONE);
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
            tasksSet.remove(tasks.get(id));
            tasks.remove(id);
            historyManager.remove(id);
        } else {
            throw new RuntimeException("Задачи с таким ID нет в базе.\n");
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
//            tasksSet.remove(epics.get(id));
            epics.remove(id);
            historyManager.remove(id);
        } else {
            throw new RuntimeException("Эпика с таким ID нет в базе.\n");
        }
    }

    // Удаление подзадачи по ID
    @Override
    public void deleteSubtask(int id) {
        if (subtasks.containsKey(id)) {
            tasksSet.remove(subtasks.get(id));
            epics.get(subtasks.get(id).getEpicId()).getSubtasks().remove(id);
            subtasks.remove(id);
            historyManager.remove(id);
        } else {
            throw new RuntimeException("Подзадачи с таким ID нет в базе.\n");
        }
    }

    // Получение списка подзадач
    @Override
    public void printEpicSubtask(Epic epic) {
        epic.getSubtasks().values().forEach(System.out::println);
    }

    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    // Напечатать список всех тасков
    @Override
    public  void printTasksList() {
        tasks.values().forEach(System.out::println);
        epics.values().forEach(System.out::println);
        subtasks.values().forEach(System.out::println);
    }
}
