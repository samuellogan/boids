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
    public void update() {
        velocity.add(acceleration);
        position.add(velocity);
        wrapAroundBorders();
        acceleration.multiply(0); // Reset acceleration to 0 after each update to prevent perpetual acceleration
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
}
