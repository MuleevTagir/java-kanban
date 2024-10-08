package handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import manager.TaskManager;
import utils.GsonHelper;

import java.io.IOException;

public class HistoryHandler extends BaseHttpHandler {

    public HistoryHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Gson gson = GsonHelper.getGson();
        this.sendTextCode200(exchange, gson.toJson(this.taskManager.getHistory()));
    }
}