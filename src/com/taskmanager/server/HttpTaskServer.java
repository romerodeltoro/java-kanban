package com.taskmanager.server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import com.taskmanager.managers.HttpTaskManager;
import com.taskmanager.managers.Managers;
import com.taskmanager.tasks.Epic;
import com.taskmanager.tasks.Subtask;
import com.taskmanager.tasks.Task;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.regex.Pattern;

import static com.taskmanager.managers.LocalDateTimeAdapter.formatter;
import static java.nio.charset.StandardCharsets.UTF_8;

public class HttpTaskServer {

    private final HttpTaskManager taskManager;
    private final HttpServer server;
    private final Gson gson = Managers.getGson();
    private static final int PORT = 8080;

    public HttpTaskServer(HttpTaskManager manager) throws IOException {
        this.taskManager = manager;
        this.server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        server.createContext("/tasks", this::handleTasks);
        server.createContext("/tasks/task/", this::handleTasksId);
        server.createContext("/tasks/epic/", this::handleEpicsId);
        server.createContext("/tasks/subtask/", this::handleSubTasksId);
    }


    public static void main(String[] args) throws IOException, InterruptedException {
        KVServer server = new KVServer();
        server.start();
        HttpTaskServer httpTaskServer = new HttpTaskServer((HttpTaskManager) Managers.getDefault());
        httpTaskServer.start();
        httpTaskServer.taskManager.createTask(new Task("task1", "������ ����������", 10,
                LocalDateTime.of(2020, 2, 20, 20, 20, 20)));
        httpTaskServer.taskManager.createEpic(new Epic("new Epic1", "����� ����"));
        httpTaskServer.taskManager.createSubtask(new Subtask("New Subtask", "���������", 2, 30,
                LocalDateTime.of(2222, 2, 22, 22, 22, 22)));
        httpTaskServer.taskManager.createSubtask(new Subtask("New Subtask2", "���������2", 2, 50,
                LocalDateTime.of(2222, 2, 23, 23, 23, 23)));
        httpTaskServer.taskManager.createEpic(new Epic("new Epic2", "����� ����"));
        httpTaskServer.taskManager.createTask(new Task("newTask1", "������ ����������", 10,
                LocalDateTime.of(2020, 2, 20, 20, 30, 20)));

        httpTaskServer.taskManager.getTask(1);
        httpTaskServer.taskManager.getTask(1);
        httpTaskServer.taskManager.getSubtask(3);



        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create("http://localhost:8080/tasks/task");
        HttpRequest request = HttpRequest.newBuilder().GET().uri(uri).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());

    }

    private void handleTasks(HttpExchange exchange) {
        try {
            String path = exchange.getRequestURI().getPath();
            String requestMethod = exchange.getRequestMethod();
            switch (requestMethod) {
                case "GET": {
                    if (Pattern.matches("^/tasks/$", path)) {
                        String response = gson.toJson(taskManager.getPrioritizedTasks());
                        sendText(exchange, response);
                        break;
                    }
                    if (Pattern.matches("^/tasks/\\S+$", path)) {
                        String pathTask = path.replaceFirst("/tasks/", "");
                        if (pathTask.equals("task")) {
                            System.out.println("���������� �����");
                            String response = gson.toJson(taskManager.getTasks());
                            sendText(exchange, response);
                            break;
                        } else if (pathTask.equals("epic")) {
                            System.out.println("���������� �����");
                            String response = gson.toJson(taskManager.getEpics());
                            sendText(exchange, response);
                            break;
                        } else if (pathTask.equals("subtask")) {
                            System.out.println("���������� ��������");
                            String response = gson.toJson(taskManager.getSubTasks());
                            sendText(exchange, response);
                            break;
                        } else if (pathTask.equals("history")) {
                            System.out.println("���������� �������");
                            String response = gson.toJson(taskManager.getHistory());
                            sendText(exchange, response);
                            break;
                        }
                    }
                }
                case "DELETE": {
                    if (Pattern.matches("^/tasks/\\S+$", path)) {
                        String pathTask = path.replaceFirst("/tasks/", "");
                        if (pathTask.equals("task")) {
                            System.out.println("������� ��� �����");
                            taskManager.deleteAllTasks();
                            sendText(exchange, "��� ����� �������");
                            break;
                        } else if (pathTask.equals("epic")) {
                            System.out.println("������� ��� �����");
                            taskManager.deleteAllEpics();
                            sendText(exchange, "��� ����� �������");
                            break;
                        } else if (pathTask.equals("subtask")) {
                            System.out.println("������� ��� ��������");
                            taskManager.deleteAllSubtasks();
                            sendText(exchange, "��� �������� �������");
                            break;
                        }
                    }
                }
                default: {
                    System.out.println("Waiting GET or DELETE request but got - " + requestMethod);
                    exchange.sendResponseHeaders(405, 0);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            exchange.close();
        }
    }

    private void handleTasksId(HttpExchange exchange) {
        try {
            String path = exchange.getRequestURI().getPath();
            String stringId = exchange.getRequestURI().getQuery();
            String requestMethod = exchange.getRequestMethod();
            switch (requestMethod) {
                case "GET": {
                    if (Pattern.matches("^/?id=\\d+$", stringId)) {
                        String pathId = stringId.replaceFirst("id=", "");
                        int id = parsePathId(pathId);
                        if (id != -1) {
                            System.out.println("���������� ����� � id: " + id);
                            String response = gson.toJson(taskManager.getTask(id));
                            sendText(exchange, response);
                        } else {
                            System.out.println("Received incorrect id - " + id);
                            exchange.sendResponseHeaders(405, 0);
                        }
                        break;
                    }
                }
                case "POST": {
                    if (Pattern.matches("^/tasks/task/$", path)) {

                        try {
                            JsonObject jsonObject = gson.fromJson(readText(exchange), JsonObject.class);
                            String title = jsonObject.get("title").getAsString();
                            String description = jsonObject.get("description").getAsString();
                            int duration = jsonObject.get("duration").getAsInt();
                            String startTime = jsonObject.get("startTime").getAsString();
                            LocalDateTime localDateTime = LocalDateTime.parse(startTime, formatter);

                            if (jsonObject.has("id") && jsonObject.get("id").getAsInt() > 0) {
                                System.out.println("��������� ����");
                                int id = jsonObject.get("id").getAsInt();
                                Task task = new Task(title, description, duration, localDateTime);
                                taskManager.updateTask(id, task);
                                sendText(exchange, "���� ��������");
                            } else {
                                Task task = new Task(title, description, duration, localDateTime);
                                System.out.println("������� ����� ���� � id: " + task.getId());
                                String response = gson.toJson(taskManager.createTask(task));
                                sendText(exchange, "���� ������" + response);
                            }
                            break;
                        } catch (IOException e) {
                            System.out.println("�� ����� ���������� ������� �������� ������.");
                        }
                    }
                }
                case "DELETE": {
                    if (Pattern.matches("^/?id=\\d+$", stringId)) {
                        String pathId = stringId.replaceFirst("id=", "");
                        int id = parsePathId(pathId);
                        if (id != -1) {
                            System.out.println("������� ����� � id: " + id);
                            taskManager.deleteTask(id);
                            sendText(exchange, "����� � id: " + id + " �������");
                            break;
                        } else {
                            System.out.println("Received incorrect id - " + id);
                            exchange.sendResponseHeaders(405, 0);
                            break;
                        }
                    }
                }
                default: {
                    System.out.println("Waiting another request but got - " + requestMethod);
                    exchange.sendResponseHeaders(405, 0);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            exchange.close();
        }
    }

    private void handleEpicsId(HttpExchange exchange) {
        try {
            String path = exchange.getRequestURI().getPath();
            String stringId = exchange.getRequestURI().getQuery();
            String requestMethod = exchange.getRequestMethod();
            switch (requestMethod) {
                case "GET": {
                    if (Pattern.matches("^/?id=\\d+$", stringId)) {
                        String pathId = stringId.replaceFirst("id=", "");
                        int id = parsePathId(pathId);
                        if (id != -1) {
                            System.out.println("���������� ���� � id: " + id);
                            String response = gson.toJson(taskManager.getEpic(id));
                            sendText(exchange, response);
                            break;
                        } else {
                            System.out.println("Received incorrect id - " + id);
                            exchange.sendResponseHeaders(405, 0);
                            break;
                        }
                    }
                }
                case "POST": {
                    if (Pattern.matches("^/tasks/epic/$", path)) {

                        try {
                            JsonObject jsonObject = gson.fromJson(readText(exchange), JsonObject.class);
                            String title = jsonObject.get("title").getAsString();
                            String description = jsonObject.get("description").getAsString();

                            Epic epic = new Epic(title, description);
                            System.out.println("������� ����� ���� � id: " + epic.getId());
                            String response = gson.toJson(taskManager.createEpic(epic));
                            sendText(exchange, "���� ������ \n" + response);

                            break;
                        } catch (IOException e) {
                            System.out.println("�� ����� ���������� ������� �������� ������.");
                        }
                    }
                }
                case "DELETE": {
                    if (Pattern.matches("^/?id=\\d+$", stringId)) {
                        String pathId = stringId.replaceFirst("id=", "");
                        int id = parsePathId(pathId);
                        if (id != -1) {
                            System.out.println("������� ���� � id: " + id);
                            taskManager.deleteEpic(id);
                            sendText(exchange, "���� � id: " + id + " ������");
                            break;
                        } else {
                            System.out.println("Received incorrect id - " + id);
                            exchange.sendResponseHeaders(405, 0);
                            break;
                        }
                    }
                }
                default: {
                    System.out.println("Waiting another request but got - " + requestMethod);
                    exchange.sendResponseHeaders(405, 0);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            exchange.close();
        }
    }

    private void handleSubTasksId(HttpExchange exchange) {
        try {
            String path = exchange.getRequestURI().getPath();
            String stringId = exchange.getRequestURI().getQuery();
            String requestMethod = exchange.getRequestMethod();
            switch (requestMethod) {
                case "GET": {
                    if (Pattern.matches("^/tasks/subtask/epic/$", path)) {
                        String pathId = stringId.replaceFirst("id=", "");
                        int id = parsePathId(pathId);
                        if (id != -1) {
                            System.out.println("���������� ������ ��������� �� id �����: " + id);
                            String response = gson.toJson(taskManager.getEpicSubtask(id));
                            sendText(exchange, response);
                            break;
                        } else {
                            System.out.println("Received incorrect id - " + id);
                            exchange.sendResponseHeaders(405, 0);
                            break;
                        }
                    }
                    if (Pattern.matches("^/?id=\\d+$", stringId)) {
                        String pathId = stringId.replaceFirst("id=", "");
                        int id = parsePathId(pathId);
                        if (id != -1) {
                            System.out.println("���������� ������� � id: " + id);
                            String response = gson.toJson(taskManager.getSubtask(id));
                            sendText(exchange, response);
                            break;
                        } else {
                            System.out.println("Received incorrect id - " + id);
                            exchange.sendResponseHeaders(405, 0);
                            break;
                        }
                    }
                }
                case "POST": {
                    if (Pattern.matches("^/tasks/subtask/$", path)) {

                        try {
                            JsonObject jsonObject = gson.fromJson(readText(exchange), JsonObject.class);
                            String title = jsonObject.get("title").getAsString();
                            String description = jsonObject.get("description").getAsString();
                            int epicId = jsonObject.get("epicId").getAsInt();
                            int duration = jsonObject.get("duration").getAsInt();
                            String startTime = jsonObject.get("startTime").getAsString();
                            LocalDateTime localDateTime = LocalDateTime.parse(startTime, formatter);

                            if (jsonObject.has("id") && jsonObject.get("id").getAsInt() > 0) {
                                System.out.println("��������� �������");
                                int id = jsonObject.get("id").getAsInt();
                                Subtask subtask = new Subtask(title, description, epicId, duration, localDateTime);
                                taskManager.updateSubtask(id, subtask);
                                sendText(exchange, "������� ��������");
                            } else {
                                Subtask subtask = new Subtask(title, description, epicId, duration, localDateTime);
                                System.out.println("������� ����� ������� � id: " + subtask.getId());
                                String response = gson.toJson(taskManager.createSubtask(subtask));
                                sendText(exchange, "������� ������ \n" + response);
                            }
                            break;
                        } catch (IOException e) {
                            System.out.println("�� ����� ���������� ������� �������� ������.");
                        }
                    }
                }
                case "DELETE": {
                    if (Pattern.matches("^/?id=\\d+$", stringId)) {
                        String pathId = stringId.replaceFirst("id=", "");
                        int id = parsePathId(pathId);
                        if (id != -1) {
                            System.out.println("������� ������� � id: " + id);
                            taskManager.deleteSubtask(id);
                            sendText(exchange, "������� � id: " + id + " ������");
                            break;
                        } else {
                            System.out.println("Received incorrect id - " + id);
                            exchange.sendResponseHeaders(405, 0);
                            break;
                        }
                    }
                }
                default: {
                    System.out.println("Waiting another request but got - " + requestMethod);
                    exchange.sendResponseHeaders(405, 0);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            exchange.close();
        }
    }

    public void start() {
        System.out.println("Started TaskServer: " + PORT);
        server.start();
    }

    public void stop() {
        server.stop(0);
        System.out.println("Stopped server on port " + PORT);
    }

    private int parsePathId(String path) {
        try {
            return Integer.parseInt(path);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private String readText(HttpExchange h) throws IOException {
        return new String(h.getRequestBody().readAllBytes(), UTF_8);
    }

    private void sendText(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(200, resp.length);
        h.getResponseBody().write(resp);
    }
}
