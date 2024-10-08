package model;

import exception.IntersectionTimeException;
import manager.TaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import utils.Managers;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;


class TaskTest {

    private final TaskManager taskManager = Managers.getDefault();

    //проверьте, что экземпляры класса Task равны друг другу, если равен их id;
    @Test
    void shouldEqualsSomeTasksById() throws IntersectionTimeException {
        Task task = new Task();
        this.taskManager.addTask(task);
        Task expectedTask = new Task();
        expectedTask.setId(0);

        Task actualTask = this.taskManager.getTaskList().get(0);

        Assertions.assertEquals(expectedTask, actualTask);
    }

    //проверьте, что наследники класса Task равны друг другу, если равен их id;
    @Test
    void shouldEqualsSomeChildTasksById() throws IntersectionTimeException {
        Epic epic = new Epic();
        Subtask subtask = new Subtask();
        this.taskManager.addSubtask(epic, subtask);
        Subtask expectedSubtask = new Subtask();
        expectedSubtask.setId(0);

        Subtask actualSubtask = this.taskManager.getSubtaskList().get(0);

        Assertions.assertEquals(expectedSubtask, actualSubtask);
    }

    //cоздайте тест, в котором проверяется неизменность задачи (по всем полям) при добавлении задачи в менеджер
    @Test
    void shouldImmutableTasksAfterAdd() throws IntersectionTimeException {
        Task expectedTask = new Task(0, "Заголовок", "Описание", Status.IN_PROGRESS, Duration.ofDays(1), LocalDateTime.now());
        taskManager.addTask(expectedTask);

        Task actualTask = taskManager.getTaskList().get(0);

        Assertions.assertEquals(expectedTask.getId(), actualTask.getId());
        Assertions.assertEquals(expectedTask.getTitle(), actualTask.getTitle());
        Assertions.assertEquals(expectedTask.getDescription(), actualTask.getDescription());
        Assertions.assertEquals(expectedTask.getStatus(), actualTask.getStatus());
    }

    //проверьте, что задачи с заданным id и сгенерированным id не конфликтуют внутри менеджера;
    @Test
    void shouldGenerationNotConflictWithId() throws IntersectionTimeException {
        Task task1 = new Task(0, "Заголовок 1", "Описание 1", Status.IN_PROGRESS, Duration.ofDays(1), LocalDateTime.now());
        taskManager.addTask(task1);
        Task task2 = new Task(1, "Заголовок 2", "Описание 2", Status.DONE, Duration.ofDays(1), LocalDateTime.now().plusDays(3));
        task2.setId(taskManager.getTaskList().get(0).getId());
        taskManager.addTask(task2);

        Task actualTask1 = taskManager.getTaskById(0);
        Task actualTask2 = taskManager.getTaskById(1);

        Assertions.assertEquals(0, actualTask1.getId());
        Assertions.assertEquals(1, actualTask2.getId());
    }

    //убедитесь, что задачи, добавляемые в HistoryManager, сохраняют предыдущую версию задачи и её данных.
    @Test
    void shouldSafeSaveHistoryItem() throws IntersectionTimeException {
        Task task = new Task(0, "Заголовок", "Описание", Status.IN_PROGRESS, Duration.ofDays(1), LocalDateTime.now());
        taskManager.addTask(task);

        Task actualTask = taskManager.getTaskById(0);
        actualTask.setStatus(Status.DONE);
        Status actualStatus = taskManager.getHistory().get(0).getStatus();

        Assertions.assertEquals(Status.IN_PROGRESS, actualStatus);
    }
}