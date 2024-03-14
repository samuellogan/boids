package ui;

import javax.swing.*;

import simulation.FlockSimulation;
import models.Parameter;
import models.ParameterGroup;

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
        setTitle("Boid Simulation Parameters");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create a main panel with BoxLayout to hold the content and set padding via
        // border
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        int padding = 10; // Adjust padding size as needed
        mainPanel.setBorder(BorderFactory.createEmptyBorder(padding, padding, padding, padding));

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

            // Instead of adding directly to the frame, add to the main panel
            mainPanel.add(createSection(group.getName(), parameterPanel, group.getName()));
        }

        // Finally, add the main panel to the frame's content pane
        getContentPane().add(mainPanel);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // Method to create sliders for each parameter
    private JPanel createParameterSlider(Parameter parameter) {
        JPanel parameterPanel = new JPanel(new BorderLayout());

        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.Y_AXIS));

        JLabel nameLabel = createLabel(parameter.getName(), Font.BOLD, 10);
        JLabel descriptionLabel = createLabel(
                "<html><body style='width: 150px'>" + parameter.getDescription() + "</body></html>", Font.PLAIN, 8);

        nameLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

        labelPanel.add(nameLabel);
        labelPanel.add(descriptionLabel);
        parameterPanel.add(labelPanel, BorderLayout.WEST);

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
        JPanel sectionPanel = new JPanel(new BorderLayout());

        // Panel for the title and checkboxes, using GridBagLayout for precise control
        JPanel topPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Configure the title label on the left
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 2; // Span across 2 rows for alignment with checkboxes
        gbc.anchor = GridBagConstraints.WEST;

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font(titleLabel.getFont().getName(), Font.BOLD, 20));
        topPanel.add(titleLabel, gbc);

        // Reset gridheight for checkboxes (though not strictly necessary for
        // side-by-side layout)
        gbc.gridheight = 1;

        // Configure the "Enable" checkbox on the right
        gbc.gridx = 1; // Position "Enable" checkbox in the second column
        gbc.gridy = 0; // Both checkboxes in the first row
        gbc.weightx = 0; // Do not let checkboxes expand horizontally
        gbc.anchor = GridBagConstraints.EAST; // Align to the east side but within the same block

        JCheckBox enableCheckbox = new JCheckBox("Enable", FlockSimulation.isBehaviorEnabled(behaviorName));
        JCheckBox debugCheckbox = new JCheckBox("Debug", FlockSimulation.isBehaviorDebugging(behaviorName));

        enableCheckbox.addActionListener(
                e -> FlockSimulation.toggleBehaviorEnabled(behaviorName, enableCheckbox.isSelected()));
        debugCheckbox.addActionListener(
                e -> FlockSimulation.toggleBehaviorDebugging(behaviorName, debugCheckbox.isSelected()));

        Font originalFont = enableCheckbox.getFont();
        Font smallerFont = new Font(originalFont.getName(), originalFont.getStyle(), originalFont.getSize() - 2);

        enableCheckbox.setFont(smallerFont);
        debugCheckbox.setFont(smallerFont);

        topPanel.add(debugCheckbox, gbc);
        gbc.gridx = 2;
        topPanel.add(enableCheckbox, gbc);

        // Adding spacing and alignment adjustments if necessary
        gbc.insets = new Insets(0, 5, 0, 5); // Add padding around components if needed

        // Combine everything into the sectionPanel
        sectionPanel.add(topPanel, BorderLayout.NORTH);
        sectionPanel.add(contentPanel, BorderLayout.CENTER);

        return sectionPanel;
    }

    private JLabel createLabel(String text, int style, int size) {
        JLabel label = new JLabel(text);
        label.setFont(new Font(Font.SANS_SERIF, style, size));
        return label;
    }
}