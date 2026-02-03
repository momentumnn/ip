package thonk.core;

import java.util.ArrayList;
import java.util.stream.IntStream;

import thonk.Storage;
import thonk.Task;

/**
 * The class TaskManager stores all tasks and handles the file management between Thonk and the filesystem.
 */
public class TaskManager {
    private final Storage storage;
    private final ArrayList<Task> tasks;

    /**
     * Creates an instance of TaskManager with a path to the file
     * @param path String path to file
     */
    public TaskManager(String path) {
        storage = new Storage(path);
        tasks = storage.load();
    }

    /**
     * Creates an instance of TaskManager with no specific path to file.
     * Path is decided via Storage class
     */
    public TaskManager() {
        storage = new Storage();
        tasks = storage.load();
    }

    /**
     * Returns an ArrayList of Tasks
     * @return ArrayList<\Task> of tasks
     */
    public ArrayList<Task> getTasks() {
        return tasks;
    }

    /**
     * Adds task into the current ArrayList and file
     * @param task Task to be added
     */
    public void add(Task task) {
        tasks.add(task);
        storage.save(tasks);
    }
    /**
     * Delete task from the current ArrayList and file
     * @param task Task to be deleted
     */
    public void delete(Task task) {
        tasks.remove(task);
        storage.save(tasks);
    }

    /**
     * Marks task as per isDone parameter
     * @param task Task to be marked
     * @param isDone boolean value of isDone
     */
    public void mark(Task task, Boolean isDone) {
        tasks.get(this.getTaskIndex(task)).setDone(isDone);
        storage.save(tasks);
    }
    private int getTaskIndex(Task task) {
        return IntStream.range(0, tasks.size())
                .filter(i -> tasks.get(i).equals(task))
                .findFirst()
                .orElse(-1); // Returns -1 if no match is found
    }
    public ArrayList<Task> find(String text) {
        return new ArrayList<Task>(tasks.stream().filter(task -> task.getDescription().contains(text)).toList());
    }
}
