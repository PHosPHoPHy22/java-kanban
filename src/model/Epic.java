package model;
import java.util.ArrayList;
public class Epic extends Task {
    ArrayList<SubTask> subTasks = new ArrayList<>();


    public Epic(String name, String description) {
        super(name, Status.NEW, description);
    }
    public Epic(String name, Status status, String description) {
        super(name, status, description);
    }

    public ArrayList<SubTask> getSubTasks() {
        return subTasks;
    }


    public void addTask(SubTask subTask) {
        subTasks.add(subTask);
    }


    public void removeTask(SubTask subTask) {
        subTasks.remove(subTask);
    }


    public Status calculateStatus() {
        if (subTasks.isEmpty() || subTasks.stream().allMatch(s -> s.getStatus().equals(Status.NEW))) {
            return Status.NEW;
        }

        if (subTasks.stream().allMatch(s -> s.getStatus().equals(Status.DONE))) {
            return Status.DONE;
        }

        return Status.IN_PROGRESS;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", status=" + status +
                ", description='" + description + '\'' +
                ", subTasks=" + subTasks +
                '}';
    }
}
