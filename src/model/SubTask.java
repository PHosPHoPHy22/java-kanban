package model;

public class SubTask extends Task {
    private Epic epic;

    public SubTask(String name, Status status, String description, Epic epic) {
        super(name, status, description);
        this.epic = epic;
    }

    public void setEpic(Epic epic) { this.epic = epic; }

    @Override
    public String toString() {
        return "SubTask{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", status=" + status +
                ", description='" + description + '\'' +
                '}';
    }

    public Integer getEpicId() {
        return epic.getId();
    }

}
