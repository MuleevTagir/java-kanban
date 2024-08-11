package manager;

import exception.IntersectionTimeException;
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

    Task addTask(Task task) throws IntersectionTimeException;

    Subtask addSubtask(Epic epic, Subtask subtask) throws IntersectionTimeException;

    Epic addEpic(Epic epic);

    void updateTask(Task task) throws IntersectionTimeException;

    void updateSubtask(Subtask subtask) throws IntersectionTimeException;

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