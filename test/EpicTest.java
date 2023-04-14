import com.taskmanager.taskmanager.InMemoryTaskManager;
import com.taskmanager.tasks.Epic;
import com.taskmanager.tasks.Subtask;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.taskmanager.tasks.Task.Status.*;
import static com.taskmanager.tasks.Task.Status.IN_PROGRESS;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class EpicTest extends TaskManagerTest<InMemoryTaskManager>{

    @BeforeEach
    public void setTaskManager() {
        taskManager = new InMemoryTaskManager();
        super.setTaskManager();
    }

    @AfterEach
    public void setNewTaskManager() {
        super.setNewTaskManager();
    }

    @Test
    public void shouldEpicStatusNewIfSubtaskListIsClear() {
        assertEquals(NEW, epic1.getStatus(), "Status wrong");
    }

    @Test
    public void shouldEpicStatusNewIfAllSubtaskNew() {
        assertEquals(NEW, epic2.getStatus(), "Status wrong");
    }

    @Test
    public void shouldEpicStatusDoneIfAllSubtaskDone() {
        taskManager.createEpic(epic2);
        final Subtask subtask1 = taskManager.createSubtask(subtask1epic2);
        final Subtask subtask2 = taskManager.createSubtask(subtask2epic2);
        final Epic epic = taskManager.createEpic(epic2);
        taskManager.updateSubtaskStatus(subtask1);
        taskManager.updateSubtaskStatus(subtask1);
        taskManager.updateSubtaskStatus(subtask2);
        taskManager.updateSubtaskStatus(subtask2);

        assertEquals(DONE, epic.getStatus(), "Status wrong");
    }

    @Test
    public void shouldEpicStatusInProgressIfSubtaskNewAndDone() {
        taskManager.createEpic(epic2);
        final Subtask subtask1 = taskManager.createSubtask(subtask1epic2);
        final Subtask subtask2 = taskManager.createSubtask(subtask2epic2);
        final Epic epic = taskManager.createEpic(epic2);
        taskManager.updateSubtaskStatus(subtask1);
        taskManager.updateSubtaskStatus(subtask1);

        assertEquals(IN_PROGRESS, epic.getStatus(), "Status wrong");
    }

    @Test
    public void shouldEpicStatusInProgressIfAllSubtaskInProgress() {
        taskManager.createEpic(epic2);
        final Subtask subtask1 = taskManager.createSubtask(subtask1epic2);
        final Subtask subtask2 = taskManager.createSubtask(subtask2epic2);
        final Epic epic = taskManager.createEpic(epic2);
        taskManager.updateSubtaskStatus(subtask1);
        taskManager.updateSubtaskStatus(subtask2);

        assertEquals(IN_PROGRESS, epic.getStatus(), "Status wrong");
    }
}
