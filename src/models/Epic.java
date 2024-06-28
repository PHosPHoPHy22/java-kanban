package models;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class Epic extends Task {

    private final Map<Integer, Subtask> subtasksForThisEpic;
    private LocalDateTime epicEndTime;

    public Epic(String name, String description) {
        super(name, description, Status.NEW);

        subtasksForThisEpic = new HashMap<>();
    }


    public void addSubtasksForThisEpic(Subtask subtask) {
        if (!subtasksForThisEpic.containsKey(subtask.getId())) {
            subtasksForThisEpic.put(subtask.getId(), subtask);
        }
    }

    public void deleteSubtaskForThisEpic(int id) {
        if (subtasksForThisEpic.containsKey(id)) {
            subtasksForThisEpic.remove(id);
        }
    }

    public Map<Integer, Subtask> getSubtasksForThisEpic() {
        return subtasksForThisEpic;
    }

    public LocalDateTime getEpicEndTime() {
        return epicEndTime;
    }

    public void setEpicEndTime(LocalDateTime epicEndTime) {
        String formattedLocalDateTime = epicEndTime.format(DATE_TIME_FORMATTER);
        this.epicEndTime = LocalDateTime.parse(formattedLocalDateTime, DATE_TIME_FORMATTER);
    }





}