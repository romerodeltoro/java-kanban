import com.taskmanager.*;
import com.taskmanager.tasks.*;

public class Main {

    public static void main(String[] args) {

        printTest();

    }

    // Тест
    static void printTest() {

        TaskManager manager = Managers.getDefault();

        manager.createEpic(new Epic("Эпик 1", "описание"));
        manager.createSubtask(new Subtask("Подзадача 1", "описание", 1));
        manager.createSubtask(new Subtask("Подзадача 2", "описание", 1));
        manager.createSubtask(new Subtask("Подзадача 3", "описание", 1));
        manager.createEpic(new Epic("Эпик 2", "описание"));


        manager.getEpic(1);
        manager.getSubtask(2);
        manager.getSubtask(3);
        manager.getSubtask(4);
        manager.getEpic(5);

        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
        System.out.println();

       manager.deleteEpic(1);

        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
        System.out.println();




    }
}
