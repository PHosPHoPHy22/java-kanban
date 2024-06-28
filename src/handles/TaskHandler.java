package handles;

import com.sun.net.httpserver.HttpExchange;
import exceptions.NotFoundException;
import models.Task;
import server.HttpMethod;
import managers.TaskManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class TaskHandler extends BaseHttpHandler {
    public TaskHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String path = exchange.getRequestURI().getPath();
            String method = exchange.getRequestMethod();
            String[] params = path.split("/");
            try {
                switch (method) {
                    case HttpMethod.GET:
                        if (params.length == 2) {

                            writeResponse(exchange, getGson().toJson(taskManager.getTasks()), 200);

                        } else if (params.length == 3) {

                            writeResponse(exchange, getGson().toJson(taskManager.getTaskById(getIdFromPath(params[2]))), 200);
                        }
                        break;
                    case HttpMethod.POST:
                        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                        Task task = getGson().fromJson(body, Task.class);
                        try {
                            taskManager.getTaskById(task.getId());
                            taskManager.updateTask(task);
                            writeResponse(exchange, "Задача обновлена", 201);
                        } catch (NotFoundException e) {
                            taskManager.saveTask(task);
                            writeResponse(exchange, "Задача создана", 201);
                        }
                        break;
                    case HttpMethod.DELETE:
                        int id = getIdFromPath(params[2]);

                        if (id == -1) {
                            writeResponse(exchange, "Нет данных с таким номером", 404);
                        } else {
                            taskManager.deleteTaskById(id);
                            sendResponseCode(exchange);
                        }
                        break;
                }
            } catch (Exception exception) {
                exception(exchange, exception);
            }
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }
}