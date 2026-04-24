package org.llschall.boot.controller;

import org.springframework.stereotype.Component;

@Component
public class CounterViewModel {
    private final CounterModel model;

    public CounterViewModel(CounterModel model) {
        this.model = model;
    }

    public int getCounterValue() {
        return model.getValue();
    }

    public String getCounterDisplayText() {
        return "Count: " + model.getValue();
    }

    public void incrementCounter() {
        model.increment();
    }
}
