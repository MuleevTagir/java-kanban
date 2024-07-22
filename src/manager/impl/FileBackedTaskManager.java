package manager.impl;

import exception.ManagerSaveException;
import manager.HistoryManager;
import model.*;
import utils.Managers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;


public class FileBackedTaskManager extends InMemoryTaskManager {

    private final File file;

    public FileBackedTaskManager(HistoryManager historyManager, File file) {
        super(historyManager);
        this.file = file;
    }

    @Override
    public Task addTask(Task task) {
        super.addTask(task);
        save();
        return task;
    }

    @Override
    public Subtask addSubtask(Epic epic, Subtask subtask) {
        super.addSubtask(epic, subtask);
        save();
        return subtask;
    }

    @Override
    public Epic addEpic(Epic epic) {
        super.addEpic(epic);
        save();
        return epic;
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void removeTaskById(Integer id) {
        super.removeTaskById(id);
        save();
    }

    @Override
    public void removeSubtaskById(Integer id) {
        super.removeSubtaskById(id);
        save();
    }

    @Override
    public void removeEpicById(Integer id) {
        super.removeEpicById(id);
        save();
    }

    @Override
    public void removeTaskAll() {
        super.removeTaskAll();
        save();
    }

    @Override
    public void removeSubtaskAll() {
        super.removeSubtaskAll();
        save();
    }

    @Override
    public void removeEpicAll() {
        super.removeEpicAll();
        save();
    }

    private void save() {
        try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter(file))) {
            fileWriter.write("id,type,name,status,description,link" + System.lineSeparator());
            for (Epic epic : this.getEpicList()) {
                fileWriter.write(epic.toString());
            }

            for (Subtask subtask : this.getSubtaskList()) {
                fileWriter.write(subtask.toString());
            }

            for (Task task : this.getTaskList()) {
                fileWriter.write(task.toString());
            }
        } catch (IOException exception) {
            throw new ManagerSaveException(String.format("%s: %s", "Ошибка чтения файла", this.file.getName()));
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager fileBackedTaskManager = Managers.getFileBackedTaskManager(file);

        String data = "";
        try {
            data = Files.readString(file.toPath());
        } catch (IOException exception) {
            throw new ManagerSaveException(String.format("%s: %s", "Ошибка чтения файла", file.getName()));
        }

        if (data.isEmpty()) {
            return fileBackedTaskManager;
        }

        Map<Integer, Epic> epicMap = new HashMap<>();
        Map<Integer, Subtask> subtaskMap = new HashMap<>();
        Map<Integer, Task> taskMap = new HashMap<>();

        for (String line : data.split(System.lineSeparator())) {
            String[] arrayString = line.split(",");
            if (arrayString.length < 4) {
                throw new ManagerSaveException("Ошибка данных.");
            }
            switch (arrayString[1]) {
                case "TASK":
                    Task task = Task.fromString(line);
                    taskMap.put(task.getId(), task);
                    break;
                case "SUBTASK":
                    Subtask subtask = Subtask.fromString(line);
                    subtaskMap.put(subtask.getId(), subtask);
                    break;
                case "EPIC":
                    Epic epic = Epic.fromString(line);
                    epicMap.put(epic.getId(), epic);
                    break;
            }
        }

        for (Integer epicId : epicMap.keySet()) {
            fileBackedTaskManager.addEpic(epicMap.get(epicId));
        }

        for (Integer subtaskId : subtaskMap.keySet()) {
            Subtask subtask = subtaskMap.get(subtaskId);
            fileBackedTaskManager.addSubtask(epicMap.get(subtask.getLinkList().get(0)), subtask);
        }

        for (Integer taskId : taskMap.keySet()) {
            fileBackedTaskManager.addTask(taskMap.get(taskId));
        }

        return fileBackedTaskManager;
    }
}