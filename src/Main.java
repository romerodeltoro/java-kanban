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

        manager.makeTask(new Task("Задача 1", "описание"));
        manager.makeTask(new Task("Задача 2", "описание"));
        manager.makeEpic(new Epic("Эпик 1", "описание"));
        manager.makeSubtask(new Subtask("Подзадача 1", "описание", 3));
        manager.makeSubtask(new Subtask("Подзадача 2", "описание", 3));

        manager.makeEpic(new Epic("Эпик 2", "описание"));
        manager.makeSubtask(new Subtask("Подзадача 3", "описание", 6));

        manager.printTasksList();
        System.out.println("****************\n");


        manager.updateTaskStatus(manager.tasks.get(2));
        manager.updateSubtask(manager.subtasks.get(4));
        manager.updateSubtask(manager.subtasks.get(7));

        manager.printTasksList();
        System.out.println("****************\n");

        manager.deleteTask(2);
        manager.deleteEpic(6);

        manager.printTasksList();
    }
}
