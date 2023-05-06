import com.taskmanager.managers.Managers;
import com.taskmanager.history.HistoryManager;
import com.taskmanager.taskmanager.FileBackedTasksManager;
import com.taskmanager.tasks.Epic;
import com.taskmanager.tasks.Subtask;
import com.taskmanager.tasks.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    protected HistoryManager historyManager = Managers.getDefaultHistory();
    protected File file = new File("test/files/file.csv");

    @BeforeEach
    public void setTaskManager() {
        taskManager = new FileBackedTasksManager(file);
        taskManager.setHistoryManager(historyManager);
        super.setTaskManager();

        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);
        taskManager.createSubtask(subtask1epic2);
        taskManager.createSubtask(subtask2epic2);
    }

    @Test
    @DisplayName("Записываем все таски в файл с пустой историей")
    public void shouldSaveIfTasksExist() {
        final List<Task> tasks = new ArrayList<>(taskManager.getTasks());
        final List<Task> epics = new ArrayList<>(taskManager.getEpics());
        final List<Task> subTasks = new ArrayList<>(taskManager.getSubTasks());
        final List<Task> history = new ArrayList<>(taskManager.getHistory());

        FileBackedTasksManager fileManager = FileBackedTasksManager.loadFromFile(file);
        final List<Task> loadTasks = new ArrayList<>(fileManager.getTasks());
        final List<Task> loadEpics = new ArrayList<>(fileManager.getEpics());
        final List<Task> loadSubTasks = new ArrayList<>(fileManager.getSubTasks());
        final List<Task> loadHistory = new ArrayList<>(fileManager.getHistory());

        assertEquals(tasks, loadTasks, "Таски разные");
        assertEquals(epics, loadEpics, "Эпики разные");
        assertEquals(subTasks, loadSubTasks, "Субтаски разные");
        assertEquals(history, loadHistory, "История разная");
    }

    @Test
    @DisplayName("Сохраняем в файл пустые списки")
    public void shouldSaveIfTasksAbsent() {
        taskManager.deleteAllTasks();
        taskManager.deleteAllEpics();
        taskManager.deleteAllSubtasks();
        final List<Task> tasks = new ArrayList<>(taskManager.getTasks());
        final List<Task> epics = new ArrayList<>(taskManager.getEpics());
        final List<Task> subTasks = new ArrayList<>(taskManager.getSubTasks());

        FileBackedTasksManager fileManager = FileBackedTasksManager.loadFromFile(file);
        final List<Task> loadTasks = new ArrayList<>(fileManager.getTasks());
        final List<Task> loadEpics = new ArrayList<>(fileManager.getEpics());
        final List<Task> loadSubTasks = new ArrayList<>(fileManager.getSubTasks());

        assertEquals(tasks, loadTasks, "Таски разные");
        assertEquals(epics, loadEpics, "Эпики разные");
        assertEquals(subTasks, loadSubTasks, "Субтаски разные");
    }

    @Test
    @DisplayName("Записываем все таски и историю в файл")
    public void shouldSaveTask() {
        taskManager.getTask(task2.getId());
        taskManager.getTask(task1.getId());
        final List<Task> tasks = new ArrayList<>(taskManager.getTasks());
        final List<Task> epics = new ArrayList<>(taskManager.getEpics());
        final List<Task> subTasks = new ArrayList<>(taskManager.getSubTasks());
        final List<Task> history = new ArrayList<>(taskManager.getHistory());

        FileBackedTasksManager fileManager = FileBackedTasksManager.loadFromFile(file);
        final List<Task> loadTasks = new ArrayList<>(fileManager.getTasks());
        final List<Task> loadEpics = new ArrayList<>(fileManager.getEpics());
        final List<Task> loadSubTasks = new ArrayList<>(fileManager.getSubTasks());
        final List<Task> loadHistory = new ArrayList<>(fileManager.getHistory());

        assertEquals(tasks, loadTasks, "Таски разные");
        assertEquals(epics, loadEpics, "Эпики разные");
        assertEquals(subTasks, loadSubTasks, "Субтаски разные");
        assertEquals(history, loadHistory, "История разная");
    }


    @Test
    @DisplayName("Выгрузка всех данных из файла")
    public void shouldLoadFromFile() {
        final List<Task> tasks = new ArrayList<>(taskManager.getTasks());
        final List<Epic> epics = new ArrayList<>(taskManager.getEpics());
        final List<Subtask> subtasks = new ArrayList<>(taskManager.getSubTasks());
        taskManager.getTask(task1.getId());
        taskManager.getEpic(epic1.getId());
        taskManager.getSubtask(subtask1epic2.getId());
        final List<Task> history = new ArrayList<>(taskManager.getHistory());

        final FileBackedTasksManager manager = FileBackedTasksManager.loadFromFile(file);
        final List<Task> loadTasks = new ArrayList<>(manager.getTasks());
        final List<Task> loadEpics = new ArrayList<>(manager.getEpics());
        final List<Task> loadSubtasks = new ArrayList<>(manager.getSubTasks());
        final List<Task> loadHistory = new ArrayList<>(manager.getHistory());

        assertEquals(tasks, loadTasks, "Таски разные");
        assertEquals(epics, loadEpics, "Эпики разные");
        assertEquals(subtasks, loadSubtasks, "Субтаски разные");
        assertEquals(history, loadHistory, "История разная");
    }

    @Test
    @DisplayName("Выгрузка всех данных из файла с пустой историей")
    public void shouldLoadFromFileWithEmptyHistory() {
        final List<Task> tasks = new ArrayList<>(taskManager.getTasks());
        final List<Epic> epics = new ArrayList<>(taskManager.getEpics());
        final List<Subtask> subtasks = new ArrayList<>(taskManager.getSubTasks());
        final List<Task> history = new ArrayList<>(taskManager.getHistory());

        final FileBackedTasksManager manager = FileBackedTasksManager.loadFromFile(file);
        final List<Task> loadTasks = new ArrayList<>(manager.getTasks());
        final List<Task> loadEpics = new ArrayList<>(manager.getEpics());
        final List<Task> loadSubtasks = new ArrayList<>(manager.getSubTasks());
        final List<Task> loadHistory = new ArrayList<>(manager.getHistory());

        assertEquals(tasks, loadTasks, "Таски разные");
        assertEquals(epics, loadEpics, "Эпики разные");
        assertEquals(subtasks, loadSubtasks, "Субтаски разные");
        assertEquals(history, loadHistory, "История разная");
    }
}
