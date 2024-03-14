package behaviour;

import java.util.List;

import models.Boid;
import models.Parameter;
import models.ParameterGroup;
import util.Vector;

public class AlignmentBehaviour implements BoidBehaviour {
    // All parameters are static so that they can be accessed from the ControlPanel
    // All boids share the same parameters
    private static boolean isEnabled;
    private static boolean isDebugging;
    private static Parameter rangeParam;
    private static Parameter factorParam;
    private static ParameterGroup parameters;

    static {
        parameters = new ParameterGroup("Alignment");

        // Initialize parameters and add them to the group
        rangeParam = new Parameter(
                "Visibility",
                "Alignment Range",
                "Controls the perception range for alignment",
                0.0f,
                50.0f,
                100.0f);
        parameters.addParameter(rangeParam);

        factorParam = new Parameter(
                "Behavior Strength",
                "Alignment Factor",
                "Controls how much the boid steers towards the average velocity (direction) of its neighbors",
                0.0f,
                0.1f,
                0.2f);
        parameters.addParameter(factorParam);
    }

    public AlignmentBehaviour() {
    }

    /**
     * Method to apply the alignment behavior to the boid
     * 
     * @param boid  the boid to apply the behavior to
     * @param boids the list of boids in the simulation
     */
    @Override
    public void applyBehavior(Boid boid, List<Boid> boids) {
        if (!isEnabled)
            return;

        float xvelAvg = 0;
        float yvelAvg = 0;
        int neighboringBoids = 0;

        float range = rangeParam.getValue();
        float factor = factorParam.getValue();

        for (Boid otherBoid : boids) {
            if (boid != otherBoid) {
                float distance = Vector.dist(boid.position, otherBoid.position);
                if (distance < range) {
                    xvelAvg += otherBoid.velocity.x;
                    yvelAvg += otherBoid.velocity.y;
                    neighboringBoids++;
                }
            }
        }

        if (neighboringBoids > 0) {
            xvelAvg /= neighboringBoids;
            yvelAvg /= neighboringBoids;

            // Adjust the boid's velocity towards the average velocity of its neighbors
            boid.velocity.x += (xvelAvg - boid.velocity.x) * factor;
            boid.velocity.y += (yvelAvg - boid.velocity.y) * factor;
        }
    }

    public static boolean isEnabled() {
        return isEnabled;
    }

    public static void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public static boolean isDebugging() {
        return isDebugging;
    }

    public static void setDebugging(boolean debugging) {
        isDebugging = debugging;
    }

    /**
     * Method to get the alignment parameters
     * 
     * @return the alignment parameters
     */
    public static ParameterGroup getParameters() {
        return parameters;
    }

    public static void setFactor(float factor) {
        factorParam.setValue(factor);
    }

    public static float getFactor() {
        return factorParam.getValue();
    }

    public static void setRange(float range) {
        rangeParam.setValue(range);
    }

    public static float getRange() {
        return rangeParam.getValue();
    }
}
