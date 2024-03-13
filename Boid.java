import java.util.List;

/**
 * Represents a Boid, an autonomous agent designed to simulate behaviors such as
 * flocking, alignment, cohesion, and separation in a group of similar agents.
 * Boids are often used in computer graphics to simulate the collective behavior
 * of animals, such as birds or fish.
 * 
 * @author Samuel Logan <contact@samuellogan.dev>
 */
public class Boid {
    // One of the boids is set to be in "debug mode" to display its area of
    // influence, field of view and other useful information
    public boolean isDebug;
    // The position of the Boid in 2D space.
    Vector position;
    // The velocity of the Boid, determining its speed and direction.
    Vector velocity;
    // The acceleration of the Boid, influencing its change in velocity.
    Vector acceleration;
    // The size of the screen, used to wrap Boids around the borders.
    Vector screenSize;

    // Field of View (FoV) angle in radians (e.g., PI/2 for 90 degrees)
    float fov = (float) Math.PI / 2;
    // Area of influence (radius within which it reacts to other boids)
    float areaOfInfluence = 50;

    float desiredVelocity;
    float maxSteerForce;

    /**
     * Constructs a Boid given an initial position. The Boid's velocity is
     * initialized to a random 2D unit vector to give it an initial direction,
     * and its acceleration is initialized to zero.
     *
     * @param x The initial x-coordinate of the Boid's position.
     * @param y The initial y-coordinate of the Boid's position.
     */
    public Boid(float x, float y) {
        isDebug = false;
        position = new Vector(x, y);
        velocity = Vector.random2D();
        acceleration = new Vector(0, 0);
        screenSize = new Vector(800, 600); // Assume a default screen size of 800x600 to avoid null pointer exceptions
    }

    /**
     * Updates the Boid's position and velocity based on its current acceleration.
     * After updating, it resets the acceleration for the next cycle and ensures
     * that the Boid wraps around the screen borders, creating a toroidal space.
     */
    public void update(List<Boid> boids) {
        Vector avoidForce = avoidOthers(boids);
        acceleration.add(avoidForce);

        // Calculate new velocity based on current acceleration
        Vector newVelocity = new Vector(velocity.x, velocity.y);
        newVelocity.add(acceleration);

        // Adjust the direction of velocity without changing its magnitude (speed)
        if (newVelocity.magnitude() > 0) {
            newVelocity.normalize(); // Set direction
            newVelocity.multiply(velocity.magnitude()); // Maintain original speed
        }

        velocity = newVelocity; // Apply the adjusted velocity
        position.add(velocity); // Update position based on the adjusted velocity

        wrapAroundBorders(); // Handle screen wrapping
        acceleration.multiply(0); // Reset acceleration after each update
    }

    /**
     * Ensures that the Boid wraps around the edges of the screen, appearing on the
     * opposite side if it moves past the border. This creates an effect of
     * continuous space where Boids can roam freely without disappearing once they
     * reach the screen's limits.
     */
    private void wrapAroundBorders() {
        // Check horizontal boundaries
        if (position.x < 0)
            position.x += screenSize.x;
        if (position.x > screenSize.x)
            position.x -= screenSize.x;

        // Check vertical boundaries
        if (position.y < 0)
            position.y += screenSize.y;
        if (position.y > screenSize.y)
            position.y -= screenSize.y;
    }

    private Vector avoidOthers(List<Boid> boids) {
        Vector steer = new Vector(0, 0);
        int count = 0;

        for (Boid other : boids) {
            float distance = Vector.dist(this.position, other.position);
            float angle = Vector.angleBetween(this.velocity, Vector.sub(other.position, this.position));

            if (other != this && distance < areaOfInfluence && angle < fov) {
                Vector diff = Vector.sub(this.position, other.position);
                diff.normalize();
                // Instead of dividing by distance, consider using a different approach to
                // ensure smooth steering
                diff.multiply(1 / distance); // This emphasizes changing direction more smoothly
                steer.add(diff);
                count++;
            }
        }

        if (count > 0) {
            steer.divide((float) count);
        }

        if (steer.magnitude() > 0) {
            // Apply the steering force more as a directional change rather than directly
            // affecting speed
            steer.normalize();
            steer.multiply(maxSteerForce); // Use maxSteerForce to adjust the steer vector
        }

        return steer;
    }

    /**
     * Sets the size of the screen for the Boid to use when wrapping around the
     * edges.
     * 
     * @param width  The width of the screen.
     * @param height The height of the screen.
     */
    public void setScreenSize(float width, float height) {
        screenSize = new Vector(width, height);
    }

    /**
     * Sets the area of influence for the Boid, which determines the radius within
     * which it reacts to other Boids.
     * 
     * @param areaOfInfluence The new area of influence for the Boid.
     */
    public void setAreaOfInfluence(float areaOfInfluence) {
        this.areaOfInfluence = areaOfInfluence;
    }

    /**
     * Sets the field of view for the Boid, which determines the angle within which
     * it reacts to other Boids.
     * 
     * @param fov The new field of view for the Boid in radians.
     */
    public void setFieldOfView(float fov) {
        this.fov = fov;
    }

    /**
     * Sets the desired velocity for the Boid, which determines the speed at which
     * it
     * steers.
     * 
     * @param desiredVelocity The new desired velocity for the Boid.
     */
    public void setDesiredVelocity(float desiredVelocity) {
        this.desiredVelocity = desiredVelocity;
    }

    /**
     * Sets the maximum steering force for the Boid, which determines the maximum
     * force it can apply to steer.
     * 
     * @param maxSteerForce The new maximum steering force for the Boid.
     */
    public void setMaxSteerForce(float maxSteerForce) {
        this.maxSteerForce = maxSteerForce;
    }
}
