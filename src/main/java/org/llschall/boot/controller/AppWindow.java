package org.llschall.boot.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;

@Component
public class AppWindow {
    private static final Logger logger = LoggerFactory.getLogger(AppWindow.class);

    private final CounterViewModel viewModel;
    private JLabel counterLabel;

    public AppWindow(CounterViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void showWindow() {
        // Skip GUI creation in headless environments (e.g., during testing)
        if (GraphicsEnvironment.isHeadless()) {
            logger.debug("Headless mode detected - window will not be displayed");
            return;
        }

        try {
            JFrame frame = new JFrame("Boot Controller");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 300);
            frame.setLocationRelativeTo(null);

            JPanel panel = new JPanel();
            panel.setLayout(new FlowLayout());

            // Counter display (View bound to ViewModel)
            counterLabel = new JLabel(viewModel.getCounterDisplayText());
            panel.add(counterLabel);

            // Increment button (View triggers ViewModel action)
            JButton incrementButton = new JButton("Increment");
            incrementButton.addActionListener(e -> {
                viewModel.incrementCounter();
                updateCounterDisplay();
            });
            panel.add(incrementButton);

            // Close button
            JButton closeButton = new JButton("Close");
            closeButton.addActionListener(e -> System.exit(0));
            panel.add(closeButton);

            frame.add(panel);
            frame.setVisible(true);
        } catch (Exception e) {
            logger.error("Failed to create window", e);
        }
    }

    private void updateCounterDisplay() {
        counterLabel.setText(viewModel.getCounterDisplayText());
    }
}
