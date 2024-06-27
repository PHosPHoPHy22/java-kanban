package handles;

import adapter.DurationAdapter;
import adapter.LocalDateAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exceptions.NotFoundException;
import exceptions.ValidationException;
import models.HttpError;
import managers.TaskManager;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

public abstract class BaseHttpHandler implements HttpHandler {
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    protected final TaskManager taskManager;

    public BaseHttpHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    protected void writeResponse(HttpExchange exchange, String responseString, int responseCode) throws IOException {
        try {
            byte[] resp = responseString.getBytes(DEFAULT_CHARSET);
            exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
            exchange.sendResponseHeaders(responseCode, resp.length);
            exchange.getResponseBody().write(resp);
        } finally {
            exchange.close();
        }
    }

    protected void sendResponseCode(HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(204, -1);
        exchange.close();
    }

    public static Gson getGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter());
        return gsonBuilder.create();
    }

    protected Optional<Integer> getId(String pathPart) {
        int id = Integer.parseInt(pathPart);
        if (id == -1) {
            throw new NotFoundException("Нет данных с id: " + id);
        } else {
            return Optional.of(id);
        }
    }

    protected int getIdFromPath(String pathPart) {
        Optional<Integer> idFromPath = getId(pathPart);
        return idFromPath.orElse(-1);
    }

    protected void exception(HttpExchange exchange, Exception exception) {
        try {
            if (exception instanceof NotFoundException) {
                writeResponse(exchange, HttpError.HTTP_NOT_FOUND_ERROR.toString(), 404);
            } else if (exception instanceof ValidationException) {
                writeResponse(exchange, HttpError.HTTP_VALIDATION_ERROR.toString(), 406);
            } else if (exception instanceof JsonSyntaxException) {
                writeResponse(exchange, HttpError.HTTP_FORMAT_ERROR.toString(), 400);
            } else {
                exception.printStackTrace(System.out);
                writeResponse(exchange, HttpError.HTTP_INTERNAL_ERROR.toString(), 500);
            }
        } catch (IOException e) {
            e.printStackTrace(System.out);
        }
    }
}