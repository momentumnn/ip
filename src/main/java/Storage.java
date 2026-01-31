import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Storage {
    private final String path;
    private static final String DEFAULT_STORAGE_PATH = "thonk.txt";

    Storage(String path) {
        this.path = stringToPath(path);
    }
    Storage() {
        this(DEFAULT_STORAGE_PATH);
    }
    private void createStoragePath(String string) {
        String home = System.getProperty("user.dir");
        Path path = Paths.get(home, string);

    }
    // Load data from disk
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
    public void save(ArrayList<Task> tasks)  {
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
    private String stringToPath(String string){
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
        return path.toString();
    }
    private String readFromTextStream(Path path) throws IOException {
        Stream<String> lines = Files.lines(path);
        String data = lines.collect(Collectors.joining("\n"));
        lines.close();
        return data;
    }
    @Override
    public String toString() {
        return path;
    }
}
