package model;

import java.util.*;
import java.util.stream.Collectors;

public class Task implements Cloneable {
    private final Type type;
    private Integer id;
    private String title;
    private String description;
    private Status status;
    protected List<Integer> linkList;

    public Task() {
        this(-1, "s", "string", Status.NEW, Type.TASK);
    }

    public Task(int i, String s, String string, Status status) {
        this(i, s, string, status, Type.TASK);
    }

    protected Task(int id, String title, String description, Status status, Type type) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.type = type;
        this.linkList = new ArrayList<>();
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        String linkString = this.linkList.stream().map(String::valueOf).collect(Collectors.joining(";"));

        return String.format("%d,%s,%s,%s,%s,%s" + System.lineSeparator(),
                this.id,
                this.type,
                this.title,
                this.status,
                this.description,
                linkString
        );
    }

    public static Task fromString(String value) {
        String[] arr = value.split(",");
        Task task = new Task(
                Integer.parseInt(arr[0]),
                arr[2],
                arr[4],
                Status.valueOf(arr[3])
        );
        return task;
    }

    @Override
    public Task clone() {
        try {
            return (Task) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    public List<Integer> getLinkList() {
        return linkList;
    }

    public void setLinkList(List<Integer> linkList) {
        this.linkList = linkList;
    }
}