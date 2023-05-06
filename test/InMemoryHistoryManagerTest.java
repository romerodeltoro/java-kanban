import com.taskmanager.managers.Managers;
import com.taskmanager.history.HistoryManager;
import com.taskmanager.taskmanager.InMemoryTaskManager;
import com.taskmanager.tasks.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.taskmanager.tasks.Task.setCounter;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class InMemoryHistoryManagerTest <T extends HistoryManager>{

    protected InMemoryTaskManager taskManager = new InMemoryTaskManager();
    HistoryManager historyManager = Managers.getDefaultHistory();
    Task task1;
    Task task2;
    Task task3;

    @BeforeEach
    public void setTaskManager() {
        taskManager.setHistoryManager(historyManager);
        task1 = new Task("Task1", "Description", 30,
                LocalDateTime.of(2020,2,20,20,20,20));
        task2 = new Task("Task2", "Description", 50,
                LocalDateTime.of(2021,11,11,11,11,11));
        task3 = new Task("Task3", "Description", 60,
                LocalDateTime.of(2022,12,22,22,22,22));
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createTask(task3);

    }
    @AfterEach
    void resetTaskManager() {
        setCounter(0);
    }

    @Test
    @DisplayName("Проверка на пустую историю")
    public void shouldGetEmptyHistory() {
        final List<Task> emptyHistory = historyManager.getHistory();

        assertTrue(emptyHistory.isEmpty());
    }

    @Test
    @DisplayName("Проверка заполнения истории")
    public void shouldGetHistory() {
        taskManager.getTask(task1.getId());
        taskManager.getTask(task2.getId());
        final List<Task> history = new ArrayList<>(List.of(task2, task1));

        assertEquals(history, historyManager.getHistory(), "Истории разные");
    }

    @Test
    @DisplayName("Проверка на задвоение истории")
    public void shouldGetHistoryWithoutDoubling() {
        taskManager.getTask(task1.getId());
        taskManager.getTask(task2.getId());
        taskManager.getTask(task1.getId());
        final List<Task> history = new ArrayList<>(List.of(task1, task2));

        assertEquals(history, historyManager.getHistory(), "Истории разные");
    }

    @Test
    @DisplayName("Удаление с конца истории")
    public void shouldRemoveFromHead() {
        taskManager.getTask(task1.getId());
        taskManager.getTask(task2.getId());
        taskManager.getTask(task3.getId());
        taskManager.deleteTask(task1.getId());
        final List<Task> history = new ArrayList<>(List.of(task3, task2));

        assertEquals(history, historyManager.getHistory(), "Истории разные");
    }
    @Test
    @DisplayName("Удаление с начала истории")
    public void shouldRemoveFromTail() {
        taskManager.getTask(task1.getId());
        taskManager.getTask(task2.getId());
        taskManager.getTask(task3.getId());
        taskManager.deleteTask(task3.getId());
        final List<Task> history = new ArrayList<>(List.of(task2, task1));

        assertEquals(history, historyManager.getHistory(), "Истории разные");
    }
    @Test
    @DisplayName("Удаление из середины истории")
    public void shouldRemoveFromCenter() {
        taskManager.getTask(task1.getId());
        taskManager.getTask(task2.getId());
        taskManager.getTask(task3.getId());
        taskManager.deleteTask(task2.getId());
        final List<Task> history = new ArrayList<>(List.of(task3, task1));

        assertEquals(history, historyManager.getHistory(), "Истории разные");
    }
}
