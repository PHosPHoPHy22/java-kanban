package service;

import model.Epic;
import model.SubTask;
import model.Task;

import java.util.List;


public interface TaskManager {
    Task create(Task task);

    Task get(int id);

    void update(Task task);

    void updateEpic(Epic epic);

    void updateSubTask(SubTask subTask);

    List<Task> getAll();

    List<SubTask> getSubTasks();

    List<Epic> getEpics();

    void delete(int id);

    void deleteEpic(int id);

    void deleteTasks();

    void deleteSubtasks();

    void deleteEpics();

    void deleteSubTask(int id);

    void calculateStatus(Epic epic);

    SubTask createSubTask(SubTask subTask);

    Epic createEpic(Epic epic);

    List<Task> getHistory();

    Task getTask(int id);

    SubTask getSubtask(int id);

    Epic getEpic(int id);
}
