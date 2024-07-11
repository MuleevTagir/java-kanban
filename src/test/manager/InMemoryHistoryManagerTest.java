package test.manager;

import manager.HistoryManager;
import manager.impl.InMemoryHistoryManager;
import model.Status;
import model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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
        this.historyManager.add(new Task(123, "Название123", "Описание123", Status.NEW));
        this.historyManager.remove(123);

        List<Task> taskList = this.historyManager.getHistory();

        Assertions.assertEquals(0, taskList.size());
    }

    @Test
    void getHistory() {
        this.historyManager.add(new Task(123, "Название123", "Описание123", Status.NEW));
        this.historyManager.add(new Task(116, "Название116", "Описание116", Status.IN_PROGRESS));
        this.historyManager.add(new Task(123, "Название123", "Описание123", Status.NEW));
        this.historyManager.add(new Task(123, "Название123", "Описание123", Status.NEW));
        this.historyManager.remove(116);
        this.historyManager.add(new Task(116, "Название116", "Описание116", Status.IN_PROGRESS));
        this.historyManager.add(new Task(945, "Название547", "Описание547", Status.DONE));
        this.historyManager.remove(945);

        List<Task> taskList = this.historyManager.getHistory();

        Assertions.assertEquals(2, taskList.size());
    }
}