package utils;

import manager.impl.InMemoryHistoryManager;
import manager.impl.InMemoryTaskManager;
import manager.TaskManager;
import manager.HistoryManager;

public class Managers {

    public static TaskManager getDefault() {
        return new InMemoryTaskManager(Managers.getDefaultHistory());
    }

    private static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
