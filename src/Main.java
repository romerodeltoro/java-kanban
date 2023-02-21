import com.taskmanager.*;
import com.taskmanager.tasks.*;

public class Main {

    public static void main(String[] args) {

        printTest();

    }

    // Тест
    static void printTest() {

        TaskManager manager = Managers.getDefault();

        manager.createTask(new Task("Задача 1", "описание"));
        manager.createTask(new Task("Задача 2", "описание"));
        manager.createEpic(new Epic("Эпик 1", "описание"));
        manager.createSubtask(new Subtask("Подзадача 1", "описание", 3));
        manager.createSubtask(new Subtask("Подзадача 2", "описание", 3));

        manager.createEpic(new Epic("Эпик 2", "описание"));
        manager.createSubtask(new Subtask("Подзадача 3", "описание", 6));

        manager.getTask(1);
        manager.getTask(2);
        manager.getEpic(3);
        manager.getTask(2);
        manager.getEpic(3);
        manager.getEpic(3);
        manager.getTask(2);
        manager.getTask(1);

        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }

    }
}
