package thonk;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class TodoTest {
    @Test
    public void testString() {
        assertEquals("[T][ ] Test", new Todo("Test").toString());
    }
    @Test
    public void testMark() {
        assertEquals(false, new Todo("Test").isDone);
    }
}

