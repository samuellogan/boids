import javax.swing.*;
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

    /**
     * Constructs the FlockSimulation panel, initializes the simulation environment,
     * and starts the simulation. This constructor sets the preferred size of the
     * panel, the background color, initializes the boids with random positions, and
     * sets up a timer to update the simulation at regular intervals.
     */
    public FlockSimulation() {
        // Set the size and background color of the simulation panel
        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.BLACK);

        // Setup the timer for regular updates
        timer = new Timer(16, e -> repaint()); // Approx. 60 FPS

        // Initialize boids with random positions within the panel's bounds
        for (int i = 0; i < 100; i++) {
            boids.add(new Boid((float) Math.random() * 800, (float) Math.random() * 600));
        }

        // Start the simulation
        timer.start();
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
        // Update and draw each boid

        Dimension size = getSize(); // Get current panel size
        for (Boid boid : boids) {
            boid.setScreenSize(size.width, size.height); // Pass current size to Boid
            boid.update(); // Update the Boid's state
            // Flocking rules can be applied within the Boid's update method or before
            // drawing

            // Draw the Boid as a white oval
            g.setColor(Color.WHITE);
            g.fillOval((int) boid.position.x, (int) boid.position.y, 5, 5);
        }
    }

    /**
     * The main method to run the FlockSimulation as an application.
     * It creates a JFrame to host the FlockSimulation panel.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        JFrame frame = new JFrame("Flock Simulation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new FlockSimulation()); // Add the simulation panel to the frame
        frame.pack(); // Fit the frame size to the preferred size of the panel
        frame.setVisible(true); // Display the frame
    }
}
