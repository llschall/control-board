package org.llschall.control.board;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class SQLiteIntegrationTest {

    @Autowired
    private CounterViewModel counterViewModel;

    @Autowired
    private MeasurementRepository measurementRepository;

    @Test
    public void testSaveAndRetrieve() {
        Measurement measurement = new Measurement(42, LocalDateTime.now());
        Measurement saved = measurementRepository.save(measurement);
        
        Optional<Measurement> retrieved = measurementRepository.findById(saved.getId());
        
        assertTrue(retrieved.isPresent());
        assertEquals(42, retrieved.get().getValue());
    }

    @Test
    public void testUpdateCounterValueSavesMeasurement() {
        long initialCount = measurementRepository.count();
        counterViewModel.updateCounterValue(99);
        assertEquals(initialCount + 1, measurementRepository.count());
        
        // Find the latest measurement
        Iterable<Measurement> measurements = measurementRepository.findAll();
        boolean found = false;
        for (Measurement m : measurements) {
            if (m.getValue() == 99) {
                found = true;
                break;
            }
        }
        assertTrue(found, "Measurement with value 99 should be saved");
    }
}
