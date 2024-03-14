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

                add(createSection(group.getName(), parameterPanel, group.getName()));
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

        // Add a top margin to the nameLabel
        int topMargin = 5;
        int leftMargin = 0;
        int bottomMargin = 0;
        int rightMargin = 0;
        nameLabel.setBorder(BorderFactory.createEmptyBorder(topMargin, leftMargin, bottomMargin, rightMargin));

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

    private JPanel createSection(String title, JPanel contentPanel, String behaviorName) {
        JPanel sectionPanel = new JPanel();
        sectionPanel.setLayout(new BorderLayout());
        sectionPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(5, 5, 5, 5),
                BorderFactory.createTitledBorder(title)));

        // Create and configure the checkbox
        JCheckBox behaviorEnableCheckbox = new JCheckBox("Enable");
        behaviorEnableCheckbox.setSelected(FlockSimulation.isBehaviorEnabled(behaviorName));
        behaviorEnableCheckbox.addActionListener(e -> FlockSimulation.toggleBehaviorEnabled(behaviorName,
                behaviorEnableCheckbox.isSelected()));

        JCheckBox behaviorDebugCheckbox = new JCheckBox("Debug");
        behaviorDebugCheckbox.setSelected(FlockSimulation.isBehaviorDebugging(behaviorName));
        behaviorDebugCheckbox.addActionListener(e -> FlockSimulation.toggleBehaviorDebugging(behaviorName,
                behaviorDebugCheckbox.isSelected()));

        Font currentFont = behaviorEnableCheckbox.getFont();
        float smallerFontSize = currentFont.getSize() - 2.0f; // Decrease font size by 2
        behaviorEnableCheckbox.setFont(currentFont.deriveFont(smallerFontSize));
        behaviorDebugCheckbox.setFont(currentFont.deriveFont(smallerFontSize));

        // Use GridBagLayout for precise control over the checkbox's placement
        JPanel checkboxPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST; // Align the checkbox to the left
        gbc.insets = new Insets(0, 0, 0, 0); // No margins around the checkbox

        // Add the checkbox to the checkboxPanel with the constraints
        checkboxPanel.add(behaviorEnableCheckbox, gbc);
        checkboxPanel.add(behaviorDebugCheckbox, gbc);

        // Add the checkboxPanel to the sectionPanel, positioned at the top
        sectionPanel.add(checkboxPanel, BorderLayout.NORTH);

        // Add the contentPanel to the sectionPanel, in the center
        sectionPanel.add(contentPanel, BorderLayout.CENTER);

        return sectionPanel;
    }

}