package behaviour;

import java.util.List;

import models.Boid;
import models.Parameter;
import models.ParameterGroup;

public class SpeedLimiterBehaviour implements BoidBehaviour {
    private static boolean isEnabled;
    private static boolean isDebugging;
    private static Parameter minSpeedParam;
    private static Parameter maxSpeedParam;
    private static ParameterGroup parameters;

    static {
        parameters = new ParameterGroup("Speed Limiter");

        // Initialize parameters and add them to the group
        minSpeedParam = new Parameter(
                "Speed Limiter",
                "Min Speed",
                "Controls the minimum speed at which a boid can travel",
                0.0f,
                3.0f,
                10.0f);
        parameters.addParameter(minSpeedParam);

        maxSpeedParam = new Parameter(
                "Speed Limiter",
                "Max Speed",
                "Controls the maximum speed at which a boid can travel",
                0.0f,
                6.0f,
                10.0f);
        parameters.addParameter(maxSpeedParam);

        isEnabled = true;
    }

    public SpeedLimiterBehaviour() {
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

        float minSpeed = minSpeedParam.getValue();
        float maxSpeed = maxSpeedParam.getValue();

        boid.velocity.limit(minSpeed, maxSpeed);
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

    public static void setMinSpeed(float speed) {
        minSpeedParam.setValue(speed);
    }

    public static float getMinSpeed() {
        return minSpeedParam.getValue();
    }

    public static void setMaxSpeed(float speed) {
        maxSpeedParam.setValue(speed);
    }

    public static float getMaxSpeed() {
        return maxSpeedParam.getValue();
    }

}
