package org.llschall.control.board;

import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
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
import javax.swing.JCheckBox;
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
import javax.swing.Timer;
import java.util.ArrayList;
import java.util.List;

@Component
public class AppWindow {
    private static final Logger logger = LoggerFactory.getLogger(AppWindow.class);

    private final CounterViewModel viewModel;
    private final ArdwloopService ardwloopService;
    private JLabel counterLabel;
    private XYChart chart;
    private final List<Integer> chartHistory;

    public AppWindow(CounterViewModel viewModel, ArdwloopService ardwloopService) {
        this.viewModel = viewModel;
        this.ardwloopService = ardwloopService;
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
            
            // Switch button
            JCheckBox ledBox = new JCheckBox("Led");
            ledBox.setSelected(viewModel.isSwitchOn());
            ledBox.addActionListener(e -> viewModel.setSwitchOn(ledBox.isSelected()));
            controlPnl.add(ledBox);

            // Start Ardwloop button
            JButton startButton = new JButton("Start");
            startButton.addActionListener(e -> {
                ardwloopService.startArdwloop();
                startButton.setEnabled(false);
            });
            controlPnl.add(startButton);

            // Close button
            JButton closeButton = new JButton("Close");
            closeButton.addActionListener(e -> System.exit(0));
            controlPnl.add(closeButton);

            mainPanel.add(controlPnl, BorderLayout.NORTH);

            // Center panel with chart
            chart = createCurveChart();
            XChartPanel<XYChart> chartPanel = new XChartPanel<>(chart);
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

            // Periodically refresh the UI to show updates from Ardwloop
            Timer timer = new Timer(100, e -> {
                updateCounterDisplay();
                updateChart();
            });
            timer.start();
        } catch (Exception e) {
            logger.error("Failed to create window", e);
        }
    }

    private XYChart createCurveChart() {
        XYChart chart = new XYChartBuilder()
                .title("Counter History")
                .xAxisTitle("Sample")
                .yAxisTitle("Count Value")
                .build();

        // Initialize with first data point
        chartHistory.add(viewModel.getCounterValue());
        List<Integer> xData = new ArrayList<>(List.of(0));

        XYSeries series = chart.addSeries("Counter Values", xData, chartHistory);
        chart.getStyler().setToolTipsEnabled(true);
        chart.getStyler().setToolTipType(org.knowm.xchart.style.Styler.ToolTipType.yLabels);
        chart.getStyler().setCursorEnabled(true);
        return chart;
    }

    private void updateCounterDisplay() {
        counterLabel.setText(viewModel.getCounterDisplayText());
    }

    private void updateChart() {
        int currentValue = viewModel.getCounterValue();
        if (!chartHistory.isEmpty() && chartHistory.get(chartHistory.size() - 1) == currentValue) {
            return;
        }
        chartHistory.add(currentValue);

        // Keep last 100 points
        if (chartHistory.size() > 100) {
            chartHistory.remove(0);
        }

        // Create labels for x-axis
        List<Integer> xData = new ArrayList<>();
        for (int i = 0; i < chartHistory.size(); i++) {
            xData.add(i);
        }

        // Update chart
        chart.updateXYSeries("Counter Values", xData, chartHistory, null);
    }
}
