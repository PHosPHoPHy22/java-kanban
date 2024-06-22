package managers;

import models.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;


public class InMemoryTaskManager implements TaskManager {

    protected final Map<Integer, Task> tasks;
    protected final Map<Integer, Subtask> subtasks;
    protected final Map<Integer, Epic> epics;
    private final HistoryManager historyManager;
    private int countId;
    private TreeSet<Task> tasksByStartTime;

    public InMemoryTaskManager() {
        tasks = new HashMap<>();
        subtasks = new HashMap<>();
        epics = new HashMap<>();
        historyManager = Managers.getDefaultHistory();
        countId = 0;
        tasksByStartTime = new TreeSet<>(Comparator.comparing(Task::getStartTime));
    }

    @Override
    public void saveTask(Task task) {
        if (!tasks.containsValue(task)) {
            countId++;
            task.setId(countId);
            task.setTypeOfTask(Type.TASK);
            if (!validateStartTimeForTask(task)) {
                return;
            }
            tasks.put(task.getId(), task);
            addToTasksByStartTimeTreeSet(task);
        }
    }

    @Override
    public void saveSubtask(Subtask subtask) {
        if (!subtasks.containsValue(subtask)) {
            countId++;
            subtask.setId(countId);
            subtask.setTypeOfTask(Type.SUBTASK);
            if (!validateStartTimeForTask(subtask)) {
                return;
            }
            Epic epic = epics.get(subtask.getEpicIdForThisSubtask());
            if (epic == null) {
                return;
            }
            epic.addSubtasksForThisEpic(subtask);
            changeEpicStatus(epic);
            changeEpicEndTime(epic);
            subtasks.put(subtask.getId(), subtask);
            addToTasksByStartTimeTreeSet(subtask);
        }
    }

    @Override
    public void saveEpic(Epic epic) {
        if (!epics.containsValue(epic)) {
            countId++;
            epic.setId(countId);
            epic.setTypeOfTask(Type.EPIC);
            changeEpicEndTime(epic);
            epics.put(epic.getId(), epic);
        }
    }

