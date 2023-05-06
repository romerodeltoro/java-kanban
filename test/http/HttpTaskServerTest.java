package http;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.taskmanager.managers.HttpTaskManager;
import com.taskmanager.managers.Managers;
import com.taskmanager.server.HttpTaskServer;
import com.taskmanager.server.KVServer;
import com.taskmanager.tasks.Epic;
import com.taskmanager.tasks.Subtask;
import com.taskmanager.tasks.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.taskmanager.tasks.Task.setCounter;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class HttpTaskServerTest {

    private KVServer kvServer = new KVServer();
    private HttpTaskManager taskManager;
    private HttpTaskServer taskServer;
    private final Gson gson = Managers.getGson();
    private final Task task = new Task("Task", "Description of task", 10,
                               LocalDateTime.of(2023,5,2,20,20,20));
    private final Epic epic = new Epic("Epic", "Description of epic");
    private final Subtask subtask = new Subtask("Subtask1", "Description of subtask", 2,333,
            LocalDateTime.of(2023,5,4,20,20,20));

    public HttpTaskServerTest() throws IOException {
    }

    @BeforeEach
    void init() throws IOException {
        kvServer.start();
        taskManager = (HttpTaskManager) Managers.getDefault();
        taskServer = new HttpTaskServer(taskManager);

        taskManager.createTask(task);
        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask);
        taskManager.getTask(1);
        taskManager.getEpic(2);
        taskManager.getSubtask(3);

        taskServer.start();
    }

    @AfterEach
    void tearDown() {
        taskServer.stop();
        kvServer.stop();
        setCounter(0);
    }

    @Test
    @DisplayName("���������� �����")
    void shouldGetTasks() throws IOException, InterruptedException {
     //   taskManager.createTask(task);
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/task");
        HttpRequest request = HttpRequest.newBuilder().GET().uri(uri).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Type taskType = new TypeToken<ArrayList<Task>>() {}.getType();
        List<Task> actual = gson.fromJson(response.body(), taskType);

        assertNotNull(actual, "����� �� ������������");
        assertEquals(1, actual.size(), "�� ������ ���������� ������");
        assertEquals(task, actual.get(0), "����� �� ���������");
    }

    @Test
    @DisplayName("���������� �����")
    void shouldGetEpics() throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/epic");
        HttpRequest request = HttpRequest.newBuilder().GET().uri(uri).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Type epicType = new TypeToken<ArrayList<Epic>>() {}.getType();
        List<Epic> actual = gson.fromJson(response.body(), epicType);

        assertNotNull(actual, "����� �� ������������");
        assertEquals(1, actual.size(), "�� ������ ���������� ������");
        assertEquals(epic, actual.get(0), "����� �� ���������");
    }

    @Test
    @DisplayName("���������� ��������")
    void shouldGetSubtasks() throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/subtask");
        HttpRequest request = HttpRequest.newBuilder().GET().uri(uri).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Type subtaskType = new TypeToken<ArrayList<Subtask>>() {}.getType();
        List<Subtask> actual = gson.fromJson(response.body(), subtaskType);

        assertNotNull(actual, "�������� �� ������������");
        assertEquals(1, actual.size(), "�� ������ ���������� ���������");
        assertEquals(subtask, actual.get(0), "�������� �� ���������");
    }

    @Test
    @DisplayName("���������� �������")
    void shouldGetHistory() throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/history");
        HttpRequest request = HttpRequest.newBuilder().GET().uri(uri).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Type type = new TypeToken<ArrayList<Task>>() {}.getType();
        List<Task> actual = gson.fromJson(response.body(), type);

        assertNotNull(actual, "�������� �� ������������");
        assertEquals(3, actual.size(), "�� ������ ���������� ���������");
    }

    @Test
    @DisplayName("���������� ����� �� id")
    void shouldGetTaskById() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/task/?id=1");
        HttpRequest request = HttpRequest.newBuilder().GET().uri(uri).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Type taskType = new TypeToken<Task>() {}.getType();
        Task actual = gson.fromJson(response.body(), taskType);

        assertNotNull(actual, "����� �� ������������");
        assertEquals(task, actual, "����� �� ���������");
    }
    @Test
    @DisplayName("���������� ���� �� id")
    void shouldGetEpicById() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/epic/?id=2");
        HttpRequest request = HttpRequest.newBuilder().GET().uri(uri).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Type epicType = new TypeToken<Epic>() {}.getType();
        Epic actual = gson.fromJson(response.body(), epicType);

        assertNotNull(actual, "���� �� ������������");
        assertEquals(epic, actual, "����� �� ���������");
    }
    @Test
    @DisplayName("���������� ������� �� id")
    void shouldGetSubtaskById() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/subtask/?id=3");
        HttpRequest request = HttpRequest.newBuilder().GET().uri(uri).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Type subtaskType = new TypeToken<Subtask>() {}.getType();
        Subtask actual = gson.fromJson(response.body(), subtaskType);

        assertNotNull(actual, "������� �� ������������");
        assertEquals(subtask, actual, "�������� �� ���������");
    }
    @Test
    @DisplayName("���������� �������� �� id �����")
    void shouldGetEpicBySubtaskId() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/subtask/epic/?id=2");
        HttpRequest request = HttpRequest.newBuilder().GET().uri(uri).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Type subtaskType = new TypeToken<ArrayList<Subtask>>() {}.getType();
        List<Subtask> actual = gson.fromJson(response.body(), subtaskType);

        assertNotNull(actual, "�������� �� ������������");
        assertEquals(1, actual.size(), "�� ������ ���������� ���������");
        assertEquals(subtask, actual.get(0), "�������� �� ���������");
    }

    @Test
    @DisplayName("������� ��� �����")
    void shouldDeleteTasks() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/task");
        HttpRequest request = HttpRequest.newBuilder().DELETE().uri(uri).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }
    @Test
    @DisplayName("������� ��� �����")
    void shouldDeleteEpics() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/epic");
        HttpRequest request = HttpRequest.newBuilder().DELETE().uri(uri).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }
    @Test
    @DisplayName("������� ��� ��������")
    void shouldDeleteSubTasks() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/subtask");
        HttpRequest request = HttpRequest.newBuilder().DELETE().uri(uri).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    @DisplayName("������� ���� �� id")
    void shouldDeleteTaskById() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/task/?id=1");
        HttpRequest request = HttpRequest.newBuilder().DELETE().uri(uri).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    @DisplayName("������� ���� �� id")
    void shouldDeleteEpicById() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/epic/?id=2");
        HttpRequest request = HttpRequest.newBuilder().DELETE().uri(uri).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    @DisplayName("������� ������� �� id")
    void shouldDeleteSubTaskById() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/subtask/?id=3");
        HttpRequest request = HttpRequest.newBuilder().DELETE().uri(uri).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }
    @Test
    @DisplayName("��������� �����")
    void shouldPostToUpdateTask() throws IOException, InterruptedException {
        Task updatedTask = new Task(1,"UpdatedTask", "Description of updatedtask", 10,
                LocalDateTime.of(2023,5,2,20,20,20));
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/task/");
        String json = gson.toJson(updatedTask);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().POST(body).uri(uri).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        assertEquals(Task.Status.IN_PROGRESS, taskManager.getTask(1).getStatus(), "����� �� ����������");
    }
    @Test
    @DisplayName("������� ����� �����")
    void shouldPostNewTask() throws IOException, InterruptedException {
        Task newTask = new Task();
        newTask.setTitle("NewTask");
        newTask.setDescription("Description of newtask");
        newTask.setDuration(90);
        newTask.setStartTime(LocalDateTime.of(2023,5,5,10,10,10));
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/task/");
        String json = gson.toJson(newTask);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().POST(body).uri(uri).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

    }
    @Test
    @DisplayName("��������� �������")
    void shouldPostToUpdateSubtask() throws IOException, InterruptedException {
        Subtask updatedSubtask = new Subtask(3,"UpdatedSubtask", "Description of updatedSubtask",
                2, 50,  LocalDateTime.of(2023,5,5,20,20,0));
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/subtask/");
        String json = gson.toJson(updatedSubtask);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().POST(body).uri(uri).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        assertEquals(Subtask.Status.IN_PROGRESS, taskManager.getSubtask(3).getStatus(), "������� �� ���������");
        assertEquals(Epic.Status.IN_PROGRESS, taskManager.getEpic(2).getStatus(), "���� �� ���������");
    }
    @Test
    @DisplayName("������� ����� �������")
    void shouldPostNewSubTask() throws IOException, InterruptedException {
        Subtask newSubTask = new Subtask();
        newSubTask.setTitle("NewSubtask");
        newSubTask.setDescription("Description of newSubtask");
        newSubTask.setEpicId(2);
        newSubTask.setDuration(90);
        newSubTask.setStartTime(LocalDateTime.of(2023,5,5,10,10,10));
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/task/");
        String json = gson.toJson(newSubTask);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().POST(body).uri(uri).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }
    @Test
    @DisplayName("������� ����� �����")
    void shouldPostNewEpic() throws IOException, InterruptedException {
        Epic newEpic = new Epic();
        newEpic.setTitle("NewEpic");
        newEpic.setDescription("Description of newEpic");
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/epic/");
        String json = gson.toJson(newEpic);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().POST(body).uri(uri).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

    }
}
