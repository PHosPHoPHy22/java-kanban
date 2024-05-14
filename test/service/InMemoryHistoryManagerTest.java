package service;

import model.Epic;
import model.Status;
import model.Task;
import org.junit.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InMemoryHistoryManagerTest {

    @Test
    public void shouldShowTasksHistory() {
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
        TaskManager taskManager = new InMemoryTaskManager(historyManager);
        Epic epic = taskManager.createEpic(new Epic("epic", " "));
        Epic epic1 = taskManager.createEpic(new Epic("epic1", " "));
        taskManager.getEpic(1);
        taskManager.getEpic(2);
        assertEquals(2, taskManager.getHistory().size());

    }

    @Test
    public void testRemoveFirst() {
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
        TaskManager taskManager = new InMemoryTaskManager(historyManager);
        Task task1 = taskManager.create(new Task("task1", Status.NEW, " "));
        historyManager.add(task1);
        Task task2 = taskManager.create(new Task("task2", Status.NEW, " "));
        historyManager.add(task2);
        Task task3 = taskManager.create( new Task("task3", Status.NEW, " "));
        historyManager.add(task3);

        historyManager.removeId(task1.getId());

        assertEquals(historyManager.getAll(), List.of(task2, task3));
    }

    @Test
    public void testRemoveLast() {
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
        TaskManager taskManager = new InMemoryTaskManager(historyManager);
        Task task1 = taskManager.create(new Task("task1", Status.NEW, " "));
        historyManager.add(task1);
        Task task2 = taskManager.create(new Task("task2", Status.NEW, " "));
        historyManager.add(task2);
        Task task3 = taskManager.create( new Task("task3", Status.NEW, " "));
        historyManager.add(task3);

        historyManager.removeId(task3.getId());

        assertEquals(historyManager.getAll(), List.of(task1, task2));
    }

    @Test
    public void mustDeleteSameTasksInHistory() {
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
        TaskManager taskManager = new InMemoryTaskManager(historyManager);
        Task task1 = taskManager.create(new Task("task1", Status.NEW, " "));
        historyManager.add(task1);
        Task task3 = taskManager.create( new Task("task3", Status.NEW, " "));
        historyManager.add(task3);
        historyManager.add(task1);

        assertEquals(historyManager.getAll(), List.of(task3, task1));
    }

    @Test
    public void mustDeleteSameEpicsInHistory() {
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
        TaskManager taskManager = new InMemoryTaskManager(historyManager);
        Epic epic = taskManager.createEpic(new Epic("epic", " "));
        historyManager.add(epic);
        Task task3 = taskManager.create( new Task("task3", Status.NEW, " "));
        historyManager.add(task3);
        historyManager.add(epic);

        assertEquals(historyManager.getAll(), List.of(task3, epic));
    }
}
