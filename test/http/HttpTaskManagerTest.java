package http;

import com.google.gson.Gson;
import com.taskmanager.client.KVTaskClient;
import com.taskmanager.managers.HttpTaskManager;
import com.taskmanager.managers.Managers;
import com.taskmanager.server.HttpTaskServer;
import com.taskmanager.server.KVServer;
import com.taskmanager.tasks.Epic;
import com.taskmanager.tasks.Subtask;
import com.taskmanager.tasks.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.taskmanager.tasks.Task.setCounter;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpTaskManagerTest {

    private KVServer kvServer;
    private HttpTaskServer httpTaskServer;
    private HttpTaskManager taskManager;
    private final Task task = new Task("Task", "Description of task", 10,
            LocalDateTime.of(2023,5,2,20,20,20));
    private final Epic epic = new Epic("Epic", "Description of epic");
    private final Subtask subtask = new Subtask("Subtask1", "Description of subtask", 2,333,
            LocalDateTime.of(2023,5,4,20,20,20));

    @BeforeEach
    void init() throws IOException {
        kvServer = new KVServer();
        kvServer.start();
        taskManager = (HttpTaskManager) Managers.getDefault();
        httpTaskServer = new HttpTaskServer(taskManager);
        httpTaskServer.start();

        taskManager.createTask(task);
        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask);
        taskManager.getTask(1);
        taskManager.getEpic(2);
        taskManager.getSubtask(3);
    }
    @AfterEach
    void tearDown() {
        httpTaskServer.stop();
        kvServer.stop();
        setCounter(0);
    }


    @Test
    @DisplayName("Сохраняем и восстанавливаем все данные с сервера")
    public void shouldSaveAndLoadTasks() throws IOException {
        final List<Task> tasks = new ArrayList<>(taskManager.getTasks());
        final List<Task> epics = new ArrayList<>(taskManager.getEpics());
        final List<Task> subTasks = new ArrayList<>(taskManager.getSubTasks());
        final List<Task> history = new ArrayList<>(taskManager.getHistory());

        HttpTaskManager manager2 = new HttpTaskManager();
        manager2.load();
        final List<Task> loadTasks = new ArrayList<>(manager2.getTasks());
        final List<Task> loadEpics = new ArrayList<>(manager2.getEpics());
        final List<Task> loadSubTasks = new ArrayList<>(manager2.getSubTasks());
        final List<Task> loadHistory = new ArrayList<>(manager2.getHistory());

        assertEquals(tasks, loadTasks, "Таски разные");
        assertEquals(epics, loadEpics, "Эпики разные");
        assertEquals(subTasks, loadSubTasks, "Субтаски разные");
        assertEquals(history, loadHistory, "История разная");
    }

}
