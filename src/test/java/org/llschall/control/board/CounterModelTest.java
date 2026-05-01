package org.llschall.control.board;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class CounterModelTest {

    private CounterModel counterModel;

    @BeforeEach
    void setUp() {
        counterModel = new CounterModel();
    }

    @Test
    void testInitialValue() {
        // Verify that the counter starts at 0
        assertEquals(0, counterModel.value);
    }

    @Test
    void testIncrementOnce() {
        // Increment counter once and verify value is 1
        counterModel.increment();
        assertEquals(1, counterModel.value);
    }

    @Test
    void testIncrementMultipleTimes() {
        // Increment counter multiple times and verify cumulative value
        counterModel.increment();
        counterModel.increment();
        counterModel.increment();
        assertEquals(3, counterModel.value);
    }

    @Test
    void testIncrementFromNonZeroValue() {
        // Set initial value and verify increment works
        counterModel.value = 5;
        counterModel.increment();
        assertEquals(6, counterModel.value);
    }

    @Test
    void testIncrementFromLargeValue() {
        // Verify increment works with large values
        counterModel.value = 100;
        counterModel.increment();
        assertEquals(101, counterModel.value);
    }
}

