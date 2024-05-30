import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class TaskManager {
    private final HashMap<Integer, Task> taskHashMap;
    private final HashMap<Integer, Subtask> subtaskHashMap;
    private final HashMap<Integer, Epic> epicHashMap;
    private Integer lastId;

    public TaskManager() {
        this.lastId = 0;
        this.taskHashMap = new HashMap<>();
        this.subtaskHashMap = new HashMap<>();
        this.epicHashMap = new HashMap<>();
    }

    private Integer getNextId() {
        return lastId++;
    }

    public List<Task> getTaskList() {
        return new ArrayList<>(taskHashMap.values());
    }

    public List<Subtask> getSubtaskList() {
        return new ArrayList<>(subtaskHashMap.values());
    }

    public List<Epic> getEpicList() {
        return new ArrayList<>(epicHashMap.values());
    }

    //Получение по идентификатору
    public Task getTaskById(Integer id) {
        if (taskHashMap.containsKey(id)) {
            return taskHashMap.get(id);
        }
        return null;
    }

    public Subtask getSubtaskById(Integer id) {
        if (subtaskHashMap.containsKey(id)) {
            return subtaskHashMap.get(id);
        }
        return null;
    }

    public Epic getEpicById(Integer id) {
        if (epicHashMap.containsKey(id)) {
            return epicHashMap.get(id);
        }
        return null;
    }

    //Создание
    public Task addTask(Task task) {
        task.setId(getNextId());
        taskHashMap.put(task.getId(), task);
        return task;
    }

    public Subtask addSubtask(Epic epic, Subtask subtask) {
        subtask.setId(getNextId());
        subtaskHashMap.put(subtask.getId(), subtask);
        epic.addSubTask(subtask);
        this.updateStatusEpic(subtask.getEpicId());
        return subtask;
    }

    public Epic addEpic(Epic epic) {
        epic.setId(getNextId());
        epicHashMap.put(epic.getId(), epic);
        return epic;
    }

    //Обновление
    public void updateTask(Task task) {
        if (taskHashMap.containsKey(task.getId())) {
            taskHashMap.put(task.getId(), task);
        }
    }

    public void updateSubtask(Subtask subtask) {
        if (subtaskHashMap.containsKey(subtask.getId())) {
            subtaskHashMap.put(subtask.getId(), subtask);
            this.updateStatusEpic(subtask.getEpicId());
        }
    }

    public void updateEpic(Epic epic) {
        if (epicHashMap.containsKey(epic.getId())) {
            epicHashMap.put(epic.getId(), epic);
            this.updateStatusEpic(epic.getId());
        }
    }

    //Удаление по идентификатору
    public void removeTaskById(Integer id) {
        taskHashMap.remove(id);
    }

    public void removeSubtaskById(Integer id) {
        if (subtaskHashMap.containsKey(id)) {
            Integer epicId = this.getSubtaskById(id).getEpicId();
            subtaskHashMap.remove(id);

            Epic epic = this.getEpicById(epicId);
            if (epic != null) {
                epic.removeSubtaskById(id);
            }
            this.updateStatusEpic(epicId);
        }
    }

    public void removeEpicById(Integer id) {
        if (epicHashMap.containsKey(id)) {
            for (Integer subtaskId : this.getEpicById(id).getSubTaskList()) {
                this.subtaskHashMap.remove(subtaskId);
            }
            epicHashMap.remove(id);
        }
    }

    public void removeTaskAll() {
        taskHashMap.clear();
    }

    public void removeSubtaskAll() {
        subtaskHashMap.clear();
    }

    public void removeEpicAll() {
        epicHashMap.clear();
    }

    public List<Subtask> getSubtaskListByEpic(Epic epic) {
        List<Subtask> subtaskList = new ArrayList<>();
        for (Integer subtaskId : epic.getSubTaskList()) {
            subtaskList.add(subtaskHashMap.get(subtaskId));
        }
        return subtaskList;
    }

    private void updateStatusEpic(Integer epicId) {
        if (epicHashMap.containsKey(epicId)) {
            Epic epic = epicHashMap.get(epicId);
            if (epic.getSubTaskList().isEmpty()) {
                epic.setStatus(Status.NEW);
                return;
            }

            HashMap<String, Integer> subtaskGroupByStatus = new HashMap<>();
            for (Integer id : epic.getSubTaskList()) {
                String status = subtaskHashMap.get(id).getStatus().toString();
                if (subtaskGroupByStatus.containsKey(status)) {
                    subtaskGroupByStatus.put(status, subtaskGroupByStatus.get(status) + 1);
                } else {
                    subtaskGroupByStatus.put(status, 1);
                }
            }

            Integer countSubtask = epic.getSubTaskList().size();
            if (Objects.equals(subtaskGroupByStatus.get(Status.NEW.toString()), countSubtask)) {
                epicHashMap.get(epicId).setStatus(Status.NEW);
                return;
            }

            if (Objects.equals(subtaskGroupByStatus.get(Status.DONE.toString()), countSubtask)) {
                epicHashMap.get(epicId).setStatus(Status.DONE);
                return;
            }

            epicHashMap.get(epicId).setStatus(Status.IN_PROGRESS);
        }
    }
}
