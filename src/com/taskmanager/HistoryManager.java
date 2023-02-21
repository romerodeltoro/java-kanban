package com.taskmanager;

import java.util.List;
import com.taskmanager.tasks.*;

public interface HistoryManager {

    void add(Task task);

    void remove(int id);

    List<Task> getHistory();

}
