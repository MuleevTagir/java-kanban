package manager;

import exception.IntersectionTimeException;
import manager.impl.InMemoryHistoryManager;
import model.Status;
import model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

class InMemoryHistoryManagerTest {
    private final HistoryManager historyManager = new InMemoryHistoryManager();

    @Test
    void add() {
        Task expectedTask = new Task();

        this.historyManager.add(expectedTask);

        Assertions.assertEquals(expectedTask, this.historyManager.getHistory().get(0));
    }

    @Test
    void remove() {
        this.historyManager.add(new Task(123, "Название123", "Описание123", Status.NEW, Duration.ofDays(1), LocalDateTime.now()));
        Assertions.assertEquals(1, this.historyManager.getHistory().size());
        this.historyManager.remove(123);

        List<Task> taskList = this.historyManager.getHistory();

        Assertions.assertEquals(0, taskList.size());
    }

    @Test
    void getHistory() {
        this.historyManager.add(new Task(123, "Название123", "Описание123", Status.NEW,Duration.ofDays(1), LocalDateTime.now()));
        this.historyManager.add(new Task(116, "Название116", "Описание116", Status.IN_PROGRESS,Duration.ofDays(1), LocalDateTime.now()));
        this.historyManager.add(new Task(123, "Название123", "Описание123", Status.NEW,Duration.ofDays(1), LocalDateTime.now()));
        this.historyManager.add(new Task(123, "Название123", "Описание123", Status.NEW,Duration.ofDays(1), LocalDateTime.now()));
        this.historyManager.remove(116);
        this.historyManager.add(new Task(116, "Название116", "Описание116", Status.IN_PROGRESS,Duration.ofDays(1), LocalDateTime.now()));
        this.historyManager.add(new Task(945, "Название547", "Описание547", Status.DONE,Duration.ofDays(1), LocalDateTime.now()));
        this.historyManager.remove(945);

        List<Task> taskList = this.historyManager.getHistory();

        Assertions.assertEquals(2, taskList.size());
    }
}