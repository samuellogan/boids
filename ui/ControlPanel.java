package ui;

import javax.swing.*;
import javax.swing.event.ChangeListener;

import models.Parameter;
import models.ParameterGroup;
import simulation.FlockSimulation;

import java.awt.*;
import java.util.Hashtable;
import java.util.List;

public class ControlPanel extends JFrame implements Parameter.ParameterListener<Float> {
    private final FlockSimulation simulation;

    public ControlPanel(FlockSimulation simulation) {
        this.simulation = simulation;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Boid Simulation Parameters");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        // Create the UI components for each parameter group
        List<ParameterGroup<Float>> parameterGroups = simulation.getParameterGroups();
        for (ParameterGroup<Float> group : parameterGroups) {
            JPanel parameterPanel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridy = 0;
            gbc.anchor = GridBagConstraints.NORTHWEST;

            for (Parameter<Float> parameter : group.getParameters().values()) {
                parameter.addListener(this); // Add this ControlPanel as a listener
                parameterPanel.add(createParameterSlider(parameter), gbc);
                gbc.gridy++;
            }

            add(createSection(group.getName(), parameterPanel));
        }

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // Method to create sliders for each parameter
    private JPanel createParameterSlider(Parameter<Float> parameter) {
        JPanel parameterPanel = new JPanel(new BorderLayout());

        // Panel for name and description labels
        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.Y_AXIS));

        JLabel nameLabel = new JLabel(parameter.getName());
        nameLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 10));
        labelPanel.add(nameLabel); // Add name label to the label panel

        JLabel descriptionLabel = new JLabel(
                "<html><body style='width: 150px'>" + parameter.getDescription() + "</body></html>"); // Wrap text
        descriptionLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 8)); // Set smaller font size
        labelPanel.add(descriptionLabel); // Add description label under the name label

        parameterPanel.add(labelPanel, BorderLayout.WEST); // Add label panel to the west of the parameter panel

        // Use actual min and max values from the parameter
        float min = parameter.getMin();
        float max = parameter.getMax();
        float value = parameter.getValue();

        // Determine if we should scale the values for the slider
        boolean scale = min == 0.0f && max == 1.0f;
        int sliderMin = scale ? 0 : (int) min;
        int sliderMax = scale ? 100 : (int) max;
        int sliderValue = scale ? (int) (value * 100) : (int) value;

        JSlider slider = new JSlider(JSlider.HORIZONTAL, sliderMin, sliderMax, sliderValue);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);

        // Adjusting tick spacing based on the scale
        int majorTickSpacing = scale ? 25 : (sliderMax - sliderMin) / 4;
        int minorTickSpacing = scale ? 5 : (sliderMax - sliderMin) / 20;
        slider.setMajorTickSpacing(majorTickSpacing);
        slider.setMinorTickSpacing(minorTickSpacing);

        // Creating a label table for the slider
        Hashtable<Integer, JLabel> labelTable = new Hashtable<>();
        for (int i = sliderMin; i <= sliderMax; i += majorTickSpacing) {
            float labelValue = scale ? i / 100.0f : i;
            JLabel label = new JLabel(String.format("%.2f", labelValue));
            label.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 8));
            labelTable.put(i, label);
        }
        slider.setLabelTable(labelTable);

        // Set up the slider to listen for changes and adjust the parameter value
        // accordingly
        slider.addChangeListener(e -> {
            if (!slider.getValueIsAdjusting()) { // Check if the slider adjustment is finished
                float newValue = scale ? slider.getValue() / 100.0f : slider.getValue();
                // Only update if the new value is different to avoid unnecessary updates
                if (newValue != parameter.getValue()) {
                    // Temporarily remove the listener to avoid feedback loop
                    ChangeListener[] listeners = slider.getChangeListeners();
                    for (ChangeListener listener : listeners) {
                        slider.removeChangeListener(listener);
                    }

                    parameter.setValue(newValue); // Update the parameter value

                    // Re-add the listeners
                    for (ChangeListener listener : listeners) {
                        slider.addChangeListener(listener);
                    }
                }
            }
        });

        // Adding components to the parameter panel
        parameterPanel.add(labelPanel, BorderLayout.WEST);
        parameterPanel.add(slider, BorderLayout.EAST);

        return parameterPanel;
    }

    // This method will be called whenever a parameter value changes
    @Override
    public void onParameterChanged(Parameter<Float> parameter) {
        // React to the parameter change
        // For example, you could update the simulation parameters directly,
        // or refresh the UI component representing this parameter.
        simulation.updateParameter(parameter.getName(), parameter.getValue());
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