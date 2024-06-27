package server;

import com.sun.net.httpserver.HttpServer;
import handles.*;
import models.Epic;
import models.Status;
import models.Subtask;
import models.Task;
import managers.Managers;
import managers.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.LocalDateTime;

public class HttpTaskServer {
    private static final int DEFAULT_PORT = 8080;
    private final TaskManager taskManager;
    private final int port;
    private HttpServer server;

    public HttpTaskServer() {
        this(DEFAULT_PORT);
    }

    public HttpTaskServer(int port) {
        this(Managers.getDefault(), port);
    }

    public HttpTaskServer(TaskManager taskManager) {
        this(taskManager, DEFAULT_PORT);
    }

    public HttpTaskServer(TaskManager taskManager, int port) {
        this.taskManager = taskManager;
        this.port = port;
    }

    public void start() {
        try {
            server = HttpServer.create(new InetSocketAddress("localhost", port), 0);
            server.start();
            System.out.println("Сервер запущен. Порт: " + port);
            this.server.createContext("/tasks", new TaskHandler(taskManager));
            this.server.createContext("/epics", new EpicHandler(taskManager));
            this.server.createContext("/subtasks", new SubTaskHandler(taskManager));
            this.server.createContext("/history", new HistoryHandler(taskManager));
            this.server.createContext("/prioritized", new PrioritizedHandler(taskManager));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void stop() {
        server.stop(0);
        System.out.println("Сервер остановлен. Порт: " + port);
    }

    public static void main(String[] args) {


        TaskManager taskManager1 = Managers.getDefault();

        Task task = new Task("Задача", "Описание", Status.NEW);
        taskManager1.saveTask(task);

        HttpTaskServer server = new HttpTaskServer(taskManager1);
        server.start();


        Epic epic = new Epic("Эпик", "Описание эпика");
        taskManager1.saveEpic(epic);

        Subtask subTask = new Subtask("Подзадача", "Описание", Status.NEW, epic.getId(),
                LocalDateTime.of(2024, 7, 13, 18, 38, 20), 15);
        taskManager1.saveSubtask(subTask);

    }
}