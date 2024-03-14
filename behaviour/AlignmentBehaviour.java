package behaviour;

import java.util.List;

import models.Boid;
import util.Vector;

public class AlignmentBehaviour implements BoidBehaviour {

    private final float visibleRange;
    private final float matchingFactor;

    public AlignmentBehaviour(float visibleRange, float matchingFactor) {
        this.visibleRange = visibleRange;
        this.matchingFactor = matchingFactor;
    }

    @Override
    public void applyBehavior(Boid boid, List<Boid> boids) {
        float xvelAvg = 0;
        float yvelAvg = 0;
        int neighboringBoids = 0;

        for (Boid otherBoid : boids) {
            if (boid != otherBoid) {
                float distance = Vector.dist(boid.position, otherBoid.position);
                if (distance < visibleRange) {
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
            boid.velocity.x += (xvelAvg - boid.velocity.x) * matchingFactor;
            boid.velocity.y += (yvelAvg - boid.velocity.y) * matchingFactor;
        }
    }
}
