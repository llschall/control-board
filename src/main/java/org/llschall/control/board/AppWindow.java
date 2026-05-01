package org.llschall.control.board;

import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.XChartPanel;
import org.llschall.ardwloop.ArdwloopStarter;
import org.llschall.ardwloop.structure.model.ArdwloopModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

@Component
public class AppWindow {
    private static final Logger logger = LoggerFactory.getLogger(AppWindow.class);

    private final CounterViewModel viewModel;
    private JLabel counterLabel;
    private CategoryChart chart;
    private final List<Integer> chartHistory;

    public AppWindow(CounterViewModel viewModel) {
        this.viewModel = viewModel;
        this.chartHistory = new ArrayList<>();
    }

    @EventListener(ApplicationReadyEvent.class)
    public void showWindow() {
        // Skip GUI creation in headless environments (e.g., during testing)
        if (GraphicsEnvironment.isHeadless()) {
            logger.debug("Headless mode detected - window will not be displayed");
            return;
        }

        try {
            JFrame frame = new JFrame("Control Board");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);

            JPanel mainPanel = new JPanel(new BorderLayout());

            // Top panel with counter control
            JPanel controlPnl = new JPanel(new FlowLayout());
            controlPnl.add(new JLabel("Java: "+System.getProperty("java.version")));
            controlPnl.add(new JLabel("ardwloop: "+ArdwloopStarter.VERSION));
            counterLabel = new JLabel(viewModel.getCounterDisplayText());
            controlPnl.add(counterLabel);

            // Increment button (View triggers ViewModel action)
            JButton incrementButton = new JButton("Increment");
            incrementButton.addActionListener(e -> {
                viewModel.incrementCounter();
                updateCounterDisplay();
                updateChart();
            });
            controlPnl.add(incrementButton);

            // Close button
            JButton closeButton = new JButton("Close");
            closeButton.addActionListener(e -> System.exit(0));
            controlPnl.add(closeButton);

            mainPanel.add(controlPnl, BorderLayout.NORTH);

            // Center panel with chart
            chart = createBarChart();
            XChartPanel<CategoryChart> chartPanel = new XChartPanel<>(chart);
            mainPanel.add(chartPanel, BorderLayout.CENTER);

            frame.add(mainPanel);

            // Add Escape key listener to exit
            frame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                    .put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "exitAction");
            frame.getRootPane().getActionMap().put("exitAction", new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.exit(0);
                }
            });

            frame.setVisible(true);
        } catch (Exception e) {
            logger.error("Failed to create window", e);
        }
    }

    private CategoryChart createBarChart() {
        CategoryChart chart = new CategoryChartBuilder()
                .title("Counter History")
                .xAxisTitle("Sample")
                .yAxisTitle("Count Value")
                .build();

        // Initialize with first data point
        chartHistory.add(viewModel.getCounterValue());
        List<String> labels = new ArrayList<>(List.of("0"));

        chart.addSeries("Counter Values", labels, chartHistory);
        return chart;
    }

    private void updateCounterDisplay() {
        counterLabel.setText(viewModel.getCounterDisplayText());
    }

    private void updateChart() {
        int currentValue = viewModel.getCounterValue();
        chartHistory.add(currentValue);

        // Create labels for x-axis
        List<String> labels = new ArrayList<>();
        for (int i = 0; i < chartHistory.size(); i++) {
            labels.add(String.valueOf(i));
        }

        // Update chart
        chart.removeSeries("Counter Values");
        chart.addSeries("Counter Values", labels, chartHistory);
    }
}
