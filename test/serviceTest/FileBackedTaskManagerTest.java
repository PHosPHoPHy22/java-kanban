package serviceTest;

import managers.InMemoryTaskManager;
import models.Epic;
import models.Status;
import models.Subtask;
import models.Task;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileBackedTaskManagerTest {



    @Test
    public void saveTaskShouldSuccessfullySaveTaskGetTaskByIdGetIdTest() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();
        final Task expectedTask = new Task("Задача 1", " ", Status.NEW);
        expectedTask.setStartTime(LocalDateTime.now());
        expectedTask.setDuration(Duration.ofMinutes(15));

        taskManager.saveTask(expectedTask);

        final int idTask = taskManager.getTaskById(expectedTask.getId()).getId();

        assertEquals(expectedTask.getId(), idTask, "Задачи не равны");
    }

    @Test
    public void saveSubtaskShouldSuccessfullySaveSubtaskGetSubtaskByIdGetIdTest() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();

        final Epic expectedEpic = new Epic("Эпик 1", "");
        taskManager.saveEpic(expectedEpic);
        final Subtask expectedSubtask = new Subtask("Подзадача 1", "");
        expectedEpic.addSubtasksForThisEpic(expectedSubtask);
        expectedSubtask.setEpicIdForThisSubtask(expectedEpic.getId());
        expectedSubtask.setStartTime(LocalDateTime.now());
        expectedSubtask.setDuration(Duration.ofMinutes(15));
        taskManager.saveSubtask(expectedSubtask);

        final int idSubTask = taskManager.getSubtaskById(expectedSubtask.getId()).getId();

        assertEquals(expectedSubtask.getId(), idSubTask, "Подзадачи не равны");
    }

}
