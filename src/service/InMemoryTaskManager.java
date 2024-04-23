package service;

import model.Status;
import model.SubTask;
import model.Epic;
import model.Task;
import service.HistoryManager;
import service.TaskManager;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private HashMap<Integer, Task> tasks;
    private HashMap<Integer, Epic> epics;
    private HashMap<Integer, SubTask> subTasks;
    private final HistoryManager historyManager;
    private int seq = 0;
    public InMemoryTaskManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
        this.tasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.subTasks = new HashMap<>();
    }
    @Override
    public Task create(Task task) {
        task.setId(generateId());
        tasks.put(task.getId(), task);
        return task;
    }


    private int generateId() { return ++seq; }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    @Override
    public Task get(int id) { return tasks.get(id); }

    @Override
    public void update(Task task) { tasks.put(task.getId(), task); }

    @Override
    public void updateEpic(Epic epic) {
        Epic saved = epics.get(epic.getId());
        if (saved == null) {
            return;
        }
        saved.setName(epic.getName());
        saved.setDescription(epic.getDescription());
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        int id = subTask.getEpicId();
        Epic savedEpic = epics.get(id);
        if (savedEpic == null) {
            return;
        }
        calculateStatus(savedEpic);
    }
    @Override
    public List<Task> getAll() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<SubTask> getSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    @Override
    public List<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public void delete(int id) { tasks.remove(id); }

    @Override
    public void deleteEpic(int id) {
        Epic epic = epics.remove(id);
        epic.getSubTasks().forEach(s -> subTasks.remove(s.getId()));
    }

    @Override
    public void deleteTasks() {
        tasks.clear();
    }

    @Override
    public void deleteSubtasks() {
        for (Epic epic : epics.values()) {
            epic.cleanSubtaskIds();
            calculateStatus(epic);
        }
        subTasks.clear();
    }

    @Override
    public void deleteEpics() {
        epics.clear();
        subTasks.clear();
    }

    @Override
    public void deleteSubTask(int id) {
        SubTask removeSubTask = subTasks.remove(id);

        Epic epic = removeSubTask.getEpic();
        Epic epicSaved = epics.get(epic.getId());

        epicSaved.getSubTasks().remove(removeSubTask);
        calculateStatus(epicSaved);
    }

    @Override
    public void calculateStatus(Epic epic) {
        Status status = epic.calculateStatus();
        epic.setStatus(status);
    }

    @Override
    public SubTask createSubTask(SubTask subTask) {
        subTask.setId(generateId());
        subTasks.put(subTask.getId(), subTask);

        Epic epic = epics.get(subTask.getEpicId());
        epic.addTask(subTask);
        calculateStatus(epic);
        return subTask;
    }

    @Override
    public Epic createEpic(Epic epic) {
        epic.setId(generateId());
        epics.put(epic.getId(), epic);
        return epic;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getAll();
    }
    @Override
    public Task getTask(int id) {
        historyManager.add(tasks.get(id));
        return tasks.get(id);
    }
    @Override
    public SubTask getSubtask(int id) {
        historyManager.add(subTasks.get(id));
        return subTasks.get(id);
    }
    @Override
    public Epic getEpic(int id) {
        historyManager.add(epics.get(id));
        return epics.get(id);
    }
}
