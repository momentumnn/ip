package thonk;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

import thonk.task.Deadline;
import thonk.task.Event;
import thonk.task.Task;
import thonk.task.Todo;

/**
 * Class to interact with file that stores the data.
 */
public class Storage {
    private static final String DEFAULT_STORAGE_PATH = "data" + File.separator + "thonk.txt";
    private static final String SPLITTING_CHAR = ";";
    private final String path;

    /**
     * Instantiate the Storage class
     * @param path String to path
     */
    public Storage(String path) {
        this.path = stringToPath(path);
    }

    /**
     * Instantiate the Storage class to the default storage path
     */
    public Storage() {
        this(DEFAULT_STORAGE_PATH);
    }

    /**
     * Load data from file
     * @return ArrayList of tasks
     */
    public ArrayList<Task> load() {
        File file = new File(path);
        ArrayList<Task> tasks = new ArrayList<>();

        if (!file.exists()) {
            return tasks; // Return empty list if file doesn't exist
        }
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) {
                    continue;
                }
                Task parsed = parseTaskFromFile(line);
                if (parsed != null) {
                    tasks.add(parsed);
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return tasks;
    }

    /**
     * Saves ArrayList of tasks to file
     * @param tasks Current list of tasks
     */
    public void save(ArrayList<Task> tasks) {
        try {
            FileWriter file = new FileWriter(path);
            for (Task task : tasks) {
                file.write(task.toSave(SPLITTING_CHAR));
                file.write(System.lineSeparator());
            }
            file.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private Task parseTaskFromFile(String line) throws IndexOutOfBoundsException {
        try {
            String[] parts = line.split(SPLITTING_CHAR);
            String type = parts[0];
            String description = parts[2];
            boolean isDone = Boolean.parseBoolean(parts[1]);
            return switch (type) {
            case "T" -> new Todo(description, isDone);
            case "D" -> new Deadline(description, isDone, parts[3]);
            case "E" -> new Event(description, isDone, parts[3], parts[4]);
            default -> throw new IllegalArgumentException("Unknown task type: " + type);
            };
        } catch (Exception e) {
            System.out.println("Invalid task line: " + line);
            System.out.println(e.getMessage());
        }
        return null;
    }
    private String stringToPath(String string) {
        if (string == null || string.isBlank()) {
            throw new IllegalArgumentException("Path cannot be null/blank");
        }
        Path inputPath = Paths.get(string.trim());
        // Detect absolute vs relative
        Path path = inputPath.isAbsolute()
                ? inputPath
                : Paths.get(System.getProperty("user.dir")).resolve(inputPath).normalize();
        try {
            // Ensure parent folders exist (if any)
            Path parent = path.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            // Ensure file exists
            if (Files.notExists(path)) {
                Files.createFile(path);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to create/access path: " + path, e);
        }
        return path.toString();
    }

    @Override
    public String toString() {
        return path;
    }
}
