package org.llschall.control.board;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class Measurement {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private int value;

    private LocalDateTime timestamp;

    public Measurement() {
    }

    public Measurement(int value, LocalDateTime timestamp) {
        this.value = value;
        this.timestamp = timestamp;
    }

    public Long getId() {
        return id;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
