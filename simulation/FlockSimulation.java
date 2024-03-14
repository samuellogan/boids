package simulation;

import javax.swing.*;

import models.Boid;
import behaviour.*;
import models.Parameter;
import models.ParameterGroup;
import ui.ControlPanel;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * A JPanel-based class that simulates flocking behavior using Boid objects.
 * This class initializes a specified number of Boid objects and simulates their
 * movement in a window, applying flocking rules to create natural, collective
 * movement patterns. The simulation uses a Swing Timer to update and repaint
 * the Boids at regular intervals.
 * 
 * @author Samuel Logan <contact@samuellogan.dev>
 */
public class FlockSimulation extends JPanel {
    // Timer used for animation updates
    Timer timer;
    // List of all Boids in the simulation
    List<Boid> boids = new ArrayList<>();
    // List of parameter groups for the control panel
    private List<ParameterGroup<Float>> parameterGroups;

    float protectedRange = 40.0f;
    float alignmentRange = 50.0f;
    float cohesionRange = 50.0f;
    float protectedFOV = 270.0f;

    float maxSpeed = 2.0f;
    float minSpeed = 4.0f;

    /**
     * Constructs the FlockSimulation panel, initializes the simulation environment,
     * and starts the simulation. This constructor sets the preferred size of the
     * panel, the background color, initializes the boids with random positions, and
     * sets up a timer to update the simulation at regular intervals.
     */
    public FlockSimulation() {
        parameterGroups = new ArrayList<>();
        initializeParameterGroups();

        // Set the size and background color of the simulation panel
        setPreferredSize(new Dimension(800, 600));
        Color backgroundColor = new Color(0x252628);
        setBackground(backgroundColor);

        // Setup the timer for regular updates
        timer = new Timer(16, e -> repaint());

        // Initialize boids with random positions within the panel's bounds
        for (int i = 0; i < 100; i++) {
            boids.add(new Boid((float) Math.random() * 800, (float) Math.random() * 600));
        }
        boids.get(0).setDebug(true); // Set the first boid to debug mode

        // Start the simulation
        timer.start();
    }

    private void initializeParameterGroups() {
        ParameterGroup<Float> alignmentGroup = AlignmentBehaviour.getParameters();
        ParameterGroup<Float> cohesionGroup = CohesionBehaviour.getParameters();
        ParameterGroup<Float> avoidanceGroup = AvoidanceBehaviour.getParameters();

        if (alignmentGroup != null)
            parameterGroups.add(alignmentGroup);
        if (cohesionGroup != null)
            parameterGroups.add(cohesionGroup);
        if (avoidanceGroup != null)
            parameterGroups.add(avoidanceGroup);
    }

    // Getter for parameter groups to be used by the UI
    public List<ParameterGroup<Float>> getParameterGroups() {
        return parameterGroups;
    }

    // Method to update a parameter value
    public void updateParameter(String parameterName, Float newValue) {
        // Find the parameter and update its value only if it's different
        for (ParameterGroup<Float> group : parameterGroups) {
            for (Parameter<Float> parameter : group.getParameters().values()) {
                if (parameter.getName().equals(parameterName) && !parameter.getValue().equals(newValue)) {
                    parameter.setValue(newValue);
                    break; // Break out of the loop once the correct parameter is updated
                }
            }
        }
    }

    private void applyParameterToSimulation(Parameter<Float> parameter) {
        // This method would apply the parameter change to the actual simulation.
        // It could update internal fields, adjust behavior weights, etc.
        // For example:
        switch (parameter.getCategory()) {
            case "Alignment":
                // Assume we have a method to set the alignment strength in the simulation
                setAlignmentStrength(parameter.getValue().floatValue());
                break;
            // Add cases for other categories
        }
    }

    // Example method that would be called when the alignment strength is updated
    private void setAlignmentStrength(float strength) {
        // Update the simulation's alignment behavior with the new strength
    }

    /**
     * Overrides the paintComponent method to draw each Boid in the simulation. This
     * method is called automatically by the repaint request in the Timer's
     * ActionListener. It updates each Boid's state and then draws it on the panel.
     *
     * @param g The Graphics object to protect.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g; // Cast to use Java2D features
        for (Boid boid : boids) {
            boid.update(boids);
            // Convert the direction of velocity to an angle
            double angle = Math.atan2(boid.velocity.y, boid.velocity.x);

            // Adjust these values to change the shape of the triangle
            int tipSize = 6; // Distance from center to tip
            int baseSize = 8; // Distance from center to base corners

            // Calculate the points for a pointier triangle
            int[] xPoints = {
                    (int) (boid.position.x + Math.cos(angle) * tipSize), // Tip
                    (int) (boid.position.x + Math.cos(angle + Math.PI - Math.PI / 6) * baseSize), // Base corner 1
                    (int) (boid.position.x + Math.cos(angle + Math.PI + Math.PI / 6) * baseSize) // Base corner 2
            };
            int[] yPoints = {
                    (int) (boid.position.y + Math.sin(angle) * tipSize), // Tip
                    (int) (boid.position.y + Math.sin(angle + Math.PI - Math.PI / 6) * baseSize), // Base corner 1
                    (int) (boid.position.y + Math.sin(angle + Math.PI + Math.PI / 6) * baseSize) // Base corner 2
            };

            // Draw the debug boid in a different color
            Color debugColor = new Color(0x1AB6E5);
            Color standardColor = new Color(0x808080);
            g2d.setColor(boid.isDebug() ? debugColor : standardColor);

            g2d.fillPolygon(xPoints, yPoints, 3);

            if (boid.isDebug())
                drawDebugInfo(g2d, boid);
        }
    }

    /**
     * Draws additional debug information for a debug boid, such as its vision
     * cone
     * 
     * @param g2d       The Graphics2D object to draw with.
     * @param debugBoid The boid to draw debug information for.
     */
    private void drawDebugInfo(Graphics2D g2d, Boid debugBoid) {
        drawFieldOfView(g2d, debugBoid, Color.RED, protectedRange, protectedFOV);
        drawFieldOfView(g2d, debugBoid, Color.BLUE, cohesionRange, 360);
        drawFieldOfView(g2d, debugBoid, Color.GREEN, alignmentRange, 360);
    }

