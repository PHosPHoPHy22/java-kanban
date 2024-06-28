package managers;

import exceptions.ManagerSaveException;
import models.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.time.Duration;



public class FileBackedTaskManager  extends InMemoryTaskManager {

    private final File filename;

    public FileBackedTaskManager() {
        this.filename = new File("file.csv");
    }

    public FileBackedTaskManager(File src) {
        this.filename = src;
    }

    private String toString(Task task) {
        return task.getId() + "," + task.getType() + "," + task.getName() + "," + task.getStatus() + ","
                + task.getDescription() + ", " + "\n";
    }

    private String toStringSub(Subtask task) {
        return task.getId() + "," + task.getType() + "," + task.getName() + "," + task.getStatus() + ","
                + task.getDescription() + "," + task.getId() + "\n";
    }



    private void createATask(String idEx, String type, String name, String status,
                             String description, String startTime, String duration, String idEpicForSub) {
        int id = Integer.parseInt(idEx);
        LocalDateTime startTimeFromFile = LocalDateTime.parse(startTime, Task.DATE_TIME_FORMATTER);
        Duration durationFromFile = Duration.parse(duration);

        switch (type) {
            case "TASK":
                Task task = new Task(name, description, getStatus(status));
                task.setId(id);
                task.setTypeOfTask(Type.TASK);
                task.setStartTime(startTimeFromFile);
                task.setDuration(durationFromFile);
                tasks.put(id, task);
                break;
            case "EPIC":
                Epic epic = new Epic(name, description);
                epic.setId(id);
                subtasks.values().forEach(subtask -> {
                    if (subtask.getEpicIdForThisSubtask() == epic.getId()) {
                        epic.addSubtasksForThisEpic(subtask);
                    }
                });

                setEpicForChangeStatusEndTime(epic);
                epic.setTypeOfTask(Type.EPIC);
                epics.put(id, epic);
                break;
            default:
                int epicId = Integer.parseInt(idEpicForSub.trim());
                Epic epicSaved = epics.get(epicId);
                if (epicSaved == null) {
                    epicSaved = new Epic(name, description);
                    epicSaved.setId(epicId);
                    epicSaved.setTypeOfTask(Type.EPIC);
                    epics.put(epicId, epicSaved);
                }
                Subtask subtask = new Subtask(name, description, Status.NEW, epicId, LocalDateTime.of(2024, 7, 13, 18, 38, 20), 15);
                subtask.setStatus(getStatus(status));
                subtask.setEpicIdForThisSubtask(epicId);
                subtask.setId(id);
                subtask.setTypeOfTask(Type.SUBTASK);
                subtask.setStartTime(startTimeFromFile);
                subtask.setDuration(durationFromFile);
                subtasks.put(id, subtask);

                epicSaved.addSubtasksForThisEpic(subtask);
                setEpicForChangeStatusEndTime(epicSaved);
                break;
        }
    }

    private void save() {
        List<Task> allTasks = new ArrayList<>();
        allTasks.addAll(this.tasks.values());
        allTasks.addAll(this.epics.values());
        allTasks.addAll(this.subtasks.values());
        try (FileWriter writer = new FileWriter(filename, StandardCharsets.UTF_8)) {
            writer.write("id,type,name,status,description,epic\n");
            for (Task entry : allTasks) {
                if (entry.getType().equals(Type.SUBTASK)) {
                    writer.write(toStringSub((Subtask) entry));
                } else {
                    writer.write(toString(entry));
                }
            }
        } catch (IOException exception) {
            throw new ManagerSaveException("Ошибка записи задач в файл");
        }
    }

    private String convertTaskToString(Task example) {
        String idEx = String.valueOf(example.getId());
        String type = String.valueOf(example.getTypeOfTask());
        String name = example.getName();
        String status = String.valueOf(example.getStatus());
        String desc = example.getDescription();
        String startTime = example.getStartTime().format(Task.DATE_TIME_FORMATTER);
        String duration = example.getDuration().toString();
        String epicIdForSub = "";
        if (example.getTypeOfTask().equals(Type.SUBTASK)) {
            Subtask subtask = (Subtask) example;
            epicIdForSub = String.valueOf(subtask.getEpicIdForThisSubtask());
        }
        return String.format("%5s,%s,%s,%s,%s,%s,%s,%-3s\n", idEx, type, name, status, desc, startTime, duration, epicIdForSub);
    }

    private Status getStatus(String status) {
        return Status.valueOf(status);
    }

    @Override
    public void saveTask(Task task) {
        super.saveTask(task);
        save();
    }

    @Override
    public void saveSubtask(Subtask subtask) {
        super.saveSubtask(subtask);
        save();
    }

    @Override
    public void saveEpic(Epic epic) {
        super.saveEpic(epic);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public Task getTaskById(int id) {
        Task task = super.getTaskById(id);
        save();
        return task;
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask = super.getSubtaskById(id);
        save();
        return subtask;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = super.getEpicById(id);
        save();
        return epic;
    }

    @Override
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteEpicById(int id) {
        super.deleteEpicById(id);
        save();
    }

    @Override
    public void deleteSubtaskById(int id) {
        super.deleteSubtaskById(id);
        save();
    }

    @Override
    public void deleteTasks() {
        super.deleteTasks();
        save();
    }

    @Override
    public void deleteSubtasks() {
        super.deleteSubtasks();
        save();
    }

    @Override
    public void deleteEpics() {
        super.deleteEpics();
        save();
    }

}
