package com.taskmanager;

import java.util.List;
import com.taskmanager.tasks.*;

public interface HistoryManager {

    void add(Task task);

    List<Task> getHistory();
}
