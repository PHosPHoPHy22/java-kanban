package models;

public class Subtask extends Task {

    private Integer relatedEpicId;


    public Subtask(String name, String description) {
        super(name, description, Status.NEW);
        relatedEpicId = 0;
    }


    public int getEpicIdForThisSubtask() {
        return relatedEpicId;
    }

    public void setEpicIdForThisSubtask(int id) {
        if (relatedEpicId == 0) {
            relatedEpicId = id;
        } else {
            System.out.println("У данной подзадачи уже есть Эпик");
        }
    }

}