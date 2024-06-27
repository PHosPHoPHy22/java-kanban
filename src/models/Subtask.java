package models;

import java.time.LocalDateTime;

public class Subtask extends Task {

    private Integer relatedEpicId;


    public Subtask(String name, String description, Status status, int id, LocalDateTime localDateTime, int i) {
        super(name, description, Status.NEW);
        this.relatedEpicId = id;
    }

    public Subtask(int id, String title, String description, Status status, int relatedEpicId, LocalDateTime startTime, int duration) {
        super(id, title, description, status, startTime, duration);
        this.relatedEpicId = relatedEpicId;
    }

    public int getEpicIdForThisSubtask() {
        return relatedEpicId;
    }

    public void setEpicIdForThisSubtask(int id) {
        if (relatedEpicId == null) {
            relatedEpicId = id;
        }
    }

}