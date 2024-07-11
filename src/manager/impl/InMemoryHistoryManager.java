package manager.impl;

import manager.HistoryManager;
import manager.Node;
import model.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private final HashMap<Integer, Node> historyHashMap;

    private Node first;
    private Node last;

    public InMemoryHistoryManager() {
        this.historyHashMap = new HashMap<>();
        this.first = null;
        this.last = null;
    }

    private Node linkLast(Task task) {
        Node l = this.last;
        Node newNode = new Node(l, task, null);
        this.last = newNode;
        if (l == null)
            this.first = newNode;
        else
            l.setNext(newNode);
        return newNode;
    }

    private void removeNode(Node node) {
        Node next = node.getNext();
        Node prev = node.getPrev();

        if (prev == null) {
            first = next;
        } else {
            prev.setNext(next);
            node.setPrev(null);
        }

        if (next == null) {
            last = prev;
        } else {
            next.setPrev(prev);
            node.setNext(null);
        }

        node.setItem(null);
    }

    private List<Task> getTasks() {
        List<Task> taskList = new ArrayList<>();
        Node current = this.first;
        while (current != null) {
            taskList.add(current.getItem());
            current = current.getNext();
        }
        return taskList;
    }

    @Override
    public void add(Task task) {
        int id = task.getId();
        if (this.historyHashMap.containsKey(id)) {
            this.remove(id);
            this.historyHashMap.remove(id);
        }
        this.historyHashMap.put(id, linkLast(task));
    }

    @Override
    public void remove(int id) {
        if (this.historyHashMap.containsKey(id)) {
            this.removeNode(this.historyHashMap.get(id));
            this.historyHashMap.remove(id);
        } else {
            System.out.println("Запись с id=" + id + "не найдена.");
        }
    }

    @Override
    public List<Task> getHistory() {
        return this.getTasks();
    }
}