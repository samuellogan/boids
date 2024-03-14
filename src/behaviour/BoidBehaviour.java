package behaviour;

import java.util.List;

import models.Boid;

public interface BoidBehaviour {
    void applyBehavior(Boid boid, List<Boid> boids);
}
