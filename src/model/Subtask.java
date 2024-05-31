package model;

public class Subtask extends Task {
    private Integer epicId = -1;

    public Subtask() {
    }

    public Subtask(String title, String description, Status status) {
        super(title, description, status);
    }

    public Integer getEpicId() {
        return epicId;
    }

    public void setEpicId(Integer epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return "model.Subtask{" +
                "id=" + this.getId() +
                ", title='" + this.getTitle() + '\'' +
                ", description='" + this.getDescription() + '\'' +
                ", status=" + this.getStatus().toString() +
                ", idEpic=" + epicId +
                "}";
    }
}
