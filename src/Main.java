import com.taskmanager.*;
import com.taskmanager.tasks.*;

public class Main {

    public static void main(String[] args) {

        printTest();

    }

    // ����
    static void printTest() {

        TaskManager manager = Managers.getDefault();

        manager.createTask(new Task("������ 1", "��������"));
        manager.createTask(new Task("������ 2", "��������"));
        manager.createEpic(new Epic("���� 1", "��������"));
        manager.createSubtask(new Subtask("��������� 1", "��������", 3));
        manager.createSubtask(new Subtask("��������� 2", "��������", 3));

        manager.createEpic(new Epic("���� 2", "��������"));
        manager.createSubtask(new Subtask("��������� 3", "��������", 6));

        manager.getTask(1);
        manager.getTask(2);
        manager.getEpic(3);
        manager.getSubtask(4);
        manager.getSubtask(5);
        manager.getEpic(6);
        manager.getSubtask(7);
        manager.getTask(1);
        manager.getTask(2);
        manager.getEpic(3);

        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
        System.out.println();

        manager.getSubtask(4);
        manager.getSubtask(5);
        manager.getEpic(6);
        manager.getSubtask(7);

        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }

    }
}
