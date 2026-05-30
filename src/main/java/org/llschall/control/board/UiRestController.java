package org.llschall.control.board;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class UiRestController {
    private final CounterViewModel viewModel;

    public UiRestController(CounterViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @GetMapping("/counter")
    public ResponseEntity<?> getCounter() {
        return ResponseEntity.ok(new CounterDto(viewModel.getCounterValue(), viewModel.getCounterDisplayText(), viewModel.isSwitchOn()));
    }

    @PostMapping("/increment")
    public ResponseEntity<?> increment() {
        viewModel.incrementCounter();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/counter")
    public ResponseEntity<?> updateCounter(@RequestParam int value) {
        viewModel.updateCounterValue(value);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/switch")
    public ResponseEntity<?> setSwitch(@RequestParam boolean on) {
        viewModel.setSwitchOn(on);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/measurements")
    public List<MeasurementDto> getMeasurements() {
        return toDtoList(viewModel.getAllMeasurements());
    }

    private List<MeasurementDto> toDtoList(Iterable<Measurement> measurements) {
        return ((List<Measurement>) ((measurements instanceof List) ? measurements : toList(measurements)))
                .stream()
                .map(m -> new MeasurementDto(m.getId(), m.getValue(), m.getTimestamp().toString()))
                .collect(Collectors.toList());
    }

    private List<Measurement> toList(Iterable<Measurement> iterable) {
        return (List<Measurement>) ((List<?>)
                java.util.stream.StreamSupport.stream(iterable.spliterator(), false).collect(Collectors.toList()));
    }

    static class CounterDto {
        public final int value;
        public final String display;
        public final boolean switchOn;

        public CounterDto(int value, String display, boolean switchOn) {
            this.value = value;
            this.display = display;
            this.switchOn = switchOn;
        }
    }

    static class MeasurementDto {
        public final Long id;
        public final int value;
        public final String timestamp;

        public MeasurementDto(Long id, int value, String timestamp) {
            this.id = id;
            this.value = value;
            this.timestamp = timestamp;
        }
    }
}

