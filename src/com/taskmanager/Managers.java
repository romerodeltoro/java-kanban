package com.taskmanager;

import com.taskmanager.history.HistoryManager;
import com.taskmanager.history.InMemoryHistoryManager;
import com.taskmanager.taskmanager.InMemoryTaskManager;
import com.taskmanager.taskmanager.TaskManager;

public class Managers {

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

}
