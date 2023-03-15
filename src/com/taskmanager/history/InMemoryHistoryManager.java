package com.taskmanager.history;

import com.taskmanager.history.HistoryManager;
import com.taskmanager.tasks.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    private Node<Task> head;
    private Node<Task> tail;
    private Map<Integer, Node> requiredTasks = new HashMap<>();


    // Добавление элемента в конец связанного списка
    public void linkLast(Task task) {
        final Node<Task> oldTail = tail;
        final Node<Task> newNode = new Node<>(oldTail, task, null);
        tail = newNode;
        if (oldTail == null) {
            head = newNode;
        } else {
            oldTail.next = newNode;
        }
        requiredTasks.put(task.getId(), newNode);
    }

    // Создание обычного списка просмотров
    public List<Task> getTasks() {
        List<Task> temporalHistory = new ArrayList<>();
        Node<Task> node = tail;
        while (node != null) {
            temporalHistory.add(node.data);
            node = node.prev;
        }
        return temporalHistory;
    }

    // Удаление элемента из мапы и связанного списка
    @Override
    public void remove(int id) {
        if (requiredTasks.containsKey(id)) {
            removeNode(requiredTasks.get(id));
            requiredTasks.remove(id);
        } else {
            System.out.println("Задачи с таким ID не было в истории.\n");
        }
    }

    // Удаление элемента из связанного списка
    public void removeNode(Node node) {
        if (node.prev == null) {
            node.next.prev = null;
            head = node.next;
        } else if (node.next == null) {
            node.prev.next = null;
            tail = node.prev;
        } else {
            node.prev.next = node.next;
            node.next.prev = node.prev;
        }
    }

    // Добавление элемента в связанный список и мапу
    @Override
    public void add(Task task) {
        if (!requiredTasks.containsKey(task.getId())) {
            linkLast(task);
        } else {
            if (requiredTasks.get(task.getId()).prev == null) {
                requiredTasks.get(task.getId()).next.prev = null;
                head = requiredTasks.get(task.getId()).next;
                linkLast(task);
            } else if (requiredTasks.get(task.getId()).next == null) {
                requiredTasks.get(task.getId()).prev.next = null;
                tail = requiredTasks.get(task.getId()).prev;
                linkLast(task);
            } else {
                requiredTasks.get(task.getId()).prev.next = requiredTasks.get(task.getId()).next;
                requiredTasks.get(task.getId()).next.prev = requiredTasks.get(task.getId()).prev;
                linkLast(task);
            }
        }
    }

    // Получение истории просмотров
    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    private static class Node<T> {
        T data;
        Node<T> next;
        Node<T> prev;

        public Node(Node<T> prev, T data, Node<T> next) {
            this.data = data;
            this.next = next;
            this.prev = prev;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node<?> node = (Node<?>) o;
            return Objects.equals(data, node.data)
                    && Objects.equals(next, node.next)
                    && Objects.equals(prev, node.prev);
        }

        @Override
        public int hashCode() {
            return Objects.hash(data, next, prev);
        }

        @Override
        public String toString() {
            return "Node{" +
                    "data=" + data +
                    '}';
        }
    }

}
