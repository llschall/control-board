package org.llschall.control.board;

import org.llschall.ardwloop.ArdwloopStarter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class ArdwloopService {
    private static final Logger logger = LoggerFactory.getLogger(ArdwloopService.class);

    private final ArdwloopBridge bridge;

    public ArdwloopService(ArdwloopBridge bridge) {
        this.bridge = bridge;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void startArdwloop() {
        logger.info("Starting Ardwloop...");
        new Thread(() -> {
            try {
                ArdwloopStarter.get().start(bridge, 9600);
            } catch (Exception e) {
                logger.error("Error starting Ardwloop", e);
            }
        }).start();
    }
}
