import com.taskmanager.Managers;
import com.taskmanager.exceptions.ManagerSaveException;
import com.taskmanager.history.HistoryManager;
import com.taskmanager.taskmanager.CSVTaskConverter;
import com.taskmanager.taskmanager.FileBackedTasksManager;
import com.taskmanager.taskmanager.TaskManager;
import com.taskmanager.tasks.Epic;
import com.taskmanager.tasks.Subtask;
import com.taskmanager.tasks.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager>{

    protected HistoryManager historyManager = Managers.getDefaultHistory();

    @BeforeEach
    public void setTaskManager() {
        taskManager = new FileBackedTasksManager(new File("src/com/taskmanager/resources/saveFile.csv"));
        taskManager.setHistoryManager(historyManager);
        super.setTaskManager();
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);
        taskManager.createSubtask(subtask1epic2);
        taskManager.createSubtask(subtask2epic2);
    }

    @AfterEach
    public void setNewTaskManager() {
        super.setNewTaskManager();
    }

    public void save() {

    }

    public boolean isFilesEqual(File file1, File file2) {
        try {
            if (Files.size(file1.toPath()) != Files.size(file2.toPath())) {
                return false;
            }
            byte[] first = Files.readAllBytes(file1.toPath());
            byte[] second = Files.readAllBytes(file2.toPath());
            return Arrays.equals(first, second);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Test
    @DisplayName("Записываем все таски в файл с пустой историей")
    public void shouldSaveIfTasksExist(){

        final File file = new File("test/files/file.csv");
        final List<Task> tasks = new ArrayList<>(taskManager.getTasks());
        final List<Epic> epics = new ArrayList<>(taskManager.getEpics());
        final List<Subtask> subtasks = new ArrayList<>(taskManager.getSubTasks());

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8))) {
            writer.write(CSVTaskConverter.getHeader());
            for (Task task : tasks) {
                writer.write(task.toString());
            }
            for (Epic epic : epics) {
                writer.write(epic.toString());
            }
            for (Subtask subtask : subtasks) {
                writer.write(subtask.toString());
            }
            writer.newLine();
            writer.write(CSVTaskConverter.historyToString(historyManager));
        } catch (IOException e) {
            throw new ManagerSaveException("Не удалось сохранить в файл", e);
        }
        final File newFile = new File("src/com/taskmanager/resources/saveFile.csv");
        boolean isFilesEqual = isFilesEqual(file, newFile);

        assertTrue(isFilesEqual, "Файлы разные");
    }

    @Test
    @DisplayName("Сохраняем в файл пустые списки")
    public void shouldSaveIfTasksAbsent(){
        final File file = new File("test/files/emptyfile.csv");
        final List<Task> tasks = new ArrayList<>();
        final List<Epic> epics = new ArrayList<>();
        final List<Subtask> subtasks = new ArrayList<>();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8))) {
            writer.write(CSVTaskConverter.getHeader());
            for (Task task : tasks) {
                writer.write(task.toString());
            }
            for (Epic epic : epics) {
                writer.write(epic.toString());
            }
            for (Subtask subtask : subtasks) {
                writer.write(subtask.toString());
            }
            writer.newLine();
        } catch (IOException e) {
            throw new ManagerSaveException("Не удалось сохранить в файл", e);
        }

        taskManager.deleteAllTasks();
        taskManager.deleteAllEpics();
        taskManager.deleteAllSubtasks();
        final File newFile = new File("src/com/taskmanager/resources/saveFile.csv");

        boolean isFilesEqual = isFilesEqual(file, newFile);

        assertTrue(isFilesEqual, "Файлы разные");
    }

    @Test
    @DisplayName("Записываем все таски и историю в файл")
    public void shouldSaveTask() {
        final File file = new File("test/files/fileWithHistory.csv");
        final List<Task> tasks = new ArrayList<>(taskManager.getTasks());
        final List<Epic> epics = new ArrayList<>(taskManager.getEpics());
        final List<Subtask> subtasks = new ArrayList<>(taskManager.getSubTasks());
        taskManager.getTask(task1.getId());
        taskManager.getEpic(epic1.getId());
        taskManager.getSubtask(subtask1epic2.getId());

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8))) {
            writer.write(CSVTaskConverter.getHeader());
            for (Task task : tasks) {
                writer.write(task.toString());
            }
            for (Epic epic : epics) {
                writer.write(epic.toString());
            }
            for (Subtask subtask : subtasks) {
                writer.write(subtask.toString());
            }
            writer.newLine();
            writer.write(CSVTaskConverter.historyToString(historyManager));
        } catch (IOException e) {
            throw new ManagerSaveException("Не удалось сохранить в файл", e);
        }

        final File newFile = new File("src/com/taskmanager/resources/saveFile.csv");
        boolean isFilesEqual = isFilesEqual(file, newFile);

        assertTrue(isFilesEqual, "Файлы разные");
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
        final File loadFile = new File("src/com/taskmanager/resources/saveFile.csv");

        final FileBackedTasksManager manager = FileBackedTasksManager.loadFromFile(loadFile);
        final List<Task> loadTasks = new ArrayList<>(manager.getTasks());
        final List<Task> loadEpics = new ArrayList<>(manager.getEpics());
        final List<Task> loadSubtasks = new ArrayList<>(manager.getSubTasks());
        final List<Task> loadHistory = new ArrayList<>(manager.getHistory());

        assertEquals(tasks,loadTasks , "Таски разные");
        assertEquals(epics,loadEpics , "Эпики разные");
        assertEquals(subtasks,loadSubtasks , "Субтаски разные");
        assertEquals(history,loadHistory , "История разная");
    }

    @Test
    @DisplayName("Выгрузка всех данных из файла с пустой историей")
    public void shouldLoadFromFileWithEmptyHistory() {
        final List<Task> tasks = new ArrayList<>(taskManager.getTasks());
        final List<Epic> epics = new ArrayList<>(taskManager.getEpics());
        final List<Subtask> subtasks = new ArrayList<>(taskManager.getSubTasks());
        final List<Task> history = new ArrayList<>(taskManager.getHistory());
        final File loadFile = new File("src/com/taskmanager/resources/saveFile.csv");

        final FileBackedTasksManager manager = FileBackedTasksManager.loadFromFile(loadFile);
        final List<Task> loadTasks = new ArrayList<>(manager.getTasks());
        final List<Task> loadEpics = new ArrayList<>(manager.getEpics());
        final List<Task> loadSubtasks = new ArrayList<>(manager.getSubTasks());
        final List<Task> loadHistory = new ArrayList<>(manager.getHistory());

        assertEquals(tasks,loadTasks , "Таски разные");
        assertEquals(epics,loadEpics , "Эпики разные");
        assertEquals(subtasks,loadSubtasks , "Субтаски разные");
        assertEquals(history,loadHistory , "История разная");
    }
}
