import java.util.List;

public class CohesionBehaviour implements BoidBehaviour {

    private final float visibleRange;
    private final float centeringFactor;

    public CohesionBehaviour(float visibleRange, float centeringFactor) {
        this.visibleRange = visibleRange;
        this.centeringFactor = centeringFactor;
    }

    @Override
    public void applyBehavior(Boid boid, List<Boid> boids) {
        float xposAvg = 0;
        float yposAvg = 0;
        int neighboringBoids = 0;

        for (Boid otherBoid : boids) {
            if (boid != otherBoid) {
                float distance = Vector.dist(boid.position, otherBoid.position);
                if (distance < visibleRange) {
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
            boid.velocity.x += (xposAvg - boid.position.x) * centeringFactor;
            boid.velocity.y += (yposAvg - boid.position.y) * centeringFactor;
        }
    }
}
