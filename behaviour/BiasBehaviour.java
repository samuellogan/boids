package behaviour;

import java.util.List;

import models.Boid;
import models.Parameter;
import models.ParameterGroup;

public class BiasBehaviour implements BoidBehaviour {
    private static boolean isEnabled;
    private static boolean isDebugging;
    private static Parameter percentageParam;
    private static Parameter positionXParam;
    private static Parameter positionYParam;
    private static Parameter strengthParam;
    private static Parameter radiusParam;
    private static ParameterGroup parameters;

    static {
        parameters = new ParameterGroup("Bias");

        // Initialize parameters and add them to the group
        percentageParam = new Parameter(
                "Bias",
                "Position X",
                "Controls the percentage of boids the bias applies to",
                0.0f,
                50.0f,
                100.0f);
        parameters.addParameter(percentageParam);

        positionXParam = new Parameter(
                "Bias",
                "Position X",
                "Controls the X position of the bias",
                0.0f,
                0.5f,
                1.0f);
        parameters.addParameter(positionXParam);

        positionYParam = new Parameter(
                "Bias",
                "Position Y",
                "Controls the Y position of the bias",
                0.0f,
                0.5f,
                1.0f);
        parameters.addParameter(positionYParam);

        strengthParam = new Parameter(
                "Bias",
                "Strength",
                "Controls how strongly boids will be influenced by the bias",
                0.0f,
                0.05f,
                1.0f);
        parameters.addParameter(strengthParam);

        radiusParam = new Parameter(
                "Bias",
                "Radius",
                "Controls the radius of the target area around the bias position",
                0.0f, // Min value
                50.0f, // Default value, adjust based on your simulation's scale
                250.0f); // Max value, adjust based on your simulation's scale
        parameters.addParameter(radiusParam);

        isEnabled = true;
    }

    /**
     * Method to apply the alignment behavior to the boid
     * 
     * @param boid  the boid to apply the behavior to
     * @param boids the list of boids in the simulation
     */
    @Override
    public void applyBehavior(Boid boid, List<Boid> boids) {
        if (!isEnabled || !boid.isBiased())
            return;

        float strength = strengthParam.getValue();
        float targetPosX = positionXParam.getValue() * boid.screenSize.x;
        float targetPosY = positionYParam.getValue() * boid.screenSize.y;
        float radius = radiusParam.getValue();

        // Calculate the vector from the boid to the target position
        float vectorToTargetX = targetPosX - boid.position.x;
        float vectorToTargetY = targetPosY - boid.position.y;
        float distanceToTarget = (float) Math
                .sqrt(vectorToTargetX * vectorToTargetX + vectorToTargetY * vectorToTargetY);

        // Check if the boid is outside the target area
        if (distanceToTarget > radius) {
            // Normalize the vector to target
            vectorToTargetX /= distanceToTarget;
            vectorToTargetY /= distanceToTarget;

            // Apply the bias by adjusting the boid's velocity towards the target area
            boid.velocity.x += vectorToTargetX * strength / boid.velocity.magnitude();
            boid.velocity.y += vectorToTargetY * strength / boid.velocity.magnitude();
        }
    }

    /**
     * Method to get the alignment parameters
     * 
     * @return the alignment parameters
     */
    public static ParameterGroup getParameters() {
        return parameters;
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

    public static void setPercentage(float percentage) {
        percentageParam.setValue(percentage);
    }

    public static float getPercentage() {
        return percentageParam.getValue();
    }

    public static void setXPosition(float xPos) {
        positionXParam.setValue(xPos);
    }

    public static float getXPosition() {
        return positionXParam.getValue();
    }

    public static void setYPosition(float yPos) {
        positionYParam.setValue(yPos);
    }

    public static float getYPosition() {
        return positionYParam.getValue();
    }

    public static void setStrength(float strength) {
        strengthParam.setValue(strength);
    }

    public static float getStrength() {
        return strengthParam.getValue();
    }

    public static void setRadius(float radius) {
        radiusParam.setValue(radius);
    }

    public static float getRadius() {
        return radiusParam.getValue();
    }
}
