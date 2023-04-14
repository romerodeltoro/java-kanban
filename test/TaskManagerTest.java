
import com.taskmanager.taskmanager.*;
import com.taskmanager.tasks.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.time.LocalDateTime;


abstract class TaskManagerTest<T extends TaskManager> {

    protected  T taskManager;
    public Task task1;
    public Task task2;
    public Epic epic1;
    public Epic epic2;
    public Subtask subtask1epic2;
    public Subtask subtask2epic2;
    public Task overlapTask;

    @BeforeEach
    public void setTaskManager() {

        task1 = new Task("Task1", "Description", 30,
                LocalDateTime.of(2020,2,20,20,20,20));
        task2 = new Task("Task2", "Description", 50,
                LocalDateTime.of(2021,11,11,11,11,11));
        epic1 = new Epic("Epic1", "Description");
        epic2 = new Epic("Epic2", "Description");
        subtask1epic2 = new Subtask("Subtask1", "Подзадача1", 4, 30,
                LocalDateTime.of(2222,2,22,22,22,22));
        subtask2epic2 = new Subtask("Subtask2", "Подзадача2", 4, 60,
                LocalDateTime.of(2222,2,23,23,23,23));
        overlapTask = new Task("Task1", "Description", 30,
                LocalDateTime.of(2020,2,20,20,30,20));

        epic2.addSubtask(subtask1epic2.getId(), subtask1epic2);
        epic2.addSubtask(subtask2epic2.getId(), subtask2epic2);

        /*
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);
        taskManager.createSubtask(subtask1epic2);
        taskManager.createSubtask(subtask2epic2);
        */
    }

    @AfterEach
    public void setNewTaskManager() {
        Task.setCounter(0); // Не нашел другого способа, чтобы сбосить счетчит id
    }

}
