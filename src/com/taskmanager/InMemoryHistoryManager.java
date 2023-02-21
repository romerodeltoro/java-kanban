package com.taskmanager;

import com.taskmanager.tasks.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager{

    private LinkedList<Task> history = new LinkedList<>();

    private Node<Task> head;
    private Node<Task> tail;
    private Map<Integer, Node> requiredTasks = new HashMap<>();


    public void linkLast (Task task) {
        final Node<Task> oldTail = tail;
        final Node<Task> newNode = new Node<>(oldTail, task, null);
        tail = newNode;
        if (oldTail == null)
            head = newNode;
        else
            oldTail.next = newNode;
        requiredTasks.put(task.getId(), newNode);
        history.add(task);
    }

    public List<Task> getTasks() {
        List<Task> actualHistory = new ArrayList<>();

        return null;
    }

    public void remove (int id) {

    }

    public void removeNode(Node node) {

    }

    // Добавление элемента в список просмотров
    @Override
    public void add(Task task) {
        if (!requiredTasks.containsKey(task.getId())) {
            linkLast(task);
        } else {
            if (requiredTasks.get(task.getId()).prev == null) {
                requiredTasks.get(task.getId()).next.prev = null;
                head = requiredTasks.get(task.getId()).next;
                history.remove(task);
                linkLast(task);
            } else if (requiredTasks.get(task.getId()).next == null) {
                requiredTasks.get(task.getId()).prev.next = null;
                tail = requiredTasks.get(task.getId()).prev;
                history.remove(task);
                linkLast(task);
            } else {
                requiredTasks.get(task.getId()).prev.next = requiredTasks.get(task.getId()).next;
                requiredTasks.get(task.getId()).next.prev = requiredTasks.get(task.getId()).prev;
                history.remove(task);
                linkLast(task);
            }
        }
    }

    // История просмотров задач
    @Override
    public LinkedList<Task> getHistory() {
        return history;
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
        public String toString() {
            return "Node{" +
                    "data=" + data +
                    '}';
        }
    }

}
