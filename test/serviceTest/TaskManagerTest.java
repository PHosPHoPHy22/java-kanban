package serviceTest;

import managers.InMemoryTaskManager;
import models.Status;
import models.Task;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TaskManagerTest {

    @Test
    public void setStartTimeAndDurationToTaskShouldSuccessfullySetStartTimeAndDurationTest() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();

        final Task task = new Task("Задача 1", "", Status.NEW);
        task.setStartTime(LocalDateTime.now());
        task.setDuration(Duration.ofMinutes(15));
        taskManager.saveTask(task);
        final LocalDateTime timeStartIsNotEmpty = task.getStartTime();
        final LocalDateTime timeEndIsNotEmpty = task.getEndTime();
        final long durationIsNotEmpty = task.getDuration().toMinutes();

        assertNotNull(timeStartIsNotEmpty, "Время старта Null");
        assertNotNull(timeEndIsNotEmpty, "Время конца Null");
        assertEquals(15, durationIsNotEmpty, "Продолжительность не 15");


        task.setStartTime(LocalDateTime.parse("2024-05-18T09:00"));
        task.setDuration(Duration.ofMinutes(15));

        final LocalDateTime timeStartNotEmpty = LocalDateTime.parse("2024-05-18T09:00");
        final long durationNotEmpty = 15;
        final LocalDateTime timeEndNotEmpty = timeStartNotEmpty.plusMinutes(durationNotEmpty);

        final LocalDateTime timeStartTask = task.getStartTime();
        final LocalDateTime timeEndTask = task.getEndTime();
        final long durationTask = task.getDuration().toMinutes();

        assertEquals(timeStartTask, timeStartNotEmpty, "Время старта не совпадает");
        assertEquals(timeEndTask, timeEndNotEmpty, "Время конца не совпадает");
        assertEquals(durationTask, durationNotEmpty, "Продолжительность не совпадает");
    }

    @Test
    public void updateTaskShouldSuccessfullyUpdateTaskStatusTest() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();

        final Task task1 = new Task("Задача 2", "", Status.NEW);
        task1.setStartTime(LocalDateTime.now());
        task1.setDuration(Duration.ofMinutes(15));
        taskManager.saveTask(task1);

        task1.setStatus(Status.IN_PROGRESS);
        taskManager.updateTask(task1);
        final Status tasksStatusAfterUpdate = taskManager.getTaskById(task1.getId()).getStatus();

        assertEquals(Status.IN_PROGRESS, tasksStatusAfterUpdate, "Статус не обновился");
    }

    @Test
    public void getTasksShouldSuccessfullySaveTasksGetTasksTest() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager();

        final Task added3Task = new Task("Задача 1", "", Status.NEW);
        final Task added4Task = new Task("Задача 2", "", Status.IN_PROGRESS);
        added3Task.setStartTime(LocalDateTime.now());
        added3Task.setDuration(Duration.ofMinutes(15));
        added4Task.setStartTime(LocalDateTime.now());
        added4Task.setDuration(Duration.ofMinutes(15));

        taskManager.saveTask(added3Task);
        taskManager.saveTask(added4Task);

        final List<Task> tasks = taskManager.getTasks();

        assertNotNull(tasks, "Список задач пуст");
        assertEquals(2, tasks.size(), "Неверное количество задач");
    }




}
