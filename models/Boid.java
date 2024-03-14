package models;

import java.util.List;

import behaviour.AlignmentBehaviour;
import behaviour.AvoidanceBehaviour;
import behaviour.BoidBehaviour;
import behaviour.CohesionBehaviour;
import util.Vector;

/**
 * Represents a Boid, an autonomous agent designed to simulate behaviors such as
 * flocking, alignment, cohesion, and separation in a group of similar agents.
 * Boids are often used in computer graphics to simulate the collective behavior
 * of animals, such as birds or fish.
 * 
 * @author Samuel Logan <contact@samuellogan.dev>
 */
public class Boid {
    private boolean isDebug = false;

    public Vector position;
    public Vector velocity;
    public Vector acceleration;
    public Vector screenSize;

    private BoidBehaviour avoidanceBehavior;
    private BoidBehaviour alignmentBehavior;
    private BoidBehaviour cohesionBehavior;

    private float minSpeed = 2.0f;
    private float maxSpeed = 4.0f;

    /**
     * Constructs a Boid given an initial position. The Boid's velocity is
     * initialized to a random 2D unit vector to give it an initial direction,
     * and its acceleration is initialized to zero.
     *
     * @param x The initial x-coordinate of the Boid's position.
     * @param y The initial y-coordinate of the Boid's position.
     */
    public Boid(float x, float y) {
        position = new Vector(x, y);
        velocity = Vector.random2D();
        acceleration = new Vector(0, 0);
        screenSize = new Vector(800, 600);

        avoidanceBehavior = new AvoidanceBehaviour();
        alignmentBehavior = new AlignmentBehaviour();
        cohesionBehavior = new CohesionBehaviour();
    }

    /**
     * Updates the Boid's position and velocity based on its current acceleration.
     * After updating, it resets the acceleration for the next cycle and ensures
     * that the Boid wraps around the screen borders, creating a toroidal space.
     */
    public void update(List<Boid> boids) {
        avoidanceBehavior.applyBehavior(this, boids);
        alignmentBehavior.applyBehavior(this, boids);
        cohesionBehavior.applyBehavior(this, boids);

        // Calculate new velocity based on current acceleration
        Vector newVelocity = new Vector(velocity.x, velocity.y);
        newVelocity.add(acceleration);

        // Adjust the direction of velocity without changing its magnitude (speed)
        if (newVelocity.magnitude() > 0) {
            newVelocity.normalize(); // Set direction
            newVelocity.multiply(velocity.magnitude()); // Maintain original speed
        }

        // Apply the adjusted velocity and update the position
        velocity = newVelocity;
        position.add(velocity);

        velocity.limit(minSpeed, maxSpeed);

        wrapAroundBorders();
        acceleration.multiply(0);
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

    /**
     * Sets the minimum speed for the Boid.
     * 
     * @param minSpeed The minimum speed of the Boid.
     */
    public void setMinSpeed(float minSpeed) {
        this.minSpeed = minSpeed;
    }

    /**
     * Sets the maximum speed for the Boid.
     * 
     * @param maxSpeed The maximum speed of the Boid.
     */
    public void setMaxSpeed(float maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    /**
     * Sets the debug mode for the Boid. When debug mode is enabled, the Boid will
     * display additional information, such as its protected area and field of view.
     * 
     * @param isDebug Whether to enable debug mode.
     */
    public void setDebug(boolean isDebug) {
        this.isDebug = isDebug;
    }

    /**
     * Gets the debug mode for the Boid.
     * 
     * @return Whether debug mode is enabled.
     */
    public boolean isDebug() {
        return isDebug;
    }
}
