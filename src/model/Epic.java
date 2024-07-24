package model;

import java.util.List;

public class Epic extends Task {
    public Epic() {
        super(-1, "title", "description", Status.NEW, Type.EPIC);
    }

    public Epic(int id, String title, String description, Status status) {
        super(id, title, description, status, Type.EPIC);
    }

    public List<Integer> getSubTaskList() {
        return this.linkList;
    }

    public void addSubTask(Subtask subtask) {
        subtask.setLinkList(List.of(this.getId()));
        this.linkList.add(subtask.getId());
    }

    public void removeSubtaskById(Integer id) {
        this.linkList.remove(id);
    }

    public static Epic fromString(String value) {
        String[] arr = value.split(",");
        Epic epic = new Epic(
                Integer.parseInt(arr[0]),
                arr[2],
                arr[4],
                Status.valueOf(arr[3])
        );

        return epic;
    }
}