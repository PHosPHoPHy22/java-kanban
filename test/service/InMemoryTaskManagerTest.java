package service;

import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import org.junit.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryTaskManagerTest {

    @Test
    public void shoulDeleteAllTasks() {
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();

        TaskManager taskManager = new InMemoryTaskManager(historyManager);
        Task task2 = taskManager.create(new Task("task2", Status.NEW, " "));
        assertEquals(1, taskManager.getAll().size());
        taskManager.delete(1);
        assertEquals(0, taskManager.getAll().size());
    }
    @Test
    public void shouldDeleteEpicAndSubTasks() {
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();

        TaskManager taskManager = new InMemoryTaskManager(historyManager);
        var newEpic = taskManager.createEpic(new Epic("newTestEpic", " "));
        SubTask subTask5 = new SubTask("SubTask5", Status.NEW, "SubTask5 test", newEpic);
        historyManager.add(subTask5);
        SubTask subTask6 = new SubTask("SubTask6", Status.NEW, "SubTask6 test", newEpic);
        historyManager.add(subTask6);
        taskManager.createSubTask(subTask5);
        taskManager.createSubTask(subTask6);
        assertNotNull(newEpic);
        taskManager.deleteEpic(1);

        assertEquals(List.of(), taskManager.getEpics());

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
        assertEquals(2, newEpic.getSubTasks().size());
        taskManager.deleteSubtasks();
        assertEquals(0, newEpic.getSubTasks().size());
    }

}
