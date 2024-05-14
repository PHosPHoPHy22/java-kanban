package model;

import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.Assert.assertEquals;

@DisplayName("Task")
public class TaskTest {

    @Test
    @DisplayName("Tasks must be equals with same id")
    public void shouldBeEqualsWithSameId() {
        Task task1 = new Task(1, "Task1", Status.NEW, " ");
        Task task2 = new Task(1, "Task1", Status.NEW, " ");
        assertEqualsTask(task1, task2);
    }

    public void assertEqualsTask(Task task1, Task task2) {
        assertEquals(task1.getName(), task2.getName());
        assertEquals(task1.getId(), task2.getId());
    }






}
