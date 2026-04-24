package org.llschall.boot.controller;

import org.springframework.stereotype.Component;

@Component
public class CounterModel {
    private int value = 0;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void increment() {
        this.value++;
    }
}
