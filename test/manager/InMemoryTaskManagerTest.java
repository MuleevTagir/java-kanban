package manager;

import exception.IntersectionTimeException;
import model.Status;
import model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import utils.Managers;

import java.time.Duration;
import java.time.LocalDateTime;

class InMemoryTaskManagerTest {

    private final TaskManager taskManager = Managers.getDefault();

    //проверьте, что InMemoryTaskManager действительно добавляет задачи разного типа и может найти их по id;
    @Test
    void shouldCorrectSomeTasksAndFindById() throws IntersectionTimeException {
        Task taskNew = new Task(0, "Заголовок", "Описание", Status.NEW, Duration.ofDays(1), LocalDateTime.now());
        Task taskInProgress = new Task(1, "Заголовок", "Описание", Status.IN_PROGRESS, Duration.ofDays(1), LocalDateTime.now().plusDays(3));
        Task taskDone = new Task(2, "Заголовок", "Описание", Status.DONE, Duration.ofDays(1), LocalDateTime.now().plusDays(6));

        taskManager.addTask(taskNew);
        taskManager.addTask(taskInProgress);
        taskManager.addTask(taskDone);

        Assertions.assertEquals(Status.NEW, taskManager.getTaskById(0).getStatus());
        Assertions.assertEquals(Status.IN_PROGRESS, taskManager.getTaskById(1).getStatus());
        Assertions.assertEquals(Status.DONE, taskManager.getTaskById(2).getStatus());
    }

    @Test
    void addWithThrowException() {
        IntersectionTimeException thrown = Assertions.assertThrows(IntersectionTimeException.class, () -> {
            Task expectedTask1 = new Task();
            Task expectedTask2 = new Task();
            this.taskManager.addTask(expectedTask1);
            this.taskManager.addTask(expectedTask2);

        }, "Время уже занято другой задачей");

        Assertions.assertEquals("Время уже занято другой задачей", thrown.getMessage());
    }
}