package test.manager;

import manager.TaskManager;
import model.Status;
import model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import utils.Managers;

class InMemoryTaskManagerTest {

    private final TaskManager taskManager = Managers.getDefault();

    //проверьте, что InMemoryTaskManager действительно добавляет задачи разного типа и может найти их по id;
    @Test
    void test1() {
        Task taskNew = new Task("Заголовок", "Описание",  Status.NEW);
        Task taskInProgress = new Task("Заголовок", "Описание",  Status.IN_PROGRESS);
        Task taskDone = new Task("Заголовок", "Описание",  Status.DONE);

        taskManager.addTask(taskNew);
        taskManager.addTask(taskInProgress);
        taskManager.addTask(taskDone);

        Assertions.assertEquals(Status.NEW, taskManager.getTaskById(0).getStatus());
        Assertions.assertEquals(Status.IN_PROGRESS, taskManager.getTaskById(1).getStatus());
        Assertions.assertEquals(Status.DONE, taskManager.getTaskById(2).getStatus());
    }
}