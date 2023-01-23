import com.taskmanager.Epic;
import com.taskmanager.Manager;
import com.taskmanager.Subtask;
import com.taskmanager.Task;

public class Main {

    public static void main(String[] args) {

        printTest();

    }

    // ����
    static void printTest() {

        Manager manager = new Manager();

        manager.createTask(new Task("������ 1", "��������"));
        manager.createTask(new Task("������ 2", "��������"));
        manager.createEpic(new Epic("���� 1", "��������"));
        manager.createSubtask(new Subtask("��������� 1", "��������", 3));
        manager.createSubtask(new Subtask("��������� 2", "��������", 3));

        manager.createEpic(new Epic("���� 2", "��������"));
        manager.createSubtask(new Subtask("��������� 3", "��������", 6));

        manager.printTasksList();
        System.out.println("****************\n");


        manager.updateTask(2, new Task("������ 3",  "��������"));
        manager.updateSubtask(4, manager.subtasks.get(4));
        manager.updateSubtask(7, manager.subtasks.get(7));

        manager.printTasksList();
        System.out.println("****************\n");

        manager.deleteTask(2);
        manager.deleteEpic(6);

        manager.printTasksList();
    }
}
