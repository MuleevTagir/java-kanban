package server;

import com.google.gson.Gson;
import manager.TaskManager;
import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.GsonHelper;
import utils.Managers;
import utils.Server;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ServerTest {
    TaskManager manager;
    Server server;
    String URL = "http://localhost:8080";
    Gson gson = GsonHelper.getGson();
    HttpClient client = HttpClient.newHttpClient();

    public ServerTest() throws IOException {
    }

    @BeforeEach
    public void setUp() throws IOException {
        manager = Managers.getDefault();
        server = new Server(manager);
        server.start();
    }

    @AfterEach
    public void shutDown() {
        this.server.stop();
    }

    @Test
    public void testAddTask() throws IOException, InterruptedException {
        String taskJson = this.gson.toJson(new Task(0, "Test 2", "Testing task 2", Status.NEW, Duration.ofMinutes(5), LocalDateTime.now()));

        URI url = URI.create(URL + "/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();

        HttpResponse<String> response = this.client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        List<Task> tasksFromManager = this.manager.getTaskList();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Test 2", tasksFromManager.get(0).getTitle(), "Некорректное имя задачи");
    }

    @Test
    public void testAddEpic() throws IOException, InterruptedException {
        String epicJson = this.gson.toJson(new Epic());

        URI url = URI.create(URL + "/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(epicJson)).build();

        HttpResponse<String> response = this.client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        List<Epic> epicsFromManager = manager.getEpicList();

        assertNotNull(epicsFromManager, "Эпики не возвращаются");
        assertEquals(1, epicsFromManager.size(), "Некорректное количество эпика");
    }

    @Test
    public void testSubtaskAdd() throws IOException, InterruptedException {
        String epicJson = this.gson.toJson(new Epic());

        URI url = URI.create(URL + "/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(epicJson)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        List<Epic> epicsFromManager = manager.getEpicList();

        assertNotNull(epicsFromManager, "Эпики не возвращаются");
        assertEquals(1, epicsFromManager.size(), "Некорректное количество эпика");

        Subtask subtask = new Subtask();
        subtask.setLinkList(List.of(epicsFromManager.get(0).getId()));
        String subtaskJson = this.gson.toJson(subtask);

        url = URI.create(URL + "/subtasks");
        request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(subtaskJson)).build();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        List<Subtask> subtaskFromManager = manager.getSubtaskList();
        assertNotNull(subtaskFromManager, "Подзадачи не возвращаются");
        assertEquals(1, subtaskFromManager.size(), "Некорректное количество подзадачи");
    }
}