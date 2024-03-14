package behaviour;

import java.util.List;

import models.Boid;
import models.Parameter;
import models.ParameterGroup;
import util.Vector;

public class AvoidanceBehaviour implements BoidBehaviour {
    private static Parameter factorParam;
    private static Parameter rangeParam;
    private static Parameter fovParam;
    private static ParameterGroup parameters;

    static {
        parameters = new ParameterGroup("Avoidance");

        rangeParam = new Parameter(
                "Avoidance",
                "Range",
                "Controls the perception range for avoidance",
                0.0f,
                30.0f,
                100.0f);
        parameters.addParameter(rangeParam);

        factorParam = new Parameter(
                "Avoidance",
                "Factor",
                "Controls how aggressively the boid steers to avoid other boids",
                0.0f,
                0.1f,
                0.2f);
        parameters.addParameter(factorParam);

        fovParam = new Parameter(
                "Avoidance",
                "Field Of View",
                "Controls the field of view for avoidance behavior",
                0.0f,
                270.0f,
                360.0f);
        parameters.addParameter(fovParam);
    }

    public AvoidanceBehaviour() {
    }

    /**
     * Method to apply the alignment behavior to the boid
     * 
     * @param boid  the boid to apply the behavior to
     * @param boids the list of boids in the simulation
     */
    @Override
    public void applyBehavior(Boid boid, List<Boid> boids) {
        float factor = factorParam.getValue();
        float range = rangeParam.getValue();
        float fovRad = (float) Math.toRadians(fovParam.getValue());

        Vector steer = new Vector(0, 0); // Initialize a vector for the steering force

        Vector boidVelocityNormalized = new Vector(boid.velocity.x, boid.velocity.y).normalize();

        for (Boid otherBoid : boids) {
            if (boid != otherBoid) {
                Vector toOtherBoid = Vector.sub(otherBoid.position, boid.position);
                float distance = toOtherBoid.magnitude();

                if (distance < range) {
                    toOtherBoid.normalize();
                    float angleBetween = (float) Math.acos(Vector.dot(boidVelocityNormalized, toOtherBoid));

                    if (angleBetween < fovRad / 2) {
                        // Normalize the vector to other boid, then invert and scale by the avoidFactor
                        // The scaling factor ensures that the closer the boids are, the stronger the
                        // steering force
                        Vector avoidanceForce = toOtherBoid.normalize().multiply(-1).divide(distance)
                                .multiply(range).multiply(factor);
                        steer.add(avoidanceForce);
                    }
                }
            }
        }

        // Apply the steering force to the boid's velocity
        boid.velocity.add(steer);
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
        AvoidanceBehaviour.factorParam.setValue(factor);
    }

    public static float getFactor() {
        return AvoidanceBehaviour.factorParam.getValue();
    }

    public static void setRange(float range) {
        AvoidanceBehaviour.rangeParam.setValue(range);
    }

    public static float getRange() {
        return AvoidanceBehaviour.rangeParam.getValue();
    }

    public static void setFOV(float fov) {
        AvoidanceBehaviour.fovParam.setValue(fov);
    }

    public static float getFOV() {
        return AvoidanceBehaviour.fovParam.getValue();
    }
}
