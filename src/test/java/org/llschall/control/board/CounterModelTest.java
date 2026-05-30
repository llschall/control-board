package org.llschall.control.board;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class CounterModelTest {

    @Autowired
    private CounterModel counterModel;

    @BeforeEach
    void setUp() {
        // Reset the autowired model's state between tests for isolation
        counterModel.value = 0;
        counterModel.switchOn = false;
    }

    @Test
    void testInitialValue() {
        // Verify that the counter starts at 0
        assertEquals(0, counterModel.value);
    }

    @Test
    void testIncrementOnce() {
        // Increment counter once and verify value is 1
        counterModel.switchOn = true;
        counterModel.increment();
        assertEquals(1, counterModel.value);
    }

    @Test
    void testIncrementMultipleTimes() {
        // Increment counter multiple times and verify cumulative value
        counterModel.switchOn = true;
        counterModel.increment();
        counterModel.increment();
        counterModel.increment();
        assertEquals(3, counterModel.value);
    }

    @Test
    void testIncrementFromNonZeroValue() {
        // Set initial value and verify increment works
        counterModel.switchOn = true;
        counterModel.value = 5;
        counterModel.increment();
        assertEquals(6, counterModel.value);
    }

    @Test
    void testIncrementFromLargeValue() {
        // Verify increment works with large values
        counterModel.switchOn = true;
        counterModel.value = 100;
        counterModel.increment();
        assertEquals(101, counterModel.value);
    }

    @Test
    void testSwitchOn() {
        assertEquals(false, counterModel.switchOn);
        counterModel.switchOn = true;
        assertEquals(true, counterModel.switchOn);
    }

    @Test
    void testIncrementWhileSwitchOff() {
        counterModel.switchOn = false;
        counterModel.value = 10;
        counterModel.increment();
        assertEquals(10, counterModel.value);
    }
}

