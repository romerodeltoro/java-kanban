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

        manager.makeTask(new Task("������ 1", "��������"));
        manager.makeTask(new Task("������ 2", "��������"));
        manager.makeEpic(new Epic("���� 1", "��������"));
        manager.makeSubtask(new Subtask("��������� 1", "��������", 3));
        manager.makeSubtask(new Subtask("��������� 2", "��������", 3));

        manager.makeEpic(new Epic("���� 2", "��������"));
        manager.makeSubtask(new Subtask("��������� 3", "��������", 6));

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
