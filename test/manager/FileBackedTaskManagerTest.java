package manager;

import exception.IntersectionTimeException;
import manager.impl.FileBackedTaskManager;
import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.Managers;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

class FileBackedTaskManagerTest {
    private File tmpFile;

    @BeforeEach
    void setUp() throws IOException {
        this.tmpFile = File.createTempFile("data", null);
    }

    @Test
    void loadFromEmptyFile() throws IntersectionTimeException {
        FileBackedTaskManager fileBackedTaskManager = null;
        fileBackedTaskManager = Managers.getFileBackedTaskManager(this.tmpFile);

        Assertions.assertEquals(0, fileBackedTaskManager.getEpicList().size());
        Assertions.assertEquals(0, fileBackedTaskManager.getSubtaskList().size());
        Assertions.assertEquals(0, fileBackedTaskManager.getTaskList().size());
    }

    @Test
    void loadFromFile() throws IntersectionTimeException {
        TaskManager taskManager = Managers.getFileBackedTaskManager(this.tmpFile);
        Epic epic1 = taskManager.addEpic(new Epic());
        Task task1 = taskManager.addTask(new Task(0, "Заголовок", "Описание", Status.NEW, Duration.ofDays(1), LocalDateTime.now().plusDays(1)));
        Task task2 = taskManager.addTask(new Task(1, "Заголовок", "Описание", Status.NEW, Duration.ofDays(1), LocalDateTime.now().plusDays(2)));
        Subtask subtask1 = taskManager.addSubtask(epic1, new Subtask(4, "Подзадача", "Описание", Status.NEW, Duration.ofDays(1), LocalDateTime.now().plusDays(3)));
        Subtask subtask2 = taskManager.addSubtask(epic1, new Subtask(3, "Подзадача", "Описание", Status.NEW, Duration.ofDays(1), LocalDateTime.now().plusDays(4)));
        Epic epic2 = taskManager.addEpic(new Epic());
        Subtask subtask3 = taskManager.addSubtask(epic2, new Subtask(7, "Подзадача", "Описание", Status.IN_PROGRESS, Duration.ofDays(1), LocalDateTime.now().plusDays(5)));

        TaskManager taskManagerLoadFromFile = Managers.getFileBackedTaskManager(this.tmpFile);

        Assertions.assertEquals(taskManager.getEpicList().size(), taskManagerLoadFromFile.getEpicList().size());
        Assertions.assertEquals(taskManager.getSubtaskList().size(), taskManagerLoadFromFile.getSubtaskList().size());
        Assertions.assertEquals(taskManager.getTaskList().size(), taskManagerLoadFromFile.getTaskList().size());
    }
}