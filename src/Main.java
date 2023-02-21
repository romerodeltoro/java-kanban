import com.taskmanager.*;
import com.taskmanager.tasks.*;

public class Main {

    public static void main(String[] args) {

        printTest();

    }

    // ����
    static void printTest() {

        TaskManager manager = Managers.getDefault();

        manager.createEpic(new Epic("���� 1", "��������"));
        manager.createSubtask(new Subtask("��������� 1", "��������", 1));
        manager.createSubtask(new Subtask("��������� 2", "��������", 1));
        manager.createSubtask(new Subtask("��������� 3", "��������", 1));
        manager.createEpic(new Epic("���� 2", "��������"));


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
