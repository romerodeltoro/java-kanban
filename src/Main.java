
import com.taskmanager.client.KVTaskClient;
import com.taskmanager.managers.HttpTaskManager;
import com.taskmanager.server.HttpTaskServer;
import com.taskmanager.server.KVServer;
import com.taskmanager.tasks.*;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) throws IOException {


        KVServer server = new KVServer();
        server.start();
        HttpTaskManager manager = new HttpTaskManager();
        HttpTaskServer httpTaskServer = new HttpTaskServer(manager);
        httpTaskServer.start();

        manager.createTask(new Task("task1", "Купить автомобиль", 10,
                LocalDateTime.of(2020,2,20,20,20,20)));
        manager.createEpic(new Epic("new Epic1", "Новый Эпик"));
        manager.createSubtask(new Subtask("New Subtask", "Подзадача", 2, 30,
                LocalDateTime.of(2222,2,22,22,22,22)));
        manager.getTask(1);
        manager.getTask(1);
        manager.getEpic(2);

        System.out.println("/*******/");

       /* manager.getTasks().forEach(System.out::println);

        HttpTaskManager manager2 = new HttpTaskManager();
        manager2.load();

        manager2.getTasks().forEach(System.out::println);
        manager2.getEpics().forEach(System.out::println);
        manager2.getSubTasks().forEach(System.out::println);
        manager2.getHistory().forEach(System.out::println);*/
    }

}
