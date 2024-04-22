package service;

import model.Task;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    List<Task> history = new ArrayList<>();
    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }
        System.out.println("The task has been added to the history: " + task.getId());
        if (history.size() == 10) {
            history.remove(0);
        }
        history.add(task);
    }

    @Override
    public List<Task> getAll() {
        return history;
    }

}
