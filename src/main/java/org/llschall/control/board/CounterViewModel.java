package org.llschall.control.board;

import org.springframework.stereotype.Component;

@Component
public class CounterViewModel {
    private final CounterModel model;

    public CounterViewModel(CounterModel model) {
        this.model = model;
    }

    public int getCounterValue() {
        return model.value;
    }

    public String getCounterDisplayText() {
        return "Count: " + model.value;
    }

    public void incrementCounter() {
        model.increment();
    }
}
