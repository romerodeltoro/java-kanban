package com.taskmanager;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.taskmanager.taskmanager.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {

    private final TaskManager taskManager;
    private final HttpServer httpServer;
    private final Gson gson;
    private static final int PORT = 8080;

    public HttpTaskServer() throws IOException {
        this.taskManager = Managers.getDefault();
        this.gson = new Gson();
        this.httpServer = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        httpServer.createContext("/tasks", new TasksHandler());
    }

    static class TasksHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());

        }

        private Endpoint getEndpoint(String requestPath, String requestMethod) {
            return Endpoint.UNKNOWN;
        }

        enum Endpoint {GET, POST, DELETE, UNKNOWN}
    }
}
