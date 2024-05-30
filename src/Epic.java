import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Epic extends Task {
    private final List<Integer> subtaskIdList;

    public Epic() {
        this.subtaskIdList = new ArrayList<>();
    }

    public Epic(String title, String description, Status status) {
        super(title, description, status);
        this.subtaskIdList = new ArrayList<>();
    }

    public List<Integer> getSubTaskList() {
        return subtaskIdList;
    }

    public void addSubTask(Subtask subtask) {
        subtask.setEpicId(this.getId());
        subtaskIdList.add(subtask.getId());
    }

    public void removeSubtaskById(Integer id) {
        subtaskIdList.remove(id);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + this.getId() +
                ", title='" + this.getTitle() + '\'' +
                ", description='" + this.getDescription() + '\'' +
                ", status=" + this.getStatus().toString() +
                ", taskIdList=" + Arrays.toString(this.subtaskIdList.toArray()) +
                "}";
    }
}
