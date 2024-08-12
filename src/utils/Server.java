package utils;

import com.sun.net.httpserver.HttpServer;
import handler.*;
import manager.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Server {
    private final TaskManager taskManager;
    private HttpServer httpServer;
    private static final Integer PORT = 8080;

    public Server(TaskManager taskManager) {
        this.taskManager = taskManager;

        try {
            this.httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
        httpServer.createContext("/tasks", new TasksHandler(this.taskManager));
        httpServer.createContext("/subtasks", new SubtasksHandler(this.taskManager));
        httpServer.createContext("/epics", new EpicHandler(this.taskManager));
        httpServer.createContext("/history", new HistoryHandler(this.taskManager));
        httpServer.createContext("/prioritized", new PrioritizedHandler(this.taskManager));
    }

    public void start() {
        this.httpServer.start();
    }

    public void stop() {
        this.httpServer.stop(1);
    }
}