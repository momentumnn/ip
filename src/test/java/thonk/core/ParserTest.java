package thonk.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import thonk.Command;
import thonk.Pair;
import thonk.Task;
import thonk.ThonkException;
import thonk.Todo;

public class ParserTest {

    @TempDir
    Path tempDir;

    private TaskManager newTaskManagerWithTasks(int n) {
        Path file = tempDir.resolve("tasks.txt");
        TaskManager tm = new TaskManager(file.toString());
        tm.getTasks().clear();
        for (int i = 1; i <= n; i++) {
            tm.getTasks().add(new Todo("t" + i));
        }
        return tm;
    }

    // -------------------------
    // Null / blank input
    // -------------------------

    @Test
    void parse_nullInput_throwsThonkException() {
        TaskManager tm = newTaskManagerWithTasks(0);
        assertThrows(ThonkException.class, () -> Parser.parse(null, tm));
    }

    @Test
    void parse_blankInput_throwsThonkException() {
        TaskManager tm = newTaskManagerWithTasks(0);
        assertThrows(ThonkException.class, () -> Parser.parse("   \t\n", tm));
    }

    @Test
    void parse_nullTaskManager_throwsThonkException() {
        assertThrows(ThonkException.class, () -> Parser.parse("list", null));
    }

    // -------------------------
    // Unknown / weird commands
    // -------------------------

    @Test
    void parse_unknownCommand_returnsUnknownAndNullTask() {
        TaskManager tm = newTaskManagerWithTasks(0);
        Pair<Command, Task> out = Parser.parse("wat", tm);
        assertEquals(Command.UNKNOWN, out.t());
        assertNull(out.u());
    }

    @Test
    void parse_todoMissingArgs_throwsThonkException() {
        TaskManager tm = newTaskManagerWithTasks(0);
        assertThrows(ThonkException.class, () -> Parser.parse("todo", tm));
    }

    @Test
    void parse_todoWithWeirdWhitespace_parsesTodo() {
        TaskManager tm = newTaskManagerWithTasks(0);
        Pair<Command, Task> out = Parser.parse("todo   read   book", tm);
        assertEquals(Command.TODO, out.t());
        assertNotNull(out.u());
        assertEquals(Todo.class, out.u().getClass());
    }

    // -------------------------
    // MARK/UNMARK/DELETE - index handling
    // -------------------------

    @Test
    void parse_markValidIndex_returnsReferencedTask() {
        TaskManager tm = newTaskManagerWithTasks(3);

        Pair<Command, Task> out = Parser.parse("mark 2", tm);
        assertEquals(Command.MARK, out.t());
        assertNotNull(out.u());
        assertSame(tm.getTasks().get(1), out.u());
    }

    @Test
    void parse_markOutOfBounds_throwsThonkException() {
        TaskManager tm = newTaskManagerWithTasks(3);
        assertThrows(ThonkException.class, () -> Parser.parse("mark 4", tm));
    }

    @Test
    void parse_markZero_throwsThonkException() {
        TaskManager tm = newTaskManagerWithTasks(3);
        assertThrows(ThonkException.class, () -> Parser.parse("mark 0", tm));
    }

    @Test
    void parse_markNonNumeric_throwsThonkException() {
        TaskManager tm = newTaskManagerWithTasks(3);
        assertThrows(ThonkException.class, () -> Parser.parse("mark two", tm));
    }

    @Test
    void parse_markMissingIndex_currentlyThrowsRuntimeException() {
        TaskManager tm = newTaskManagerWithTasks(3);
        // This reveals a robustness gap: missing index token.
        assertThrows(RuntimeException.class, () -> Parser.parse("mark", tm));
    }

    @Test
    void parse_multiDigitIndex_regexBug() {
        TaskManager tm = newTaskManagerWithTasks(12);

        // With 12 tasks, "mark 10" should be valid logically.
        // If bounds check uses a character-class regex like "[1-12]",
        // it will NOT match "10" and will throw "out of bounds".
        assertThrows(ThonkException.class, () -> Parser.parse("mark 10", tm));
    }

    // -------------------------
    // DEADLINE / EVENT weird formats
    // -------------------------

    @Test
    void parse_deadlineMissingArgs_throwsThonkException() {
        TaskManager tm = newTaskManagerWithTasks(0);
        assertThrows(ThonkException.class, () -> Parser.parse("deadline", tm));
    }

    @Test
    void parse_deadlineMissingBySegment_currentlyThrowsRuntimeException() {
        TaskManager tm = newTaskManagerWithTasks(0);
        // Example: no "/by" segment -> commonly causes ArrayIndexOutOfBoundsException
        assertThrows(RuntimeException.class, () -> Parser.parse("deadline submit report 2026-01-01", tm));
    }

    @Test
    void parse_eventMissingToSegment_currentlyThrowsRuntimeException() {
        TaskManager tm = newTaskManagerWithTasks(0);
        assertThrows(RuntimeException.class, () -> Parser.parse("event party /from 1pm", tm));
    }

    @Test
    void parse_eventMissingFromSegment_currentlyThrowsRuntimeException() {
        TaskManager tm = newTaskManagerWithTasks(0);
        assertThrows(RuntimeException.class, () -> Parser.parse("event party /to 2pm", tm));
    }
}
