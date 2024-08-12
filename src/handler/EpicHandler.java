package handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import manager.TaskManager;
import model.Epic;
import utils.GsonHelper;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class EpicHandler extends BaseHttpHandler {

    public EpicHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String[] params = exchange.getRequestURI().getPath().split("/");
        Optional<Integer> id = Optional.empty();
        if (params.length > 2) {
            id = Optional.of(Integer.parseInt(params[2]));
        }

        String method = exchange.getRequestMethod();
        Gson gson = GsonHelper.getGson();
        switch (method) {
            case "GET": {
                if (id.isPresent()) {
                    if (params.length > 3 && params[3].equals("subtasks")) {
                        Epic epic = this.taskManager.getEpicById(id.get());
                        this.sendTextCode200(exchange, gson.toJson(this.taskManager.getSubtaskListByEpic(epic)));
                    } else {
                        this.sendTextCode200(exchange, gson.toJson(this.taskManager.getEpicById(id.get())));
                    }
                } else {
                    this.sendTextCode200(exchange, gson.toJson(this.taskManager.getEpicList()));
                }
                break;
            }
            case "POST": {
                InputStream inputStream = exchange.getRequestBody();
                String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                Epic epic = gson.fromJson(body, Epic.class);
                if (this.taskManager.getEpicById(epic.getId()) != null) {
                    this.taskManager.updateEpic(epic);
                    this.sendTextCode201(exchange, gson.toJson(epic));
                } else {
                    epic = this.taskManager.addEpic(epic);
                    this.sendTextCode200(exchange, gson.toJson(epic));
                }
            }
            break;
            case "DELETE": {
                if (id.isPresent()) {
                    this.taskManager.removeEpicById(id.get());
                    this.sendTextCode200(exchange, "Success.");
                } else {
                    this.sendNotFoundCode404(exchange, "ID not found.");
                }
                break;
            }
            default: {
                this.sendNotFoundCode404(exchange, "Action not found.");
            }
        }
    }
}