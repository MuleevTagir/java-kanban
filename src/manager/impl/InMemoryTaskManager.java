package manager.impl;

import exception.IntersectionTimeException;
import manager.HistoryManager;
import manager.TaskManager;
import model.*;

import java.util.*;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {
    private final HashMap<Integer, Task> taskHashMap;
    private final HashMap<Integer, Subtask> subtaskHashMap;
    private final HashMap<Integer, Epic> epicHashMap;
    private final Set<Task> prioritizedTasks;
    protected final HistoryManager historyManager;
    private Integer lastId;

    public InMemoryTaskManager(HistoryManager historyManager) {
        this.lastId = 0;
        this.taskHashMap = new HashMap<>();
        this.subtaskHashMap = new HashMap<>();
        this.epicHashMap = new HashMap<>();
        this.historyManager = historyManager;

        this.prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));
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

    private boolean isIntersectionTime(Task task) {
        for (Task el : this.prioritizedTasks) {
            if (task.getStartTime().equals(el.getStartTime()) || task.getEndTime().equals(el.getEndTime())) {
                return true;
            }
            if (task.getStartTime().isAfter(el.getStartTime()) && task.getStartTime().isBefore(el.getEndTime())) {
                return true;
            }
            if (task.getEndTime().isAfter(el.getStartTime()) && task.getEndTime().isBefore(el.getEndTime())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Task addTask(Task task) {
        try {
            if (isIntersectionTime(task)) {
                throw new IntersectionTimeException("Время уже занято другой задачей");
            }
        } catch (IntersectionTimeException e) {
            System.out.println(e.getMessage());
        }

        task.setId(getNextId());
        this.taskHashMap.put(task.getId(), task);
        this.prioritizedTasks.add(task);

        return task;
    }

    @Override
    public Subtask addSubtask(Epic epic, Subtask subtask) {
        try {
            if (isIntersectionTime(subtask)) {
                throw new IntersectionTimeException("Время уже занято другой задачей");
            }
        } catch (IntersectionTimeException e) {
            System.out.println(e.getMessage());
        }

        subtask.setId(getNextId());
        this.subtaskHashMap.put(subtask.getId(), subtask);
        epic.addSubTask(subtask);
        this.prioritizedTasks.add(subtask);

        this.updateStatusEpic(epic.getId());
        if (epic.getSubTaskList().isEmpty()) {
            epic.setStartTime(subtask.getStartTime());
            epic.setDuration(subtask.getDuration());
            epic.setEndTime(subtask.getEndTime());
        } else {
            if (epic.getStartTime().isBefore(subtask.getStartTime())) {
                epic.setEndTime(subtask.getEndTime());
            } else {
                epic.setStartTime(subtask.getStartTime());
            }
            epic.setDuration(epic.getDuration().plus(subtask.getDuration()));
        }

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
            try {
                if (isIntersectionTime(task)) {
                    throw new IntersectionTimeException("Время уже занято другой задачей");
                }
            } catch (IntersectionTimeException e) {
                System.out.println(e.getMessage());
            }
            this.taskHashMap.put(task.getId(), task);
            this.prioritizedTasks.remove(task);
            this.prioritizedTasks.add(task);
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (this.subtaskHashMap.containsKey(subtask.getId())) {
            try {
                if (isIntersectionTime(subtask)) {
                    throw new IntersectionTimeException("Время уже занято другой задачей");
                }
            } catch (IntersectionTimeException e) {
                System.out.println(e.getMessage());
            }
            this.subtaskHashMap.put(subtask.getId(), subtask);
            this.updateStatusEpic(subtask.getLinkList().get(0));
            this.prioritizedTasks.remove(subtask);
            this.prioritizedTasks.add(subtask);
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
        this.prioritizedTasks.remove(this.taskHashMap.get(id));
    }

    @Override
    public void removeSubtaskById(Integer id) {
        if (this.subtaskHashMap.containsKey(id)) {
            Task task = this.getTaskById(id);
            Epic epic = this.getEpicById(task.getLinkList().get(0));
            if (epic != null) {
                epic.removeSubtaskById(id);

                epic.setDuration(epic.getDuration().minus(task.getDuration()));
                List<Subtask> subtaskList = this.getSubtaskListByEpic(epic);
                epic.setStartTime(subtaskList.get(0).getStartTime());
                epic.setEndTime(subtaskList.get(subtaskList.size() - 1).getEndTime());

                this.subtaskHashMap.remove(id);
                this.historyManager.remove(id);
                this.updateStatusEpic(epic.getId());
            }
            this.prioritizedTasks.remove(this.subtaskHashMap.get(id));
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
            this.prioritizedTasks.remove(this.taskHashMap.get(id));
        }
        this.taskHashMap.clear();
    }

    @Override
    public void removeSubtaskAll() {
        for (Integer id : this.subtaskHashMap.keySet()) {
            this.historyManager.remove(id);
            this.prioritizedTasks.remove(this.subtaskHashMap.get(id));
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
        return epic.getSubTaskList()
                .stream()
                .map(this::getSubtaskById)
                .sorted(Comparator.comparing(Task::getStartTime))
                .collect(Collectors.toList());
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

    @Override
    public Set<Task> getPrioritizedTasks() {
        return this.prioritizedTasks;
    }
}