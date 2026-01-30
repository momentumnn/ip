import java.util.ArrayList;

public class TaskManager {
    private final Storage storage;
    private final ArrayList<Task> tasks;

    public TaskManager(String path) {
        storage = new Storage(path);
        tasks = storage.load();
    }

    public TaskManager() {
        storage = new Storage();
        tasks = storage.load();
    }
    public ArrayList<Task> getTasks() {
        return tasks;
    }
    public void add(Task task) {
        tasks.add(task);
        storage.save(tasks);
    }

    public void delete(Task task) {
        tasks.remove(task);
        storage.save(tasks);
    }
}
