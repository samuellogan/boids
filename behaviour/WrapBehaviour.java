package behaviour;

import java.util.List;

import models.Boid;

public class WrapBehaviour implements BoidBehaviour {
    private static boolean isEnabled;
    private static boolean isDebugging;

    static {
        isEnabled = true;
    }

    public WrapBehaviour() {
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

        // Check horizontal boundaries
        if (boid.position.x < 0)
            boid.position.x += boid.screenSize.x;
        if (boid.position.x > boid.screenSize.x)
            boid.position.x -= boid.screenSize.x;

        // Check vertical boundaries
        if (boid.position.y < 0)
            boid.position.y += boid.screenSize.y;
        if (boid.position.y > boid.screenSize.y)
            boid.position.y -= boid.screenSize.y;
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
}
