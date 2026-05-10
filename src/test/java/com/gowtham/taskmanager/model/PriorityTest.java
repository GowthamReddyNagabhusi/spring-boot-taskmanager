package com.gowtham.taskmanager.model;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
public class PriorityTest {
    @Test
    public void testFromChoiceReturnsLow() {
        assertEquals(Priority.LOW, Priority.fromChoice(1));
    }
    @Test
    public void testFromChoiceReturnsMedium() {
        assertEquals(Priority.MEDIUM, Priority.fromChoice(2));
    }
    @Test
    public void testFromChoiceReturnsHigh() {
        assertEquals(Priority.HIGH, Priority.fromChoice(3));
    }
}
