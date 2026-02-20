package thonk;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

import thonk.task.Todo;

public class TodoTest {
    @Test
    public void testString() {
        assertEquals("[T][ ] Test", new Todo("Test").toString());
    }
    @Test
    public void testMark() {
        assertFalse(new Todo("Test").getDone());
    }
}

