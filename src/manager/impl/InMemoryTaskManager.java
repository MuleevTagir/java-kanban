package manager.impl;

import manager.HistoryManager;
import manager.TaskManager;
import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private final HashMap<Integer, Task> taskHashMap;
    private final HashMap<Integer, Subtask> subtaskHashMap;
    private final HashMap<Integer, Epic> epicHashMap;
    private final HistoryManager historyManager;
    private Integer lastId;

    public InMemoryTaskManager(HistoryManager historyManager) {
        this.lastId = 0;
        this.taskHashMap = new HashMap<>();
        this.subtaskHashMap = new HashMap<>();
        this.epicHashMap = new HashMap<>();
        this.historyManager = historyManager;
    }

    private Integer getNextId() {
        return this.lastId++;
    }

    @Override
    public List<Task> getTaskList() {
        return new ArrayList<>(this.taskHashMap.values());
    }

    @Override
    public List<Subtask> getSubtaskList() {
        return new ArrayList<>(this.subtaskHashMap.values());
    }

    @Override
    public List<Epic> getEpicList() {
        return new ArrayList<>(this.epicHashMap.values());
    }

    @Override
    public Task getTaskById(Integer id) {
        if (this.taskHashMap.containsKey(id)) {
            this.historyManager.add(this.taskHashMap.get(id).clone());
            return this.taskHashMap.get(id);
        }
        return null;
    }

    @Override
    public Subtask getSubtaskById(Integer id) {
        if (this.subtaskHashMap.containsKey(id)) {
            this.historyManager.add(this.subtaskHashMap.get(id).clone());
            return subtaskHashMap.get(id);
        }
        return null;
    }

    @Override
    public Epic getEpicById(Integer id) {
        if (this.epicHashMap.containsKey(id)) {
            this.historyManager.add(this.epicHashMap.get(id).clone());
            return epicHashMap.get(id);
        }
        return null;
    }

    @Override
    public Task addTask(Task task) {
        task.setId(getNextId());
        this.taskHashMap.put(task.getId(), task);
        return task;
    }

    @Override
    public Subtask addSubtask(Epic epic, Subtask subtask) {
        subtask.setId(getNextId());
        this.subtaskHashMap.put(subtask.getId(), subtask);
        epic.addSubTask(subtask);
        this.updateStatusEpic(subtask.getEpicId());
        return subtask;
    }

    @Override
    public Epic addEpic(Epic epic) {
        epic.setId(getNextId());
        this.epicHashMap.put(epic.getId(), epic);
        return epic;
    }

    @Override
    public void updateTask(Task task) {
        if (this.taskHashMap.containsKey(task.getId())) {
            this.taskHashMap.put(task.getId(), task);
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (this.subtaskHashMap.containsKey(subtask.getId())) {
            this.subtaskHashMap.put(subtask.getId(), subtask);
            this.updateStatusEpic(subtask.getEpicId());
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        if (this.epicHashMap.containsKey(epic.getId())) {
            this.epicHashMap.put(epic.getId(), epic);
            this.updateStatusEpic(epic.getId());
        }
    }

    @Override
    public void removeTaskById(Integer id) {
        this.taskHashMap.remove(id);
        this.historyManager.remove(id);
    }

    @Override
    public void removeSubtaskById(Integer id) {
        if (this.subtaskHashMap.containsKey(id)) {
            Integer epicId = this.getSubtaskById(id).getEpicId();
            this.subtaskHashMap.remove(id);
            this.historyManager.remove(id);

            Epic epic = this.getEpicById(epicId);
            if (epic != null) {
                epic.removeSubtaskById(id);
            }
            this.updateStatusEpic(epicId);
        }
    }

    @Override
    public void removeEpicById(Integer id) {
        if (this.epicHashMap.containsKey(id)) {
            for (Integer subtaskId : this.getEpicById(id).getSubTaskList()) {
                this.subtaskHashMap.remove(subtaskId);
                this.historyManager.remove(id);
            }
            this.epicHashMap.remove(id);
        }
    }

    @Override
    public void removeTaskAll() {
        for (Integer id : this.taskHashMap.keySet()) {
            this.historyManager.remove(id);
        }
        this.taskHashMap.clear();
    }

    @Override
    public void removeSubtaskAll() {
        for (Integer id : this.subtaskHashMap.keySet()) {
            this.historyManager.remove(id);
        }
        this.subtaskHashMap.clear();
    }

    @Override
    public void removeEpicAll() {
        for (Integer id : this.epicHashMap.keySet()) {
            this.historyManager.remove(id);
        }
        this.epicHashMap.clear();
    }

    @Override
    public List<Subtask> getSubtaskListByEpic(Epic epic) {
        List<Subtask> subtaskList = new ArrayList<>();
        for (Integer subtaskId : epic.getSubTaskList()) {
            subtaskList.add(this.subtaskHashMap.get(subtaskId));
        }
        return subtaskList;
    }

    private void updateStatusEpic(Integer epicId) {
        if (this.epicHashMap.containsKey(epicId)) {
            Epic epic = this.epicHashMap.get(epicId);
            if (epic.getSubTaskList().isEmpty()) {
                epic.setStatus(Status.NEW);
                return;
            }

            HashMap<String, Integer> subtaskGroupByStatus = new HashMap<>();
            for (Integer id : epic.getSubTaskList()) {
                String status = this.subtaskHashMap.get(id).getStatus().toString();
                if (subtaskGroupByStatus.containsKey(status)) {
                    subtaskGroupByStatus.put(status, subtaskGroupByStatus.get(status) + 1);
                } else {
                    subtaskGroupByStatus.put(status, 1);
                }
            }

            Integer countSubtask = epic.getSubTaskList().size();
            if (Objects.equals(subtaskGroupByStatus.get(Status.NEW.toString()), countSubtask)) {
                epic.setStatus(Status.NEW);
                return;
            }

            if (Objects.equals(subtaskGroupByStatus.get(Status.DONE.toString()), countSubtask)) {
                epic.setStatus(Status.DONE);
                return;
            }

            epic.setStatus(Status.IN_PROGRESS);
        }
    }

    @Override
    public List<Task> getHistory() {
        return this.historyManager.getHistory();
    }
}
