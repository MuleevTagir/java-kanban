package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class Epic extends Task {
    private LocalDateTime endTime;

    public static Epic fromString(String value) {
        String[] arr = value.split(",");
        return new Epic(
                Integer.parseInt(arr[0]),
                arr[2],
                arr[4],
                Status.valueOf(arr[3])
        );
    }

    public Epic() {
        super(-1, "title", "description", Status.NEW, Type.EPIC, Duration.ofDays(0), LocalDateTime.now());
    }

    public Epic(int id, String title, String description, Status status) {
        super(id, title, description, status, Type.EPIC, Duration.ofDays(0), LocalDateTime.now());
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

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
}