import manager.TaskManager;
import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import utils.Managers;

public class Main {

    public static void main(String[] args) {

        TaskManager taskManager = Managers.getDefault();

        Task task1 = taskManager.addTask(new Task());
        Task task2 = taskManager.addTask(new Task());

        Epic epic1 = taskManager.addEpic(new Epic());
        //Subtask subtask1 = taskManager.addSubtask(epic1, new Subtask(4, "Подзадача", "Описание", Status.NEW));
        Subtask subtask2 = taskManager.addSubtask(epic1, new Subtask());

        Epic epic2 = taskManager.addEpic(new Epic());
        //Subtask subtask3 = taskManager.addSubtask(epic2, new Subtask(7, "Подзадача", "Описание", Status.IN_PROGRESS));

        printAllTasks(taskManager);
    }

    private static void printAllTasks(TaskManager manager) {
        System.out.println("Задачи:");
        for (Task task : manager.getTaskList()) {
            System.out.println(task);
        }
        System.out.println("Эпики:");
        for (Epic epic : manager.getEpicList()) {
            System.out.println(epic);

            for (Task task : manager.getSubtaskListByEpic(epic)) {
                System.out.println("--> " + task);
            }
        }
        System.out.println("Подзадачи:");
        for (Task subtask : manager.getSubtaskList()) {
            System.out.println(subtask);
        }

        System.out.println("История:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
    }
}
