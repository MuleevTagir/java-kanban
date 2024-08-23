package utils;

import com.sun.net.httpserver.HttpServer;
import handler.*;
import manager.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Server {
    private final HttpServer httpServer;
    private static final Integer PORT = 8080;

    public Server(TaskManager taskManager) throws IOException {
        this.httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);

        httpServer.createContext("/tasks", new TasksHandler(taskManager));
        httpServer.createContext("/subtasks", new SubtasksHandler(taskManager));
        httpServer.createContext("/epics", new EpicHandler(taskManager));
        httpServer.createContext("/history", new HistoryHandler(taskManager));
        httpServer.createContext("/prioritized", new PrioritizedHandler(taskManager));
    }

    public void start() {
        this.httpServer.start();
    }

    public void stop() {
        this.httpServer.stop(1);
    }
}