package model;
import java.util.ArrayList;
public class Epic extends Task {
    private ArrayList<SubTask> subTasks = new ArrayList<>();


    public Epic(String name, String description) {
        super(name, Status.NEW, description);
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

    public void cleanSubtaskIds() {
        subTasks.clear();
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
