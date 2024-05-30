public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        Task task1 = taskManager.addTask(new Task());
        Task task2 = taskManager.addTask(new Task());

        Epic epic1 = taskManager.addEpic(new Epic());
        Subtask subtask1 = taskManager.addSubtask(epic1, new Subtask("Подзадача", "Описание", Status.NEW));
        Subtask subtask2 = taskManager.addSubtask(epic1, new Subtask());

        Epic epic2 = taskManager.addEpic(new Epic());
        Subtask subtask3 = taskManager.addSubtask(epic2, new Subtask("Подзадача", "Описание", Status.IN_PROGRESS));

        System.out.println(taskManager.getEpicList());
        System.out.println(taskManager.getTaskList());
        System.out.println(taskManager.getSubtaskList());

        System.out.println();
        System.out.println("Изменим статуса Subtask id=" + subtask3.getId());
        System.out.println("Было:");
        System.out.println(taskManager.getEpicList());
        System.out.println(taskManager.getSubtaskList());
        subtask3.setStatus(Status.DONE);
        taskManager.updateSubtask(subtask3);
        System.out.println("Стало:");
        System.out.println(taskManager.getEpicList());
        System.out.println(taskManager.getSubtaskList());

        System.out.println();
        System.out.println("Удаление Epic id=" + epic1.getId());
        System.out.println("Было:");
        System.out.println(taskManager.getEpicList());
        System.out.println(taskManager.getSubtaskList());
        taskManager.removeEpicById(epic1.getId());
        System.out.println("Стало:");
        System.out.println(taskManager.getEpicList());
        System.out.println(taskManager.getSubtaskList());

        System.out.println();
        System.out.println("Удаление Task id=" + task1.getId());
        System.out.println("Было:");
        System.out.println(taskManager.getTaskList());
        System.out.println("Стало:");
        taskManager.removeTaskById(task1.getId());
        System.out.println(taskManager.getTaskList());

        System.out.println();
        System.out.println("Удаление Subtask id=" + subtask3.getId());
        System.out.println("Было:");
        System.out.println(taskManager.getEpicList());
        System.out.println(taskManager.getSubtaskList());
        taskManager.removeSubtaskById(subtask3.getId());
        System.out.println("Стало:");
        System.out.println(taskManager.getEpicList());
        System.out.println(taskManager.getSubtaskList());

        System.out.println();
        System.out.println("Удаление Epic id=" + epic1.getId());
        System.out.println("Было:");
        System.out.println(taskManager.getEpicList());
        System.out.println(taskManager.getSubtaskList());
        taskManager.removeEpicById(epic1.getId());
        System.out.println("Стало:");
        System.out.println(taskManager.getEpicList());
        System.out.println(taskManager.getSubtaskList());
    }
}
