package service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import model.Task;

public interface HistoryManager {

    void add(Task task);

    List<Task> getAll();

}
