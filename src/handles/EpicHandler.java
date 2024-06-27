package handles;

import com.sun.net.httpserver.HttpExchange;
import exceptions.NotFoundException;
import models.Epic;
import models.HttpError;
import server.HttpMethod;
import managers.TaskManager;

import java.nio.charset.StandardCharsets;

public class EpicHandler extends BaseHttpHandler {

    public EpicHandler(TaskManager taskManager) {
        super(taskManager);
    }

    public void handle(HttpExchange exchange) {
        System.out.println("Началась обработка Эпика");

        String path = exchange.getRequestURI().getPath();
        String method = exchange.getRequestMethod();
        String[] pathParts = path.split("/");

        try {
            switch (method) {
                case HttpMethod.GET:
                    if (pathParts.length == 2) {
                        writeResponse(exchange, getGson().toJson(taskManager.getEpics()), 200);
                    } else if (pathParts.length == 3) {
                        int epicId = getIdFromPath(pathParts[2]);
                        writeResponse(exchange, getGson().toJson(taskManager.getEpicById(epicId)), 200);
                    } else if (pathParts.length == 4) {
                        int epicId = getIdFromPath(pathParts[2]);
                        writeResponse(exchange, getGson().toJson(taskManager.getAllSubtasksByEpic(epicId)), 200);
                    } else {
                        writeResponse(exchange, HttpError.HTTP_NOT_FOUND_ERROR.toString(), 404);
                    }
                    break;
                case HttpMethod.POST:
                    String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                    Epic epic = getGson().fromJson(body, Epic.class);
                    if (epic.getSubtasksForThisEpic() == null) {
                        writeResponse(exchange, "Эпик должен иметь подзадачи", 400);
                        return;
                    }
                    try {
                        taskManager.getEpicById(epic.getId());
                        writeResponse(exchange, "Эпик с таким ID уже существует", 409);
                    } catch (NotFoundException e) {
                        taskManager.saveEpic(epic);
                        writeResponse(exchange, "Эпик создан", 201);
                    }
                    break;
                case HttpMethod.DELETE:
                    int epicId = getIdFromPath(pathParts[2]);
                    if (epicId == -1) {
                        writeResponse(exchange, "Такого эпика нет", 404);
                    } else {
                        taskManager.deleteEpicById(epicId);
                        sendResponseCode(exchange);
                    }
                    break;
                default:
                    writeResponse(exchange, HttpError.HTTP_NOT_FOUND_ERROR.toString(), 405);
            }
        } catch (Exception e) {
            exception(exchange, e);
        }
    }
}