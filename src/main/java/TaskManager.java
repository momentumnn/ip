import java.util.ArrayList;
import java.util.stream.IntStream;

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
}
