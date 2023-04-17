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
    @DisplayName("�������� �� ����������� �������")
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
        assertEquals("����� ���������� ������ ������������ � ��� �������������", e.getMessage());
    }

    @Test
    @DisplayName("�������� �� ������� � ��������������� ������")
    void shouldPrioritizedTasksSorted() {
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createTask(task3);
        final List<Task> sortedList = List.of(task3, task1, task2);

        assertEquals(sortedList, taskManager.getPrioritizedTasks(), "�������� ���������� �����.");
    }

    @Test
    @DisplayName("�������� ������ �����")
    void addNewTask() {

        final Task savedTask = taskManager.createTask(task1);

        assertNotNull(savedTask, "������ �� �������.");
        assertEquals(task1, savedTask, "������ �� ���������.");

        final List<Task> tasks = new ArrayList<>(taskManager.getTasks());

        assertNotNull(tasks, "������ �� ������������.");
        assertEquals(1, tasks.size(), "�������� ���������� �����.");
        assertEquals(task1, tasks.get(0), "������ �� ���������.");
    }

    @Test
    @DisplayName("�������� ������ �����")
    void addNewEpic() {

        final Epic savedEpic = taskManager.createEpic(epic1);

        assertNotNull(savedEpic, "������ �� �������.");
        assertEquals(epic1, savedEpic, "������ �� ���������.");

        final List<Task> epics = new ArrayList<>(taskManager.getEpics());

        assertNotNull(taskManager.getEpics(), "������ �� ������������.");
        assertEquals(1, epics.size(), "�������� ���������� �����.");
        assertEquals(epic1, epics.get(0), "������ �� ���������.");
    }

    @Test
    @DisplayName("�������� ������ ��������")
    void addNewSubtask() {

        final Epic savedEpic = taskManager.createEpic(epic2);
        final Subtask savedSubtask = taskManager.createSubtask(subtask1epic2);

        assertNotNull(savedSubtask, "������ �� �������.");
        assertEquals(subtask1epic2, savedSubtask, "������ �� ���������.");

        final List<Task> subtasks = new ArrayList<>(taskManager.getSubTasks());

        assertNotNull(taskManager.getSubTasks(), "������ �� ������������.");
        assertEquals(1, subtasks.size(), "�������� ���������� �����.");
        assertEquals(subtask1epic2, subtasks.get(0), "������ �� ���������.");
        assertNotNull(savedEpic, "������ �� �������.");
    }

    @Test
    @DisplayName("��������� ������ ������")
    void getAllTasks() {
        final Task task = taskManager.createTask(task1);
        final List<Task> tasks = new ArrayList<>(taskManager.getTasks());
        int size = tasks.size();

        assertNotNull(tasks, "������ �� ������������.");
        assertEquals(task, tasks.get(0), "������ �� ���������.");
        assertEquals(1, size, "���������� ����� �� ���������.");
    }

    @Test
    @DisplayName("��������� ������ ������")
    void getAllEpics() {
        taskManager.createEpic(epic1);
        final List<Task> epics = new ArrayList<>(taskManager.getEpics());
        int size = epics.size();

        assertNotNull(epics, "������ �� ������������.");
        assertEquals(epic1, epics.get(0), "������ �� ���������.");
        assertEquals(1, size, "���������� ����� �� ���������.");
    }

    @Test
    @DisplayName("��������� ������ ��������")
    void getAllSubTusks() {
        taskManager.createEpic(epic2);
        taskManager.createSubtask(subtask1epic2);
        final List<Task> subtasks = new ArrayList<>(taskManager.getSubTasks());
        int size = subtasks.size();

        assertNotNull(subtasks, "������ �� ������������.");
        assertEquals(subtask1epic2, subtasks.get(0), "������ �� ���������.");
        assertEquals(1, size, "���������� ����� �� ���������.");

    }

    @Test
    @DisplayName("�������� ������ �� ������")
    void deleteAllTasks() {
        taskManager.createTask(task1);
        taskManager.deleteAllTasks();
        final List<Task> emptyTasks = new ArrayList<>(taskManager.getTasks());

        assertTrue(emptyTasks.isEmpty(), "������ �� ����");
    }

    @Test
    @DisplayName("�������� ������ �� ������")
    void deleteAllEpics() {
        taskManager.createEpic(epic1);
        taskManager.deleteAllEpics();
        final List<Task> emptyEpics = new ArrayList<>(taskManager.getEpics());

        assertTrue(emptyEpics.isEmpty(), "������ �� ����");
    }

    @Test
    @DisplayName("�������� ��������� �� ������")
    void deleteAllSubTasks() {
        taskManager.createEpic(epic2);
        taskManager.createSubtask(subtask1epic2);
        taskManager.deleteAllSubtasks();
        final List<Task> emptySubTasks = new ArrayList<>(taskManager.getSubTasks());

        assertTrue(emptySubTasks.isEmpty(), "������ �� ����");
    }

    @Test
    @DisplayName("��������� ����� �� ID")
    void getTask() {
        final Task savedTask = taskManager.createTask(task1);
        final int id = savedTask.getId();
        assertEquals(savedTask, taskManager.getTask(id), "������ �� ���������.");

        final NullPointerException e = assertThrows(
                NullPointerException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        taskManager.getTask(0);
                    }
                }
        );
        assertEquals("������ � ����� ID ��� � ����.", e.getMessage());

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
    @DisplayName("��������� ����� �� ID")
    void getEpic() {
        final Epic savedEpic = taskManager.createEpic(epic1);
        final int id = savedEpic.getId();
        assertEquals(savedEpic, taskManager.getEpic(id), "������ �� ���������.");

        final NullPointerException e = assertThrows(
                NullPointerException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        taskManager.getEpic(0);
                    }
                }
        );
        assertEquals("������ � ����� ID ��� � ����.", e.getMessage());

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
    @DisplayName("��������� ��������� �� ID")
    void getSubtask() {
        taskManager.createEpic(epic2);
        final Subtask savedSubtask = taskManager.createSubtask(subtask1epic2);
        final int id = savedSubtask.getId();

        assertEquals(savedSubtask, taskManager.getSubtask(id), "������ �� ���������.");
        final NullPointerException e = assertThrows(
                NullPointerException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        taskManager.getEpic(0);
                    }
                }
        );
        assertEquals("������ � ����� ID ��� � ����.", e.getMessage());
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
    @DisplayName("���������� �����")
    void updateTask() {
        final Task savedTask1 = taskManager.createTask(task1);
        final Task savedTask2 = taskManager.createTask(task2);
        taskManager.updateTask(savedTask1.getId(), savedTask2);
        Task.Status status1 = savedTask1.getStatus();
        assertEquals(Task.Status.IN_PROGRESS, status1,"������1 �� ������");

        taskManager.updateTask(savedTask1.getId(), savedTask1);
        Task.Status status2 = savedTask1.getStatus();
        assertEquals(Task.Status.DONE, status2,"������2 �� ������");
    }

    @Test
    @DisplayName("���������� ��������")
    void updateSubtask() {
        taskManager.createEpic(epic2);
        final Subtask savedSubtask1 = taskManager.createSubtask(subtask1epic2);
        taskManager.updateSubtask(savedSubtask1.getId(), subtask2epic2);
        Task.Status status1 = savedSubtask1.getStatus();
        assertEquals(Task.Status.IN_PROGRESS, status1,"������1 �� ������");

        taskManager.updateSubtask(savedSubtask1.getId(), savedSubtask1);
        Task.Status status2 = savedSubtask1.getStatus();
        assertEquals(Task.Status.DONE, status2,"������2 �� ������");
    }

    @Test
    @DisplayName("�������� ����� �� ID")
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
        assertEquals("������ � ����� ID ��� � ����.", e.getMessage());
    }

    @Test
    @DisplayName("�������� ����� �� ID")
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
        assertEquals("������ � ����� ID ��� � ����.", nPE1.getMessage());

        final NullPointerException nPE2 = assertThrows(
                NullPointerException.class,
                new Executable() {
                    @Override
                    public void execute() { taskManager.getSubtask(subtask1ForSavedEpic.getId());}
                }
        );
        assertEquals("������ � ����� ID ��� � ����.", nPE2.getMessage());

        final NullPointerException nPE3 = assertThrows(
                NullPointerException.class,
                new Executable() {
                    @Override
                    public void execute() { taskManager.getSubtask(subtask2ForSavedEpic.getId());}
                }
        );
        assertEquals("������ � ����� ID ��� � ����.", nPE3.getMessage());

    }

    @Test
    @DisplayName("�������� ��������� �� ID")
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
        assertEquals("������ � ����� ID ��� � ����.", e.getMessage());
        assertNull(epicForSavedSubtask.getSubtasks().get(id));
    }
}
