package behaviour;

import java.util.List;

import models.Boid;
import models.Parameter;
import models.ParameterGroup;
import util.Vector;

public class CohesionBehaviour implements BoidBehaviour {

    private static Parameter rangeParam;
    private static Parameter factorParam;
    private static ParameterGroup parameters;

    static {
        parameters = new ParameterGroup("Cohesion");

        // Initialize parameters and add them to the group
        rangeParam = new Parameter(
                "Cohesion",
                "Cohesion Range",
                "Controls the perception range for alignment",
                0.0f,
                50.0f,
                100.0f);
        parameters.addParameter(rangeParam);

        factorParam = new Parameter(
                "Cohesion",
                "Cohesion Factor",
                "Controls the strength of cohesion behavior",
                0.0f,
                0.025f,
                0.05f);
        parameters.addParameter(factorParam);
    }

    public CohesionBehaviour() {
    }

    @Override
    public void applyBehavior(Boid boid, List<Boid> boids) {
        float range = rangeParam.getValue();
        float factor = factorParam.getValue();

        float xposAvg = 0;
        float yposAvg = 0;
        int neighboringBoids = 0;

        for (Boid otherBoid : boids) {
            if (boid != otherBoid) {
                float distance = Vector.dist(boid.position, otherBoid.position);
                if (distance < range) {
                    xposAvg += otherBoid.position.x;
                    yposAvg += otherBoid.position.y;
                    neighboringBoids++;
                }
            }
        }

        if (neighboringBoids > 0) {
            xposAvg /= neighboringBoids;
            yposAvg /= neighboringBoids;

            // Steer the boid towards the average position of its neighbors
            boid.velocity.x += (xposAvg - boid.position.x) * factor;
            boid.velocity.y += (yposAvg - boid.position.y) * factor;
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

    public static void setRange(float range) {
        rangeParam.setValue(range);
    }

    public static float getRange() {
        return rangeParam.getValue();
    }

    public static void setFactor(float factor) {
        factorParam.setValue(factor);
    }

    public static float getFactor() {
        return factorParam.getValue();
    }
}
