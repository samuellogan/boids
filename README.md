# Boids
A basic implementation of Craig Reynolds flocking simulation in Java using Swing.

## Behaviours
Each boid is constrained by a number of behaviours. While each of these behaviours is alone very simple and doesn't yield particularly interesing results, when several behaviours are combined some very interesting patterns start to emerge as boids negotiate both common and unique goals from each other.

### Speed Bounds
While not specifically a behaviour defined by [the paper](http://www.red3d.com/cwr/papers/1987/SIGGRAPH87.pdf), when boids collide or otherwise interact, it is possible for the boid to slow down significatly or completely stop. To avoid this, the velocity of each boid is clamped between a range. This can result in boids colliding, as they will be forced to travel at a minimum velocity even when it becomes possible to avoid a boid ahead however this issue is mitigated by other behaviours in the model.

### Avoidance
As we are effectively simulating schools of fish, or a flock of birds, the simulation doesn't appear particularly realistic if they regularly collide with each other at high velocity. To avoid this, we can allow each boid to "look ahead" and steer away from boids in its path. A problem that quickly arises with this method however, is that boids instantaniously turn away from as soon as they are aware of each other, making for a very unnaturnal "snap turn". To help with this, the avoidanceVelocity (the amount of velocity that needs to be added to the boid to avoid its neighbour) is multiplied by a factor, meaning it can only turn by a portion of what they want to with each update.

### Alignment
A behavour that the simulation massively benefits for each boid to align its velocity (rotation) with nearby boids. This means that when two or more boids get close enough to each other, they will effectively negotiate a velocity to both travel in together. Again, on its own this behaviour suffers from unnatural "snap turns" that can be voided by multiplying the alignmentVelocity by a factor.

### Cohesion
While boids aligning with its neighbours can help to show flocking behaviour, to better define each flock, boids can attract nearby neighbours to create groups. Just like with previous behaviours, cohesionVelocity is multiplied by a defined factor.

### Bias
A final behaviour that can be added is to simulate areas of interest to the model. This can represent a feeding ground, nesting ground or other area of interest for the boids. To do this, we can define a point in the simulation with a defined range. Boids outwith this range will be attracted towards the center of the point, again being multiplied be a defined factor to limit the turning strength of a boid. To add a further level of realism to the model we can only apply this behaviour to a subset of the boids. This also leads to more complex interactions between boids as they negotiate their desired location.
It should be noted that this bias behaviour could be further extended by giving groups of boids targets to reach, with the target area changing after they arrive at their destination.
