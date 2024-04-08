package service;
import model.Status;
import model.SubTask;
import model.Epic;
import model.Task;

import java.util.HashMap;
import java.util.ArrayList;
public class TaskManager {
    private HashMap<Integer, Task> tasks;
    private HashMap<Integer, Epic> epics;
    private HashMap<Integer, SubTask> subTasks;
    private int seq = 0;
    public TaskManager() {
        this.tasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.subTasks = new HashMap<>();
    }
    public Task create(Task task) {
        task.setId(generateId());
        tasks.put(task.getId(), task);
        return task;
    }


    private int generateId() { return ++seq; }

    public Task get(int id) { return tasks.get(id); }


    public void update(Task task) { tasks.put(task.getId(), task); }

    public void updateEpic(Epic epic) {
        Epic saved = epics.get(epic.getId());
        if (saved == null) {
            return;
        }
        saved.setName(epic.getName());
        saved.setDescription(epic.getDescription());
    }


    public void updateSubTask(SubTask subTask) {
        Epic epic = subTask.getEpic();
        Epic savedEpic = epics.get(epic.getId());
        if (savedEpic == null) {
            return;
        }
        calculateStatus(savedEpic);
    }

    public ArrayList<Task> getAll() {
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<SubTask> getSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    public void delete(int id) { tasks.remove(id); }

    public void deleteEpic(int id) {
        Epic epic = epics.remove(id);
        epic.getSubTasks().forEach(s -> subTasks.remove(s.getId()));
    }

    public void deleteTasks() {
        tasks.clear();
    }

    public void deleteSubtasks() {
        for (Epic epic : epics.values()) {
            epic.cleanSubtaskIds();
            calculateStatus(epic);
        }
        subTasks.clear();
    }

    public void deleteEpics() {
        epics.clear();
        subTasks.clear();
    }

    public void deleteSubTask(int id) {
        SubTask removeSubTask = subTasks.remove(id);

        Epic epic = removeSubTask.getEpic();
        Epic epicSaved = epics.get(epic.getId());

        epicSaved.getSubTasks().remove(removeSubTask);
        calculateStatus(epicSaved);
    }

    private void calculateStatus(Epic epic) {
        Status status = epic.calculateStatus();
        epic.setStatus(status);
    }

    public SubTask createSubTask(SubTask subTask) {
        subTask.setId(generateId());
        subTasks.put(subTask.getId(), subTask);

        Epic epic = epics.get(subTask.getEpic().getId());
        epic.addTask(subTask);
        calculateStatus(epic);
        return subTask;
    }

    public Epic createEpic(Epic epic) {
        epic.setId(generateId());
        epics.put(epic.getId(), epic);
        return epic;
    }

///
}
