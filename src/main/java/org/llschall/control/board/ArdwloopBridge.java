package org.llschall.control.board;

import org.llschall.ardwloop.IArdwProgram;
import org.llschall.ardwloop.value.SerialData;
import org.springframework.stereotype.Component;

@Component
public class ArdwloopBridge implements IArdwProgram {

    private final CounterModel model;

    public ArdwloopBridge(CounterModel model) {
        this.model = model;
    }

    @Override
    public SerialData ardwSetup(SerialData data) {
        return data;
    }

    @Override
    public SerialData ardwLoop(SerialData data) {
        if (model.switchOn) {
            data.a.v = 1;
        }
        model.value = data.a.v;
        data.b.v = model.value;
        return data;
    }
}
