package handles;

import com.sun.net.httpserver.HttpExchange;
import server.HttpMethod;
import managers.TaskManager;

import java.io.IOException;

public class PrioritizedHandler extends BaseHttpHandler {


    public PrioritizedHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) {
        try (exchange) {
            try {
                if (exchange.getRequestMethod().equals(HttpMethod.GET)) {
                    writeResponse(exchange, getGson().toJson(taskManager.getPrioritizedTasks()), 200);
                } else
                    writeResponse(exchange, "Ошибка при обработке запроса", 404);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}