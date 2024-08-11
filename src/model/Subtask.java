package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class Subtask extends Task {

    public Subtask() {
        super(-1, "title", "description", Status.NEW, Type.SUBTASK, Duration.ofDays(1), LocalDateTime.now());
    }

    public Subtask(int id, String title, String description, Status status, Duration duration, LocalDateTime startTime) {
        super(id, title, description, status, Type.SUBTASK, duration, startTime);
    }

    public static Subtask fromString(String value) {
        String[] arr = value.split(",");
        Subtask subtask = new Subtask(
                Integer.parseInt(arr[0]),
                arr[2],
                arr[4],
                Status.valueOf(arr[3]),
                Duration.ofMinutes(Long.parseLong(arr[5])),
                LocalDateTime.parse(arr[6])
        );
        subtask.setLinkList(List.of(Integer.parseInt(arr[7])));
        return subtask;
    }
}