    private void drawFieldOfView(Graphics2D g2d, Boid debugBoid, Color color, float areaOfInfluence,
            float fieldOfViewAngle) {
        // Calculate the boid's direction in radians and degrees
        double directionInRadians = Math.atan2(debugBoid.velocity.y, debugBoid.velocity.x);
        double directionInDegrees = Math.toDegrees(directionInRadians);

        // Calculate the start angle for the arc
        int startAngle = (int) (360 - (directionInDegrees + fieldOfViewAngle / 2));

        // Set drawing properties
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));

        // Calculate left and right angles of field of view
        int leftAngleDeg = (int) (directionInDegrees - (fieldOfViewAngle / 2));
        int rightAngleDeg = (int) (directionInDegrees + (fieldOfViewAngle / 2));

        // Convert angles to radians
        double leftAngleRad = Math.toRadians(leftAngleDeg);
        double rightAngleRad = Math.toRadians(rightAngleDeg);

        // Draw lines for FOV edges without an outline
        drawLineToFOV(g2d, debugBoid, leftAngleRad, color, areaOfInfluence);
        drawLineToFOV(g2d, debugBoid, rightAngleRad, color, areaOfInfluence);

        // Fill the FoV arc with 50% transparency
        int x = (int) (debugBoid.position.x - areaOfInfluence);
        int y = (int) (debugBoid.position.y - areaOfInfluence);
        int diameter = (int) (2 * areaOfInfluence);
        g2d.setColor(color);
        g2d.fillArc(x, y, diameter, diameter, startAngle, (int) fieldOfViewAngle);

        // Reset transparency
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
    }

    // Helper method to draw line to FOV edge without an outline
    private void drawLineToFOV(Graphics2D g2d, Boid specialBoid, double angleRad, Color color, float areaOfInfluence) {
        Stroke previousStroke = g2d.getStroke();
        g2d.setStroke(new BasicStroke(1)); // Set stroke width for the lines
        g2d.setColor(color);
        g2d.drawLine(
                (int) specialBoid.position.x,
                (int) specialBoid.position.y,
                (int) (specialBoid.position.x + Math.cos(angleRad) * areaOfInfluence),
                (int) (specialBoid.position.y + Math.sin(angleRad) * areaOfInfluence));
        g2d.setStroke(previousStroke); // Restore previous stroke
    }

    /**
     * The main method to run the FlockSimulation as an application.
     * It creates a JFrame to host the FlockSimulation panel.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FlockSimulation simulation = new FlockSimulation();
            JFrame frame = new JFrame("Flock Simulation");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(simulation);
            frame.pack();
            frame.setVisible(true);

            new ControlPanel(simulation);
        });
    }

    public void setAvoidRange(float range) {
        this.protectedRange = range;
        AvoidanceBehaviour.setRange(range);
    }

    public void setAvoidFOV(float fov) {
        this.protectedFOV = fov;
        AvoidanceBehaviour.setFOV(fov);
    }

    public void setAvoidFactor(float factor) {
        AvoidanceBehaviour.setFactor(factor);
    }

    public void setAlignFactor(float factor) {
        AlignmentBehaviour.setFactor(factor);
    }

    public void setAlignRange(float range) {
        this.alignmentRange = range;
        AlignmentBehaviour.setRange(range);
    }

    public void setCohereFactor(float factor) {
        CohesionBehaviour.setFactor(factor);
    }

    public void setCohereRange(float range) {
        this.cohesionRange = range;
        CohesionBehaviour.setRange(range);
    }

    public void setMinSpeed(float minSpeed) {
        this.minSpeed = minSpeed;
        for (Boid boid : boids) {
            boid.setMinSpeed(minSpeed);
        }
    }

    public void setMaxSpeed(float maxSpeed) {
        this.maxSpeed = maxSpeed;
        for (Boid boid : boids) {
            boid.setMaxSpeed(maxSpeed);
        }
    }
}
