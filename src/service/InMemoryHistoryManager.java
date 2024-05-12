package service;

import model.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
     LinkedHashMap map;
     LinkedList list;

    private static class Node {
        Task item;
        Node next;
        Node prev;

        Node(Node prev, Task element, Node next) {
            this.item = element;
            this.next = next;
            this.prev = prev;
        }
    }

    HashMap<Integer, Node> history = new HashMap<>();
    Node first;
    Node last;

    @Override
    public void add(Task task) {
        Node node = history.get(task.getId());
        if (node != null) {
            removeNode(node);
        }
        linkLast(task);
        history.put(task.getId(), last);
    }

    @Override
    public List<Task> getAll() {
        ArrayList<Task> list = new ArrayList<>();
        Node current = first;
        while (current != null) {
            list.add(current.item);
            current = current.next;
        }
        return list;
    }

    @Override
    public void removeId(int id) {
        Node node = history.get(id);
        if (node != null) {
            removeNode(node);
        }
    }

    void linkLast(Task task) {
        final Node l = last;
        final Node newNode = new Node(l, task, null);
        last = newNode;
        if (l == null) {
            first = newNode;
        } else {
            l.next = newNode;
        }
    }
    void removeNode(Node node) {
        if (node.next != null) {
            node.next.prev = node.prev;
        } else {
            last = node.prev;
        }
        if (node.prev != null) {
            node.prev.next = node.next;
        } else {
            first = node.next;
        }
        history.remove(node.item.getId());
    }
}
