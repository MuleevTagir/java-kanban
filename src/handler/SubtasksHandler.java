package handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import exception.IntersectionTimeException;
import manager.TaskManager;
import model.Epic;
import model.Subtask;
import typeadapter.DurationAdapter;
import typeadapter.LocalDateTimeAdapter;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

public class SubtasksHandler extends BaseHttpHandler {

    public SubtasksHandler(TaskManager taskManager) {
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
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationAdapter());
        Gson gson = gsonBuilder.create();
        switch (method) {
            case "GET": {
                if (id.isPresent()) {
                    this.sendTextCode200(exchange, gson.toJson(this.taskManager.getSubtaskById(id.get())));
                } else {
                    this.sendTextCode200(exchange, gson.toJson(this.taskManager.getSubtaskList()));
                }
                break;
            }
            case "POST": {
                InputStream inputStream = exchange.getRequestBody();
                String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                Subtask subtask = gson.fromJson(body, Subtask.class);
                if (this.taskManager.getSubtaskById(subtask.getId()) != null) {
                    try {
                        this.taskManager.updateSubtask(subtask);
                    } catch (IntersectionTimeException exception) {
                        this.sendHasInteractionsCode406(exchange, exception.getMessage());
                    }
                    this.sendTextCode201(exchange, gson.toJson(subtask));
                } else {
                    try {
                        Epic epic = this.taskManager.getEpicById(subtask.getLinkList().get(0));
                        subtask = this.taskManager.addSubtask(epic, subtask);
                    } catch (IntersectionTimeException exception) {
                        this.sendHasInteractionsCode406(exchange, exception.getMessage());
                    }
                    this.sendTextCode200(exchange, gson.toJson(subtask));
                }
            }
            break;
            case "DELETE": {
                if (id.isPresent()) {
                    this.taskManager.removeSubtaskById(id.get());
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