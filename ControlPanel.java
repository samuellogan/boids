import javax.swing.*;
import java.awt.*;

public class ControlPanel extends JFrame {
    private final FlockSimulation simulation;

    public ControlPanel(FlockSimulation simulation) {
        this.simulation = simulation;
        setTitle("Simulation Controls");
        setSize(300, 600);
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        initializeSliders();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initializeSliders() {
        JSlider areaOfInfluenceSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
        areaOfInfluenceSlider.setMajorTickSpacing(10);
        areaOfInfluenceSlider.setPaintTicks(true);
        areaOfInfluenceSlider.setPaintLabels(true);
        areaOfInfluenceSlider.addChangeListener(e -> simulation.setAreaOfInfluence(areaOfInfluenceSlider.getValue()));
        JLabel areaOfInfluenceLabel = new JLabel("Area of Influence");
        add(areaOfInfluenceLabel);
        add(areaOfInfluenceSlider);

        JSlider fieldOfViewSlider = new JSlider(JSlider.HORIZONTAL, 0, 180, 90);
        fieldOfViewSlider.setMajorTickSpacing(45);
        fieldOfViewSlider.setPaintTicks(true);
        fieldOfViewSlider.setPaintLabels(true);
        fieldOfViewSlider
                .addChangeListener(e -> simulation.setFieldOfView(fieldOfViewSlider.getValue()));
        JLabel fieldOfViewLabel = new JLabel("Field of View");
        add(fieldOfViewLabel);
        add(fieldOfViewSlider);

        JSlider desiredVelocitySlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
        desiredVelocitySlider.setMajorTickSpacing(10);
        desiredVelocitySlider.setPaintTicks(true);
        desiredVelocitySlider.setPaintLabels(true);
        desiredVelocitySlider
                .addChangeListener(e -> simulation.setDesiredVelocity(desiredVelocitySlider.getValue()));
        JLabel desiredVelocityLabel = new JLabel("Desired Velocity");
        add(desiredVelocityLabel);
        add(desiredVelocitySlider);

        JSlider maxSteerForceSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
        maxSteerForceSlider.setMajorTickSpacing(10);
        maxSteerForceSlider.setPaintTicks(true);
        maxSteerForceSlider.setPaintLabels(true);
        maxSteerForceSlider
                .addChangeListener(e -> simulation.setMaxSteerForce(maxSteerForceSlider.getValue() / 500.0f));
        JLabel maxSteerForceLabel = new JLabel("Max Steer Force");
        add(maxSteerForceLabel);
        add(maxSteerForceSlider);
    }
}
