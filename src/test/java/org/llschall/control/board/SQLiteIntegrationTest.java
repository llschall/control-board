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
    private MeasurementRepository measurementRepository;

    @Test
    public void testSaveAndRetrieve() {
        Measurement measurement = new Measurement(42, LocalDateTime.now());
        Measurement saved = measurementRepository.save(measurement);
        
        Optional<Measurement> retrieved = measurementRepository.findById(saved.getId());
        
        assertTrue(retrieved.isPresent());
        assertEquals(42, retrieved.get().getValue());
    }
}
