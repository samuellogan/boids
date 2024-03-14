import javax.swing.*;
import java.awt.*;
import java.util.Hashtable;

public class ControlPanel extends JFrame {
    private final FlockSimulation simulation;

    public ControlPanel(FlockSimulation simulation) {
        this.simulation = simulation;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Boid Simulation Parameters");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        JPanel parameterPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        Behaviour.GLOBAL.getParameters().forEach(p -> {
            parameterPanel.add(createParameter(p), gbc);
            gbc.gridy++;
        });

        add(createSection("Global Constraints", parameterPanel));

        addBehaviorSections();

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void addBehaviorSections() {
        add(createSection("Avoidance", createParameterPanel(Behaviour.AVOIDANCE)));
        add(createSection("Alignment", createParameterPanel(Behaviour.ALIGNMENT)));
        add(createSection("Cohesion", createParameterPanel(Behaviour.COHESION)));
    }

    private JPanel createParameterPanel(Behaviour behaviour) {
        JPanel parameterPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.weightx = 1.0; // Give extra horizontal space to components

        // Add checkboxes
        gbc.insets = new Insets(0, 0, 0, 0);
        parameterPanel.add(createCheckBox("Enable", true), gbc);
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 10, 0);
        parameterPanel.add(createCheckBox("Show Debug Graphics", false), gbc);
        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.gridx = 0;
        gbc.gridy++;

        behaviour.getParameters().forEach(p -> {
            parameterPanel.add(createParameter(p), gbc);
            gbc.gridy++;
        });

        return parameterPanel;
    }

    private JCheckBox createCheckBox(String label, boolean isSelected) {
        JCheckBox checkBox = new JCheckBox(label, isSelected);
        checkBox.setFont(new Font("SansSerif", Font.PLAIN, 10)); // Reduce font size

        Insets margin = checkBox.getMargin();
        checkBox.setMargin(new Insets(margin.top - 2, margin.left - 2, margin.bottom - 2, margin.right - 2));

        return checkBox;
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

    private JPanel createParameter(Parameter parameter) {
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

        // Slider setup remains largely unchanged
        int minValue = parameter.getMinValue();
        int maxValue = parameter.getMaxValue();
        int initialValue = parameter.getDefaultValue();

        JSlider slider = new JSlider(JSlider.HORIZONTAL, minValue, maxValue, initialValue);
        slider.setMajorTickSpacing((maxValue - minValue) / 4);
        slider.setMinorTickSpacing((maxValue - minValue) / 10);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);

        Hashtable<Integer, JLabel> labelTable = new Hashtable<>();
        for (int i = minValue; i <= maxValue; i += (maxValue - minValue) / 4) {
            JLabel label = new JLabel(String.valueOf(i));
            label.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 8));
            labelTable.put(i, label);
        }
        slider.setLabelTable(labelTable);

        slider.addChangeListener(e -> {
            float value = slider.getValue();
            parameter.updateParameter(value, simulation); // Assuming 'simulation' is accessible
        });

        parameterPanel.add(slider, BorderLayout.EAST); // Add slider to the east of the parameter panel

        return parameterPanel;
    }
}
