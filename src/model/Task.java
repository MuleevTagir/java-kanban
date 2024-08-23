package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class Task implements Cloneable {
    private Type type;
    private Integer id;
    private String title;
    private String description;
    private Status status;
    private Duration duration;
    private LocalDateTime startTime;
    protected List<Integer> linkList;

    public static Task fromString(String value) {
        String[] arr = value.split(",");
        return new Task(
                Integer.parseInt(arr[0]),
                arr[2],
                arr[4],
                Status.valueOf(arr[3]),
                Duration.ofMinutes(Long.parseLong(arr[5])),
                LocalDateTime.parse(arr[6])
        );
    }

    public Task() {
        this(-1, "s", "string", Status.NEW, Type.TASK, Duration.ofDays(1), LocalDateTime.now());
    }

    public Task(int i, String s, String string, Status status, Duration duration, LocalDateTime startTime) {
        this(i, s, string, status, Type.TASK, duration, startTime);
    }

    protected Task(int id, String title, String description, Status status, Type type, Duration duration, LocalDateTime startTime) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.type = type;
        this.duration = duration;
        this.startTime = startTime;
        this.linkList = new ArrayList<>();
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
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

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public List<Integer> getLinkList() {
        return linkList;
    }

    public void setLinkList(List<Integer> linkList) {
        this.linkList = linkList;
    }

    public LocalDateTime getEndTime() {
        return startTime.plus(duration);
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
    public Task clone() {
        try {
            return (Task) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    @Override
    public String toString() {
        String linkString = this.linkList.stream().map(String::valueOf).collect(Collectors.joining(";"));

        return String.format("%d,%s,%s,%s,%s,%d,%s,%s" + System.lineSeparator(),
                this.id,
                this.type,
                this.title,
                this.status,
                this.description,
                this.duration.toMinutes(),
                this.startTime.toString(),
                linkString
        );
    }
}