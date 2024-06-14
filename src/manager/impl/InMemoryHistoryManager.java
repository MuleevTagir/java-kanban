package manager.impl;

import manager.HistoryManager;
import model.Task;

import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final List<Task> historyList;
    private static final int MAX_COUNT_TASK = 10;

    public InMemoryHistoryManager() {
        this.historyList = new LinkedList<>();
    }

    @Override
    public void add(Task task) {
        if (historyList.size() == MAX_COUNT_TASK) {
            historyList.remove(0);
        }
        historyList.add(task.clone());
    }

    @Override
    public List<Task> getHistory() {
        return historyList;
    }
}
