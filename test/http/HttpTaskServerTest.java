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
    @DisplayName("Возвращаем Таски")
    void shouldGetTasks() throws IOException, InterruptedException {
     //   taskManager.createTask(task);
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/task");
        HttpRequest request = HttpRequest.newBuilder().GET().uri(uri).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Type taskType = new TypeToken<ArrayList<Task>>() {}.getType();
        List<Task> actual = gson.fromJson(response.body(), taskType);

        assertNotNull(actual, "Таски не возвращаются");
        assertEquals(1, actual.size(), "Не верное количество тасков");
        assertEquals(task, actual.get(0), "Таски не совпадают");
    }

    @Test
    @DisplayName("Возвращаем Эпики")
    void shouldGetEpics() throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/epic");
        HttpRequest request = HttpRequest.newBuilder().GET().uri(uri).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Type epicType = new TypeToken<ArrayList<Epic>>() {}.getType();
        List<Epic> actual = gson.fromJson(response.body(), epicType);

        assertNotNull(actual, "Эпики не возвращаются");
        assertEquals(1, actual.size(), "Не верное количество эпиков");
        assertEquals(epic, actual.get(0), "Эпики не совпадают");
    }

    @Test
    @DisplayName("Возвращаем Субтаски")
    void shouldGetSubtasks() throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/subtask");
        HttpRequest request = HttpRequest.newBuilder().GET().uri(uri).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Type subtaskType = new TypeToken<ArrayList<Subtask>>() {}.getType();
        List<Subtask> actual = gson.fromJson(response.body(), subtaskType);

        assertNotNull(actual, "Субтаски не возвращаются");
        assertEquals(1, actual.size(), "Не верное количество субтасков");
        assertEquals(subtask, actual.get(0), "Субтаски не совпадают");
    }

    @Test
    @DisplayName("Возвращаем историю")
    void shouldGetHistory() throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/history");
        HttpRequest request = HttpRequest.newBuilder().GET().uri(uri).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Type type = new TypeToken<ArrayList<Task>>() {}.getType();
        List<Task> actual = gson.fromJson(response.body(), type);

        assertNotNull(actual, "Субтаски не возвращаются");
        assertEquals(3, actual.size(), "Не верное количество субтасков");
    }

    @Test
    @DisplayName("Возвращаем таску по id")
    void shouldGetTaskById() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/task/?id=1");
        HttpRequest request = HttpRequest.newBuilder().GET().uri(uri).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Type taskType = new TypeToken<Task>() {}.getType();
        Task actual = gson.fromJson(response.body(), taskType);

        assertNotNull(actual, "Таска не возвращаются");
        assertEquals(task, actual, "Таски не совпадают");
    }
    @Test
    @DisplayName("Возвращаем эпик по id")
    void shouldGetEpicById() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/epic/?id=2");
        HttpRequest request = HttpRequest.newBuilder().GET().uri(uri).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Type epicType = new TypeToken<Epic>() {}.getType();
        Epic actual = gson.fromJson(response.body(), epicType);

        assertNotNull(actual, "Эпик не возвращаются");
        assertEquals(epic, actual, "Эпики не совпадают");
    }
    @Test
    @DisplayName("Возвращаем субтаск по id")
    void shouldGetSubtaskById() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/subtask/?id=3");
        HttpRequest request = HttpRequest.newBuilder().GET().uri(uri).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Type subtaskType = new TypeToken<Subtask>() {}.getType();
        Subtask actual = gson.fromJson(response.body(), subtaskType);

        assertNotNull(actual, "Субтаск не возвращаются");
        assertEquals(subtask, actual, "Субтаски не совпадают");
    }
    @Test
    @DisplayName("Возвращаем Субтаски по id Эпика")
    void shouldGetEpicBySubtaskId() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/subtask/epic/?id=2");
        HttpRequest request = HttpRequest.newBuilder().GET().uri(uri).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Type subtaskType = new TypeToken<ArrayList<Subtask>>() {}.getType();
        List<Subtask> actual = gson.fromJson(response.body(), subtaskType);

        assertNotNull(actual, "Субтаски не возвращаются");
        assertEquals(1, actual.size(), "Не верное количество субтасков");
        assertEquals(subtask, actual.get(0), "Субтаски не совпадают");
    }

    @Test
    @DisplayName("Удаляем все таски")
    void shouldDeleteTasks() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/task");
        HttpRequest request = HttpRequest.newBuilder().DELETE().uri(uri).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }
    @Test
    @DisplayName("Удаляем все эпики")
    void shouldDeleteEpics() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/epic");
        HttpRequest request = HttpRequest.newBuilder().DELETE().uri(uri).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }
    @Test
    @DisplayName("Удаляем все субтаски")
    void shouldDeleteSubTasks() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/subtask");
        HttpRequest request = HttpRequest.newBuilder().DELETE().uri(uri).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    @DisplayName("Удаляем таск по id")
    void shouldDeleteTaskById() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/task/?id=1");
        HttpRequest request = HttpRequest.newBuilder().DELETE().uri(uri).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    @DisplayName("Удаляем эпик по id")
    void shouldDeleteEpicById() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/epic/?id=2");
        HttpRequest request = HttpRequest.newBuilder().DELETE().uri(uri).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    @DisplayName("Удаляем субтаск по id")
    void shouldDeleteSubTaskById() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/subtask/?id=3");
        HttpRequest request = HttpRequest.newBuilder().DELETE().uri(uri).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }
    @Test
    @DisplayName("Обновляем таску")
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

        assertEquals(Task.Status.IN_PROGRESS, taskManager.getTask(1).getStatus(), "Таска не изменилась");
    }
    @Test
    @DisplayName("Создаем новую таску")
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
    @DisplayName("Обновляем субтаск")
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

        assertEquals(Subtask.Status.IN_PROGRESS, taskManager.getSubtask(3).getStatus(), "Субтаск не изменился");
        assertEquals(Epic.Status.IN_PROGRESS, taskManager.getEpic(2).getStatus(), "Эпик не изменился");
    }
    @Test
    @DisplayName("Создаем новый субтаск")
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
    @DisplayName("Создаем новую таску")
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