    @Override
    public List<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }


    @Override
    public List<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public List<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public Task getTaskById(int id) {
        Task taskFromMap = tasks.get(id);
        historyManager.addToHistory(taskFromMap);
        return taskFromMap;
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtaskFromMap = subtasks.get(id);
        historyManager.addToHistory(subtaskFromMap);
        return subtaskFromMap;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epicFromMap = epics.get(id);
        historyManager.addToHistory(epicFromMap);
        return epicFromMap;
    }

    @Override
    public void deleteTaskById(int id) {
        if (tasks.containsKey(id)) {
            tasksByStartTime.remove(tasks.get(id));
            tasks.remove(id);
            historyManager.removeFromHistory(id);
        }
    }

    @Override
    public void deleteEpicById(int id) {
        if (epics.containsKey(id)) {
            epics.get(id).getSubtasksForThisEpic().keySet().forEach(someId -> {
                subtasks.remove(someId);
                historyManager.removeFromHistory(someId);
            });
            epics.remove(id);
            historyManager.removeFromHistory(id);
        }
    }

    @Override
    public void deleteSubtaskById(int id) {
        if (subtasks.containsKey(id)) {
            Subtask subtask = subtasks.get(id);
            Epic epic = epics.get(subtask.getEpicIdForThisSubtask());
            epic.deleteSubtaskForThisEpic(id);
            changeEpicStatus(epic);
            changeEpicEndTime(epic);
            subtasks.remove(id);
            historyManager.removeFromHistory(id);
            tasksByStartTime.remove(subtask);
        }
    }

    @Override
    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            if (!validateStartTimeForTask(task)) {
                return;
            }
            tasks.put(task.getId(), task);
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (subtasks.containsKey(subtask.getId())) {
            if (!validateStartTimeForTask(subtask)) {
                return;
            }
            epics.get(subtask.getEpicIdForThisSubtask()).deleteSubtaskForThisEpic(subtask.getId());
            subtasks.put(subtask.getId(), subtask);
            epics.get(subtask.getEpicIdForThisSubtask()).addSubtasksForThisEpic(subtask);
            changeEpicStatus(epics.get(subtask.getEpicIdForThisSubtask()));
            changeEpicEndTime(epics.get(subtask.getEpicIdForThisSubtask()));
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            epics.put(epic.getId(), epic);
        }
    }

    @Override
    public void deleteTasks() {
        tasks.values().forEach(task -> historyManager.removeFromHistory(task.getId()));
        tasks.values().forEach(task -> tasksByStartTime.remove(task));
        tasks.clear();
    }

    @Override
    public void deleteSubtasks() {
        epics.values().forEach(epic -> {
            epic.getSubtasksForThisEpic().clear();
            changeEpicStatus(epic);
            changeEpicEndTime(epic);
        });

        subtasks.values().forEach(subtask -> historyManager.removeFromHistory(subtask.getId()));
        subtasks.values().forEach(subtask -> tasksByStartTime.remove(subtask));
        subtasks.clear();
    }

    @Override
    public void deleteEpics() {
        subtasks.values().forEach(subtask -> historyManager.removeFromHistory(subtask.getId()));
        subtasks.values().forEach(subtask -> tasksByStartTime.remove(subtask));
        epics.values().forEach(epic -> historyManager.removeFromHistory(epic.getId()));
        subtasks.clear();
        epics.clear();

    }



    @Override
    public List<Subtask> getAllSubtasksByEpic(int id) {
        Map<Integer, Subtask> subtasksByEpic = epics.get(id).getSubtasksForThisEpic();
        return new ArrayList<>(subtasksByEpic.values());
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(tasksByStartTime);
    }

    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    public void setEpicForChangeStatusEndTime(Epic epic) {
        changeEpicStatus(epic);
        changeEpicEndTime(epic);
    }

    private void changeEpicStatus(Epic epic) {
        if (epic.getSubtasksForThisEpic().isEmpty()) {
            epic.setStatus(Status.NEW);
        } else {
            ArrayList<Status> statusesFromEpicSubtasks = new ArrayList<>();
            epic.getSubtasksForThisEpic().values().forEach(
                    subtask -> statusesFromEpicSubtasks.add(subtask.getStatus()));

            if (statusesFromEpicSubtasks.stream()
                    .anyMatch(status -> status == Status.IN_PROGRESS)) {
                epic.setStatus(Status.IN_PROGRESS);
                return;
            } else if (statusesFromEpicSubtasks.stream()
                    .anyMatch(status -> status == Status.NEW) &&
                    statusesFromEpicSubtasks.stream()
                            .anyMatch(status -> status == Status.DONE)) {
                epic.setStatus(Status.IN_PROGRESS);
                return;
            } else if (statusesFromEpicSubtasks.stream()
                    .anyMatch(status -> status == Status.NEW)) {
                epic.setStatus(Status.NEW);
                return;
            }
            epic.setStatus(Status.DONE);
        }
    }

    private void changeEpicEndTime(Epic epic) {
        Map<Integer, Subtask> subTaskIds = epic.getSubtasksForThisEpic();
        LocalDateTime startTime = null;
        LocalDateTime endTime = null;
        Duration duration = Duration.ofMinutes(0);

        for (Integer id : subTaskIds.keySet()) {
            Subtask subTask = subtasks.get(id);
            if(subTask.getStartTime() != null) {
                if (startTime == null || startTime.isAfter(subTask.getStartTime())) {
                    startTime = subTask.getStartTime();
                }
                if (endTime == null || endTime.isBefore(subTask.getEndTime())) {
                    endTime = subTask.getEndTime();
                }
                duration = duration.plus(subTask.getDuration());
            }
        }
        epic.setStartTime(startTime);
        epic.setEpicEndTime(endTime);
        epic.setDuration(duration);
    }

    private boolean checkTimeCrossing(Task task1, Task task2) {
        if (task1.getStartTime() == null || task2.getStartTime() == null)
            return false;
        return (task1.getEndTime().isAfter(task2.getStartTime())
                && task1.getEndTime().isBefore(task2.getEndTime()))
                || (task1.getStartTime().isAfter(task2.getStartTime())
                && task1.getStartTime().isBefore(task2.getEndTime()))
                || (task2.getEndTime().isAfter(task1.getStartTime())
                && task2.getEndTime().isBefore(task1.getEndTime()))
                || (task2.getStartTime().isAfter(task1.getStartTime())
                && task2.getStartTime().isBefore(task1.getEndTime()));


    }

    private void addToTasksByStartTimeTreeSet(Task task) {
        if (task.getStartTime() != null) {
            tasksByStartTime.add(task);
        }
    }

    private boolean validateStartTimeForTask(Task task) {
        Type type = task.getTypeOfTask();
        boolean check = false;
        switch (task.getTypeOfTask()) {
            case TASK:
                check = tasks.values()
                        .stream()
                        .noneMatch(t -> task.getId() != t.getId() && checkTimeCrossing(t, task));
            case SUBTASK:
                check = subtasks.values()
                        .stream()
                        .noneMatch(s -> task.getId() != s.getId() && checkTimeCrossing(s, task));
        }
        return check;
    }

}