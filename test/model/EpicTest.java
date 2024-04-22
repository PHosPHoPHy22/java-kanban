package model;


import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import service.InMemoryHistoryManager;
import service.InMemoryTaskManager;
import service.TaskManager;

import static org.junit.Assert.assertEquals;
@DisplayName("Epic")
public class EpicTest {
    @Test
    @DisplayName("Epics must be equals with its copy")
    public void shouldEqualsWithCopy() {
        Epic epic1 = new Epic("name1", "test1");
        Epic epicExpected = new Epic("name1", "test2");
        assertEqualsTask(epicExpected, epic1, "The epic must match");

    }

    private static void assertEqualsTask(Task exepted, Task actual, String message) {
        assertEquals(message + ", id", exepted.getId(), actual.getId());
        assertEquals(message + ", name", exepted.getName(), actual.getName());
    }

    @Test
    public void mustReturnSubTasks() {
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
        TaskManager taskManager = new InMemoryTaskManager(historyManager);
        var newEpic = taskManager.createEpic(new Epic("newTestEpic", " "));
        SubTask subTask5 = new SubTask("SubTask5", Status.NEW, "SubTask5 test", newEpic);
        SubTask subTask6 = new SubTask("SubTask6", Status.NEW, "SubTask6 test", newEpic);
        SubTask subTask7 = new SubTask("SubTask7", Status.NEW, "SubTask7 test", newEpic);
        SubTask subTask8 = new SubTask("SubTask8", Status.NEW, "SubTask8 test", newEpic);
        taskManager.createSubTask(subTask5);
        taskManager.createSubTask(subTask6);
        taskManager.createSubTask(subTask7);
        taskManager.createSubTask(subTask8);
        System.out.println(newEpic.getSubTasks());
    }
    @Test
    public void shouldDeleteSubTask() {
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
        TaskManager taskManager = new InMemoryTaskManager(historyManager);
        var newEpic = taskManager.createEpic(new Epic("newTestEpic", " "));
        SubTask subTask5 = new SubTask("SubTask5", Status.NEW, "SubTask5 test", newEpic);
        newEpic.removeTask(subTask5);
    }
    @Test
    public void calculateStatusTest() {
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
        TaskManager taskManager = new InMemoryTaskManager(historyManager);
        var newEpic = taskManager.createEpic(new Epic("newTestEpic", " "));
        SubTask subTask5 = new SubTask("SubTask5", Status.NEW, "SubTask5 test", newEpic);
        taskManager.createSubTask(subTask5);
        System.out.println(newEpic.getStatus());

        subTask5.setStatus(Status.IN_PROGRESS);
        taskManager.updateSubTask(subTask5);
        newEpic.calculateStatus();
        System.out.println(newEpic.getStatus());

        subTask5.setStatus(Status.DONE);
        taskManager.updateSubTask(subTask5);
        newEpic.calculateStatus();
        System.out.println(newEpic.getStatus());
    }

    @Test
    public void shouldClearAllSubTasks() {
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
        TaskManager taskManager = new InMemoryTaskManager(historyManager);
        var newEpic = taskManager.createEpic(new Epic("newTestEpic", " "));
        SubTask subTask5 = new SubTask("SubTask5", Status.NEW, "SubTask5 test", newEpic);
        SubTask subTask6 = new SubTask("SubTask6", Status.NEW, "SubTask6 test", newEpic);
        taskManager.createSubTask(subTask5);
        taskManager.createSubTask(subTask6);
        System.out.println(newEpic.getSubTasks());
        newEpic.cleanSubtaskIds();
        System.out.println(newEpic.getSubTasks());
    }
}

