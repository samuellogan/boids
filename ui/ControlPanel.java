package ui;

import javax.swing.*;

import models.Parameter;
import models.ParameterGroup;
import simulation.FlockSimulation;

import java.awt.*;
import java.util.Hashtable;
import java.util.List;

public class ControlPanel extends JFrame {
    private final FlockSimulation simulation;

    public ControlPanel(FlockSimulation simulation) {
        this.simulation = simulation;
        initializeUI();
    }

    private void initializeUI() {
        try {
            setTitle("Boid Simulation Parameters");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

            // Create the UI components for each parameter group
            List<ParameterGroup> parameterGroups = simulation.getParameterGroups();
            for (ParameterGroup group : parameterGroups) {
                JPanel parameterPanel = new JPanel(new GridBagLayout());
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.gridy = 0;
                gbc.anchor = GridBagConstraints.NORTHWEST;

                for (Parameter parameter : group.getParameters().values()) {
                    parameterPanel.add(createParameterSlider(parameter), gbc);
                    gbc.gridy++;
                }

                add(createSection(group.getName(), parameterPanel));
            }

            pack();
            setLocationRelativeTo(null);
            setVisible(true);
        } catch (Exception e) {
            e.printStackTrace(); // Log the exception
        }
    }

    // Method to create sliders for each parameter
    private JPanel createParameterSlider(Parameter parameter) {
        JPanel parameterPanel = new JPanel(new BorderLayout());

        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.Y_AXIS));

        JLabel nameLabel = new JLabel(parameter.getName());
        nameLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 10));
        labelPanel.add(nameLabel);

        JLabel descriptionLabel = new JLabel(
                "<html><body style='width: 150px'>" + parameter.getDescription() + "</body></html>"); // Wrap text
        descriptionLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 8));
        labelPanel.add(descriptionLabel);

        parameterPanel.add(labelPanel, BorderLayout.WEST); // Add label panel to the west of the parameter panel

        float min = parameter.getMin();
        float max = parameter.getMax();
        float value = parameter.getValue();
        float range = max - min;

        int sliderMin = 0;
        int sliderMax = 100;
        // Calculate based on how the actual value maps to the slider range (0-100)
        int sliderValue = (int) (((value - min) / range) * 100);

        JSlider slider = new JSlider(JSlider.HORIZONTAL, sliderMin, sliderMax, sliderValue);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);

        int majorTickSpacing = 25; // Every 25% on the slider
        int minorTickSpacing = 5; // Every 5% on the slider

        slider.setMajorTickSpacing(majorTickSpacing);
        slider.setMinorTickSpacing(minorTickSpacing);

        // Creating a labelTable for the slider to reflects the actual parameter range
        Hashtable<Integer, JLabel> labelTable = new Hashtable<>();
        for (int i = sliderMin; i <= sliderMax; i += majorTickSpacing) {
            // Calculate the actual value this slider position represents
            float labelValue = (i * (max - min) / 100) + min;

            // Choose label format based on the parameter's range
            String labelFormat = range <= 1.0f ? "%.2f" : "%.0f";
            JLabel label = new JLabel(String.format(labelFormat, labelValue));

            label.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 8));
            labelTable.put(i, label);
        }
        slider.setLabelTable(labelTable);

        slider.addChangeListener(e -> {
            // Convert the slider's value back to the actual parameter range
            float newValue = (slider.getValue() * (max - min) / 100) + min;
            parameter.setValue(newValue);
        });

        parameterPanel.add(labelPanel, BorderLayout.WEST);
        parameterPanel.add(slider, BorderLayout.EAST);

        return parameterPanel;
    }

    private JPanel createSection(String title, JPanel contentPanel) {
        JPanel sectionPanel = new JPanel();
        sectionPanel.setLayout(new BorderLayout());
        sectionPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(5, 5, 5, 5),
                BorderFactory.createTitledBorder(title)));
        sectionPanel.add(contentPanel, BorderLayout.CENTER);
        return sectionPanel;
    }
}