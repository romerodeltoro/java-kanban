import com.taskmanager.taskmanager.InMemoryTaskManager;
import com.taskmanager.tasks.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {


    @BeforeEach
    public void setTaskManager() {
        taskManager = new InMemoryTaskManager();
        super.setTaskManager();
    }

    @Test
    @DisplayName("Проверка на пересечение времени")
    void shouldNewTaskOverlapExisting() {
        taskManager.createTask(task1);
        final RuntimeException e = assertThrows(
                RuntimeException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        taskManager.createTask(overlapTask);
                    }
                }
        );
        assertEquals("Время выволнения задачи пересекается с уже существующими", e.getMessage());
    }

    @Test
    @DisplayName("Проверка на порядок в отсортированном списке")
    void shouldPrioritizedTasksSorted() {
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createTask(task3);
        final List<Task> sortedList = List.of(task3, task1, task2);

        assertEquals(sortedList, taskManager.getPrioritizedTasks(), "Неверное количество задач.");
    }

    @Test
    @DisplayName("Создание нового таска")
    void addNewTask() {

        final Task savedTask = taskManager.createTask(task1);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task1, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = new ArrayList<>(taskManager.getTasks());

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task1, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    @DisplayName("Создание нового эпика")
    void addNewEpic() {

        final Epic savedEpic = taskManager.createEpic(epic1);

        assertNotNull(savedEpic, "Задача не найдена.");
        assertEquals(epic1, savedEpic, "Задачи не совпадают.");

        final List<Task> epics = new ArrayList<>(taskManager.getEpics());

        assertNotNull(taskManager.getEpics(), "Задачи не возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество задач.");
        assertEquals(epic1, epics.get(0), "Задачи не совпадают.");
    }

    @Test
    @DisplayName("Создание нового субтаска")
    void addNewSubtask() {

        final Epic savedEpic = taskManager.createEpic(epic2);
        final Subtask savedSubtask = taskManager.createSubtask(subtask1epic2);

        assertNotNull(savedSubtask, "Задача не найдена.");
        assertEquals(subtask1epic2, savedSubtask, "Задачи не совпадают.");

        final List<Task> subtasks = new ArrayList<>(taskManager.getSubTasks());

        assertNotNull(taskManager.getSubTasks(), "Задачи не возвращаются.");
        assertEquals(1, subtasks.size(), "Неверное количество задач.");
        assertEquals(subtask1epic2, subtasks.get(0), "Задачи не совпадают.");
        assertNotNull(savedEpic, "Задача не найдена.");
    }

    @Test
    @DisplayName("Получение списка тасков")
    void getAllTasks() {
        final Task task = taskManager.createTask(task1);
        final List<Task> tasks = new ArrayList<>(taskManager.getTasks());
        int size = tasks.size();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
        assertEquals(1, size, "Количество задач не совпадает.");
    }

    @Test
    @DisplayName("Получение списка эпиков")
    void getAllEpics() {
        taskManager.createEpic(epic1);
        final List<Task> epics = new ArrayList<>(taskManager.getEpics());
        int size = epics.size();

        assertNotNull(epics, "Задачи не возвращаются.");
        assertEquals(epic1, epics.get(0), "Задачи не совпадают.");
        assertEquals(1, size, "Количество задач не совпадает.");
    }

    @Test
    @DisplayName("Получение списка подзадач")
    void getAllSubTusks() {
        taskManager.createEpic(epic2);
        taskManager.createSubtask(subtask1epic2);
        final List<Task> subtasks = new ArrayList<>(taskManager.getSubTasks());
        int size = subtasks.size();

        assertNotNull(subtasks, "Задачи не возвращаются.");
        assertEquals(subtask1epic2, subtasks.get(0), "Задачи не совпадают.");
        assertEquals(1, size, "Количество задач не совпадает.");

    }

    @Test
    @DisplayName("Удаление тасков из списка")
    void deleteAllTasks() {
        taskManager.createTask(task1);
        taskManager.deleteAllTasks();
        final List<Task> emptyTasks = new ArrayList<>(taskManager.getTasks());

        assertTrue(emptyTasks.isEmpty(), "Список не пуст");
    }

    @Test
    @DisplayName("Удаление эпиков из списка")
    void deleteAllEpics() {
        taskManager.createEpic(epic1);
        taskManager.deleteAllEpics();
        final List<Task> emptyEpics = new ArrayList<>(taskManager.getEpics());

        assertTrue(emptyEpics.isEmpty(), "Список не пуст");
    }

    @Test
    @DisplayName("Удаление сабтасков из списка")
    void deleteAllSubTasks() {
        taskManager.createEpic(epic2);
        taskManager.createSubtask(subtask1epic2);
        taskManager.deleteAllSubtasks();
        final List<Task> emptySubTasks = new ArrayList<>(taskManager.getSubTasks());

        assertTrue(emptySubTasks.isEmpty(), "Список не пуст");
    }

    @Test
    @DisplayName("Получение таска по ID")
    void getTask() {
        final Task savedTask = taskManager.createTask(task1);
        final int id = savedTask.getId();
        assertEquals(savedTask, taskManager.getTask(id), "Задачи не совпадают.");

        final NullPointerException e = assertThrows(
                NullPointerException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        taskManager.getTask(0);
                    }
                }
        );
        assertEquals("Задачи с таким ID нет в базе.", e.getMessage());

        final List<Task> tasks = new ArrayList<>(taskManager.getTasks());
        tasks.clear();
        final NullPointerException exception = assertThrows(
                NullPointerException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        taskManager.getTask(id);
                    }
                }
        );
        assertNull(exception.getMessage());
    }

    @Test
    @DisplayName("Получение эпика по ID")
    void getEpic() {
        final Epic savedEpic = taskManager.createEpic(epic1);
        final int id = savedEpic.getId();
        assertEquals(savedEpic, taskManager.getEpic(id), "Задачи не совпадают.");

        final NullPointerException e = assertThrows(
                NullPointerException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        taskManager.getEpic(0);
                    }
                }
        );
        assertEquals("Задачи с таким ID нет в базе.", e.getMessage());

        final List<Task> epics = new ArrayList<>(taskManager.getEpics());
        epics.clear();
        final NullPointerException exception = assertThrows(
                NullPointerException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        taskManager.getEpic(id);
                    }
                }
        );
        assertNull(exception.getMessage());
    }

    @Test
    @DisplayName("Получение подзадачи по ID")
    void getSubtask() {
        taskManager.createEpic(epic2);
        final Subtask savedSubtask = taskManager.createSubtask(subtask1epic2);
        final int id = savedSubtask.getId();

        assertEquals(savedSubtask, taskManager.getSubtask(id), "Задачи не совпадают.");
        final NullPointerException e = assertThrows(
                NullPointerException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        taskManager.getEpic(0);
                    }
                }
        );
        assertEquals("Задачи с таким ID нет в базе.", e.getMessage());
        final List<Task> subtasks = new ArrayList<>(taskManager.getSubTasks());
        subtasks.clear();
        final NullPointerException exception = assertThrows(
                NullPointerException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        taskManager.getSubtask(id);
                    }
                }
        );
        assertNull(exception.getMessage());
    }

    @Test
    @DisplayName("Обновление таска")
    void updateTask() {
        final Task savedTask1 = taskManager.createTask(task1);
        final Task savedTask2 = taskManager.createTask(task2);
        taskManager.updateTask(savedTask1.getId(), savedTask2);
        Task.Status status1 = savedTask1.getStatus();
        assertEquals(Task.Status.IN_PROGRESS, status1,"Статус1 не верный");

        taskManager.updateTask(savedTask1.getId(), savedTask1);
        Task.Status status2 = savedTask1.getStatus();
        assertEquals(Task.Status.DONE, status2,"Статус2 не верный");
    }

    @Test
    @DisplayName("Обновление субтаска")
    void updateSubtask() {
        taskManager.createEpic(epic2);
        final Subtask savedSubtask1 = taskManager.createSubtask(subtask1epic2);
        taskManager.updateSubtask(savedSubtask1.getId(), subtask2epic2);
        Task.Status status1 = savedSubtask1.getStatus();
        assertEquals(Task.Status.IN_PROGRESS, status1,"Статус1 не верный");

        taskManager.updateSubtask(savedSubtask1.getId(), savedSubtask1);
        Task.Status status2 = savedSubtask1.getStatus();
        assertEquals(Task.Status.DONE, status2,"Статус2 не верный");
    }

    @Test
    @DisplayName("Удаление таска по ID")
    void deleteTask() {
        final Task savedTask = taskManager.createTask(task1);
        final int id = savedTask.getId();
        taskManager.deleteTask(id);

        final NullPointerException e = assertThrows(
                NullPointerException.class,
                new Executable() {
                    @Override
                    public void execute() { taskManager.getTask(id);}
                }
        );
        assertEquals("Задачи с таким ID нет в базе.", e.getMessage());
    }

    @Test
    @DisplayName("Удаление эпика по ID")
    void deleteEpic() {
        final Epic savedEpic = taskManager.createEpic(epic2);
        final int id = savedEpic.getId();
        final Subtask subtask1ForSavedEpic = taskManager.createSubtask(subtask1epic2);
        final Subtask subtask2ForSavedEpic = taskManager.createSubtask(subtask2epic2);

        taskManager.deleteEpic(id);
        final NullPointerException nPE1 = assertThrows(
                NullPointerException.class,
                new Executable() {
                    @Override
                    public void execute() { taskManager.getEpic(id);}
                }
        );
        assertEquals("Задачи с таким ID нет в базе.", nPE1.getMessage());

        final NullPointerException nPE2 = assertThrows(
                NullPointerException.class,
                new Executable() {
                    @Override
                    public void execute() { taskManager.getSubtask(subtask1ForSavedEpic.getId());}
                }
        );
        assertEquals("Задачи с таким ID нет в базе.", nPE2.getMessage());

        final NullPointerException nPE3 = assertThrows(
                NullPointerException.class,
                new Executable() {
                    @Override
                    public void execute() { taskManager.getSubtask(subtask2ForSavedEpic.getId());}
                }
        );
        assertEquals("Задачи с таким ID нет в базе.", nPE3.getMessage());

    }

    @Test
    @DisplayName("Удаление субтакска по ID")
    void deleteSubtask() {
        final Epic epicForSavedSubtask = taskManager.createEpic(epic2);
        final Subtask savedSubtask = taskManager.createSubtask(subtask1epic2);
        final int id = savedSubtask.getId();
        taskManager.deleteSubtask(id);

        final NullPointerException e = assertThrows(
                NullPointerException.class,
                new Executable() {
                    @Override
                    public void execute() { taskManager.getSubtask(id);}
                }
        );
        assertEquals("Задачи с таким ID нет в базе.", e.getMessage());
        assertNull(epicForSavedSubtask.getSubtasks().get(id));
    }
}
