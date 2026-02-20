package thonk;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class StorageTest {

    @TempDir
    Path tempDir;

    private Storage newStorageAtTempFile(String filename) {
        Path file = tempDir.resolve(filename);
        return new Storage(file.toString());
    }

    @Test
    void load_whenFileIsEmpty_returnsEmptyList() throws Exception {
        Storage storage = newStorageAtTempFile("empty.txt");

        // Ensure file is empty
        Files.writeString(Path.of(storage.toString()), "");

        ArrayList<Task> tasks = storage.load();
        assertEquals(0, tasks.size());
    }

    @Test
    void saveAndLoad_roundTrip_isSuccessful() {
        Storage storage = newStorageAtTempFile("roundtrip.txt");

        ArrayList<Task> original = new ArrayList<>();
        original.add(new Todo("read book", true));
        original.add(new Deadline("submit report", false, "2026-02-20"));
        original.add(new Event("party", true, "1pm", "2pm"));

        storage.save(original);

        ArrayList<Task> loaded = storage.load();
        assertEquals(3, loaded.size());

        assertInstanceOf(Todo.class, loaded.get(0));
        assertEquals("read book", loaded.get(0).getDescription());
        assertTrue(loaded.get(0).getDone());

        assertInstanceOf(Deadline.class, loaded.get(1));
        assertEquals("submit report", loaded.get(1).getDescription());
        assertFalse(loaded.get(1).getDone());

        assertInstanceOf(Event.class, loaded.get(2));
        assertEquals("party", loaded.get(2).getDescription());
        assertTrue(loaded.get(2).getDone());
    }

    @Test
    void load_skipsBlankLines() throws Exception {
        Storage storage = newStorageAtTempFile("blank-lines.txt");
        Path filePath = Path.of(storage.toString());

        Files.writeString(filePath,
                System.lineSeparator()
                        + "   " + System.lineSeparator()
                        + "\t" + System.lineSeparator()
                        + System.lineSeparator());

        ArrayList<Task> tasks = storage.load();
        assertEquals(0, tasks.size());
    }

    @Test
    void load_skipsMalformedLines_missingFields() throws Exception {
        Storage storage = newStorageAtTempFile("malformed.txt");
        Path filePath = Path.of(storage.toString());

        // Missing description field -> parts[2] will throw IndexOutOfBoundsException
        Files.writeString(filePath,
                "T;true" + System.lineSeparator()
                        + "D;false;something" + System.lineSeparator() // missing by date
                        + "E;true;something;start" + System.lineSeparator() // missing end time
        );

        ArrayList<Task> tasks = storage.load();
        assertEquals(0, tasks.size());
    }

    @Test
    void load_unknownTaskType_currentlyThrowsIllegalArgumentException() throws Exception {
        Storage storage = newStorageAtTempFile("unknown-type.txt");
        Path filePath = Path.of(storage.toString());

        Files.writeString(filePath, "X;false;???\n");

        // parseTaskFromFile throws IllegalArgumentException for unknown type,
        // and load() does not catch it -> it bubbles out.
        assertThrows(IllegalArgumentException.class, storage::load);
    }

    @Test
    void save_writesOneTaskPerLine() throws Exception {
        Storage storage = newStorageAtTempFile("format.txt");
        Path filePath = Path.of(storage.toString());

        ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(new Todo("a", false));
        tasks.add(new Todo("b", true));

        storage.save(tasks);

        String content = Files.readString(filePath);
        // Very lightweight check: should contain two lines (platform-specific line separator).
        String[] lines = content.split("\\R");
        assertEquals(2, lines.length);
    }
}
