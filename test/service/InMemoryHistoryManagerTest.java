package service;

import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import org.junit.Assert;
import org.junit.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class InMemoryHistoryManagerTest {
    @Test
    public void shouldShowTasksHistory() {
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
        TaskManager taskManager = new InMemoryTaskManager(historyManager);
        Epic epic = taskManager.createEpic(new Epic("epic", " "));
        Epic epic1 = taskManager.createEpic(new Epic("epic1", " "));
        SubTask subTask1 = taskManager.createSubTask(new SubTask("subTask1", Status.NEW, " ", epic));
        taskManager.getEpic(1);
        taskManager.getEpic(2);
        taskManager.getSubtask(0);
        assertEquals(2, taskManager.getHistory().size());

    }
}
