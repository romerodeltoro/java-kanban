import com.taskmanager.Epic;
import com.taskmanager.Manager;
import com.taskmanager.Subtask;
import com.taskmanager.Task;

public class Main {

    public static void main(String[] args) {

        printTest();

    }

    // Тест
    static void printTest() {

        Manager manager = new Manager();

        manager.createTask(new Task("Задача 1", "описание"));
        manager.createTask(new Task("Задача 2", "описание"));
        manager.createEpic(new Epic("Эпик 1", "описание"));
        manager.createSubtask(new Subtask("Подзадача 1", "описание", 3));
        manager.createSubtask(new Subtask("Подзадача 2", "описание", 3));

        manager.createEpic(new Epic("Эпик 2", "описание"));
        manager.createSubtask(new Subtask("Подзадача 3", "описание", 6));

        manager.printTasksList();
        System.out.println("****************\n");


        manager.updateTask(2, new Task("Задача 3",  "описание"));
        manager.updateSubtask(4, manager.subtasks.get(4));
        manager.updateSubtask(7, manager.subtasks.get(7));

        manager.printTasksList();
        System.out.println("****************\n");

        manager.deleteTask(2);
        manager.deleteEpic(6);

        manager.printTasksList();
    }
}
