package org.llschall.control.board;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class CounterViewModel {
    private final CounterModel model;
    private final MeasurementRepository measurementRepository;

    public CounterViewModel(CounterModel model, MeasurementRepository measurementRepository) {
        this.model = model;
        this.measurementRepository = measurementRepository;
    }

    public int getCounterValue() {
        return model.value;
    }

    public String getCounterDisplayText() {
        return "Count: " + model.value;
    }

    public void incrementCounter() {
        model.increment();
        measurementRepository.save(new Measurement(model.value, LocalDateTime.now()));
    }

    public boolean isSwitchOn() {
        return model.switchOn;
    }

    public void setSwitchOn(boolean on) {
        model.switchOn = on;
    }

    public Iterable<Measurement> getAllMeasurements() {
        return measurementRepository.findAll();
    }
}
