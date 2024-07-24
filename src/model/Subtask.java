package model;

import java.util.List;

public class Subtask extends Task {

    public Subtask() {
        super(-1, "title", "description", Status.NEW, Type.SUBTASK);
    }

    public Subtask(int id, String title, String description, Status status) {
        super(id, title, description, status, Type.SUBTASK);
    }

    public static Subtask fromString(String value) {
        String[] arr = value.split(",");
        Subtask subtask = new Subtask(
                Integer.parseInt(arr[0]),
                arr[2],
                arr[4],
                Status.valueOf(arr[3])
        );
        subtask.setLinkList(List.of(Integer.parseInt(arr[5])));
        return subtask;
    }
}