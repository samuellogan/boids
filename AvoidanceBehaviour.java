import java.util.List;

public class AvoidanceBehaviour implements BoidBehaviour {

    private final float avoidFactor;
    private final float avoidRange;
    private final float fieldOfViewRadians;

    public AvoidanceBehaviour(float avoidFactor, float avoidRange, float fieldOfViewDegrees) {
        this.avoidFactor = avoidFactor;
        this.avoidRange = avoidRange;
        this.fieldOfViewRadians = (float) Math.toRadians(fieldOfViewDegrees); // Convert degrees to radians
    }

    @Override
    public void applyBehavior(Boid boid, List<Boid> boids) {
        Vector steer = new Vector(0, 0); // Initialize a vector for the steering force

        Vector boidVelocityNormalized = new Vector(boid.velocity.x, boid.velocity.y).normalize();

        for (Boid otherBoid : boids) {
            if (boid != otherBoid) {
                Vector toOtherBoid = Vector.sub(otherBoid.position, boid.position);
                float distance = toOtherBoid.magnitude();

                if (distance < avoidRange) {
                    toOtherBoid.normalize();
                    float angleBetween = (float) Math.acos(Vector.dot(boidVelocityNormalized, toOtherBoid));

                    if (angleBetween < fieldOfViewRadians / 2) {
                        // Normalize the vector to other boid, then invert and scale by the avoidFactor
                        // The scaling factor ensures that the closer the boids are, the stronger the
                        // steering force
                        Vector avoidanceForce = toOtherBoid.normalize().multiply(-1).divide(distance)
                                .multiply(avoidRange).multiply(avoidFactor);
                        steer.add(avoidanceForce);
                    }
                }
            }
        }

        // Apply the steering force to the boid's velocity
        boid.velocity.add(steer);
    }
}
