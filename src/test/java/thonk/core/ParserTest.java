package thonk.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import thonk.ThonkException;
import thonk.command.Command;
import thonk.command.DeadlineCommand;
import thonk.command.ErrorCommand;
import thonk.command.EventCommand;
import thonk.command.MarkCommand;
import thonk.command.TodoCommand;
import thonk.task.Todo;

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
    void parse_nullInput_returnsErrorCommand() {
        TaskManager tm = newTaskManagerWithTasks(0);

        Command out = Parser.parse(null, tm);

        assertInstanceOf(ErrorCommand.class, out);
        assertEquals("Input cannot be empty", out.toString());
    }

    @Test
    void parse_blankInput_returnsErrorCommand() {
        TaskManager tm = newTaskManagerWithTasks(0);

        Command out = Parser.parse("   \t\n", tm);

        assertInstanceOf(ErrorCommand.class, out);
        assertEquals("Input cannot be empty", out.toString());
    }

    @Test
    void parse_nullTaskManager_throwsThonkException() {
        assertThrows(ThonkException.class, () -> Parser.parse("list", null));
    }

    // -------------------------
    // Unknown / weird commands
    // -------------------------

    @Test
    void parse_unknownCommand_throwsThonkException() {
        TaskManager tm = newTaskManagerWithTasks(0);
        assertThrows(ThonkException.class, () -> Parser.parse("wat", tm));
    }

    @Test
    void parse_todoMissingArgs_throwsThonkException() {
        TaskManager tm = newTaskManagerWithTasks(0);
        assertThrows(ThonkException.class, () -> Parser.parse("todo", tm));
    }

    @Test
    void parse_todoWithWeirdWhitespace_returnsTodoCommand() {
        TaskManager tm = newTaskManagerWithTasks(0);

        Command out = Parser.parse("todo   read   book", tm);

        assertInstanceOf(TodoCommand.class, out);
    }

    // -------------------------
    // MARK - index handling
    // -------------------------

    @Test
    void parse_markValidIndex_marksCorrectTaskOnExecute() {
        TaskManager tm = newTaskManagerWithTasks(3);
        assertFalse(tm.getTasks().get(1).getDone());

        Command out = Parser.parse("mark 2", tm);

        assertInstanceOf(MarkCommand.class, out);
        out.execute(tm);
        assertTrue(tm.getTasks().get(1).getDone());
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
        assertThrows(RuntimeException.class, () -> Parser.parse("mark", tm));
    }

    // -------------------------
    // DEADLINE / EVENT formats & validation
    // -------------------------

    @Test
    void parse_deadlineMissingArgs_throwsThonkException() {
        TaskManager tm = newTaskManagerWithTasks(0);
        assertThrows(ThonkException.class, () -> Parser.parse("deadline", tm));
    }

    @Test
    void parse_deadlineMissingBySegment_returnsErrorCommand() {
        TaskManager tm = newTaskManagerWithTasks(0);

        Command out = Parser.parse("deadline submit report 2026-01-01", tm);

        assertInstanceOf(ErrorCommand.class, out);
        assertEquals(Parser.INVALID_DEADLINE_FORMAT_MESSAGE, out.toString());
    }

    @Test
    void parse_deadlineInvalidDate_returnsErrorCommand() {
        TaskManager tm = newTaskManagerWithTasks(0);

        Command out = Parser.parse("deadline submit report /by not-a-date", tm);

        assertInstanceOf(ErrorCommand.class, out);
        assertEquals(Parser.INVALID_ISO_DATE_MESSAGE, out.toString());
    }

    @Test
    void parse_deadlineValid_returnsDeadlineCommand() {
        TaskManager tm = newTaskManagerWithTasks(0);

        Command out = Parser.parse("deadline submit report /by 2026-01-01", tm);

        assertInstanceOf(DeadlineCommand.class, out);
    }

    @Test
    void parse_eventMissingToSegment_returnsErrorCommand() {
        TaskManager tm = newTaskManagerWithTasks(0);

        Command out = Parser.parse("event party /from 1pm", tm);

        assertInstanceOf(ErrorCommand.class, out);
        assertEquals(Parser.INVALID_EVENT_FORMAT_MESSAGE, out.toString());
    }

    @Test
    void parse_eventMissingFromSegment_returnsErrorCommand() {
        TaskManager tm = newTaskManagerWithTasks(0);

        Command out = Parser.parse("event party /to 2pm", tm);

        assertInstanceOf(ErrorCommand.class, out);
        assertEquals(Parser.INVALID_EVENT_FORMAT_MESSAGE, out.toString());
    }

    @Test
    void parse_eventValid_returnsEventCommand() {
        TaskManager tm = newTaskManagerWithTasks(0);

        Command out = Parser.parse("event party /from 2022-12-12 /to 2022-12-12", tm);

        assertInstanceOf(EventCommand.class, out);
    }
}
