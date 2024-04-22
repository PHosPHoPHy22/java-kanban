package service;

import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import org.junit.Test;

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
        System.out.println(taskManager.getHistory());

    }
}
