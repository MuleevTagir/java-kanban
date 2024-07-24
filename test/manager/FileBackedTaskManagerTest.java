package manager;

import manager.TaskManager;
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

class FileBackedTaskManagerTest {
    private File tmpFile;

    @BeforeEach
    void setUp() throws IOException {
        this.tmpFile = File.createTempFile("data", null);
    }

    @Test
    void loadFromEmptyFile() {
        FileBackedTaskManager fileBackedTaskManager = Managers.getFileBackedTaskManager(this.tmpFile);

        Assertions.assertEquals(0, fileBackedTaskManager.getEpicList().size());
        Assertions.assertEquals(0, fileBackedTaskManager.getSubtaskList().size());
        Assertions.assertEquals(0, fileBackedTaskManager.getTaskList().size());
    }

    @Test
    void loadFromFile() {
        TaskManager taskManager = Managers.getFileBackedTaskManager(this.tmpFile);
        Epic epic1 = taskManager.addEpic(new Epic());
        Task task1 = taskManager.addTask(new Task());
        Task task2 = taskManager.addTask(new Task());
        Subtask subtask1 = taskManager.addSubtask(epic1, new Subtask(4, "Подзадача", "Описание", Status.NEW));
        Subtask subtask2 = taskManager.addSubtask(epic1, new Subtask());
        Epic epic2 = taskManager.addEpic(new Epic());
        Subtask subtask3 = taskManager.addSubtask(epic2, new Subtask(7, "Подзадача", "Описание", Status.IN_PROGRESS));

        TaskManager taskManagerLoadFromFile = Managers.getFileBackedTaskManager(this.tmpFile);

        Assertions.assertEquals(taskManager.getEpicList().size(), taskManagerLoadFromFile.getEpicList().size());
        Assertions.assertEquals(taskManager.getSubtaskList().size(), taskManagerLoadFromFile.getSubtaskList().size());
        Assertions.assertEquals(taskManager.getTaskList().size(), taskManagerLoadFromFile.getTaskList().size());
    }
}