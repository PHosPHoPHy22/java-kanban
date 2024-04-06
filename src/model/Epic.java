package model;
import java.util.HashMap;
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

    }


    public void removeTask(SubTask subTask) {

    }


    public static void printEpic(Epic epic) {
        System.out.println("Epic: id=" + epic.getId() + ", name=" + epic.getName());
    }

    public Status epicStatus() {
        boolean allDone = true;
        boolean allNew = true;
        if (subTasks.isEmpty()) {
            return Status.NEW;
        }
        for (SubTask subTask : subTasks) {
            if (subTask.getStatus().equals(Status.DONE)) {
                allNew = false;
            } else if (subTask.getStatus().equals(Status.NEW)) {
                allDone = false;
            }
        }
        if(allDone) {
            return Status.DONE;
        } else if (allNew) {
            return Status.NEW;
        } else {
            return Status.IN_PROGRESS;
        }
    }

}
