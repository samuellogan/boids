package models;

import java.util.List;

import behaviour.AlignmentBehaviour;
import behaviour.AvoidanceBehaviour;
import behaviour.BiasBehaviour;
import behaviour.BoidBehaviour;
import behaviour.CohesionBehaviour;
import behaviour.SpeedLimiterBehaviour;
import behaviour.WrapBehaviour;
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
    private boolean isBiased = false;

    public Vector position;
    public Vector velocity;
    public Vector acceleration;
    public Vector screenSize;

    private BoidBehaviour avoidanceBehavior;
    private BoidBehaviour alignmentBehavior;
    private BoidBehaviour cohesionBehavior;
    private BoidBehaviour biasBehaviour;
    private BoidBehaviour wrapBehaviour;
    private BoidBehaviour limitSpeedBehaviour;

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
        biasBehaviour = new BiasBehaviour();
        wrapBehaviour = new WrapBehaviour();
        limitSpeedBehaviour = new SpeedLimiterBehaviour();
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
        biasBehaviour.applyBehavior(this, boids);

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

        limitSpeedBehaviour.applyBehavior(this, boids);
        wrapBehaviour.applyBehavior(this, boids);

        acceleration.multiply(0);
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

    public void setBiased(boolean isBiased) {
        this.isBiased = isBiased;
    }

    public boolean isBiased() {
        return isBiased;
    }
}
