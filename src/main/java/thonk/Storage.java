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

/**
 * Class to interact with file that stores the data.
 */
public class Storage {
    private static final String DEFAULT_STORAGE_PATH = "thonk.txt";
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
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (parseTask(line) != null) {
                    tasks.add(parseTask(line));
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
            tasks.forEach(task -> {
                try {
                    file.write(task.toSave() + "\n");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            file.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private Task parseTask(String line) throws IndexOutOfBoundsException {
        try {
            String[] parts = line.split(";");
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
        }
        return null;
    }
    private String stringToPath(String string) {
        String home = System.getProperty("user.dir");
        Path path = Paths.get(home, string);
        boolean directoryExists = Files.exists(path);
        if (!directoryExists) {
            System.out.println("Directory does not exist!");
            try {
                Files.createFile(path);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        assert path.endsWith(string) : "Path not created properly";
        assert Files.exists(path) : "File does not exist";
        return path.toString();
    }
    @Override
    public String toString() {
        return path;
    }
}
