package utils;

import manager.impl.FileBackedTaskManager;
import manager.impl.InMemoryHistoryManager;
import manager.impl.InMemoryTaskManager;
import manager.TaskManager;
import manager.HistoryManager;

import java.io.File;

public class Managers {

    public static TaskManager getDefault() {
        return new InMemoryTaskManager(Managers.getDefaultHistory());
    }

    public static FileBackedTaskManager getFileBackedTaskManager(File filename) {
        return FileBackedTaskManager.loadFromFile(filename);
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
