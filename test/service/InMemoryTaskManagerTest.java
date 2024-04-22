package service;

import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class InMemoryTaskManagerTest {
    @Test
    public void shoulDeleteAllTasks() {
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
        TaskManager taskManager = new InMemoryTaskManager(historyManager);
        Task task1 = taskManager.create(new Task("task1", Status.IN_PROGRESS, " "));
        Task task2 = taskManager.create(new Task("task2", Status.NEW, " "));
        System.out.println(taskManager.getAll());
        taskManager.delete(1);
        System.out.println(taskManager.getAll());
    }
    @Test
    public void shouldDeleteEpicAndSubTasks() {
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
        TaskManager taskManager = new InMemoryTaskManager(historyManager);
        var newEpic = taskManager.createEpic(new Epic("newTestEpic", " "));
        SubTask subTask5 = new SubTask("SubTask5", Status.NEW, "SubTask5 test", newEpic);
        SubTask subTask6 = new SubTask("SubTask6", Status.NEW, "SubTask6 test", newEpic);
        taskManager.createSubTask(subTask5);
        taskManager.createSubTask(subTask6);
        System.out.println(taskManager.getEpics());
        taskManager.deleteEpic(1);
        System.out.println(taskManager.getEpics());
    }
    @Test
    public void shouldDeleteAllSubTasksButNotEpic() {
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
        TaskManager taskManager = new InMemoryTaskManager(historyManager);
        var newEpic = taskManager.createEpic(new Epic("newTestEpic", " "));
        SubTask subTask5 = new SubTask("SubTask5", Status.NEW, "SubTask5 test", newEpic);
        SubTask subTask6 = new SubTask("SubTask6", Status.NEW, "SubTask6 test", newEpic);
        taskManager.createSubTask(subTask5);
        taskManager.createSubTask(subTask6);
        System.out.println(taskManager.getEpics());
        taskManager.deleteSubtasks();
        System.out.println(taskManager.getEpics());
    }

}
