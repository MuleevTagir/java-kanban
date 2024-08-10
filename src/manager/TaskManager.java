package manager;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.List;
import java.util.Set;

public interface TaskManager {

    List<Task> getTaskList();

    List<Subtask> getSubtaskList();

    List<Epic> getEpicList();

    Task getTaskById(Integer id);

    Subtask getSubtaskById(Integer id);

    Epic getEpicById(Integer id);

    Task addTask(Task task);

    Subtask addSubtask(Epic epic, Subtask subtask);

    Epic addEpic(Epic epic);

    void updateTask(Task task);

    void updateSubtask(Subtask subtask);

    void updateEpic(Epic epic);

    void removeTaskById(Integer id);

    void removeSubtaskById(Integer id);

    void removeEpicById(Integer id);

    void removeTaskAll();

    void removeSubtaskAll();

    void removeEpicAll();

    List<Subtask> getSubtaskListByEpic(Epic epic);

    List<Task> getHistory();

    Set<Task> getPrioritizedTasks();
}