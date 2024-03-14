import java.util.List;

public enum Behaviour {
    AVOIDANCE(
            List.of(
                    new Parameter("Protected Range",
                            "Defines the radius within which the boid detects other nearby boids and avoids them", 40,
                            0, 100, (value, simulation) -> simulation.setProtectedRange(value)),
                    new Parameter("Protected Field Of View",
                            "Defines the angle within which the boid detects other nearby boids and avoids them", 270,
                            0, 360,
                            (value, simulation) -> simulation.setProtectedFOV(value)),
                    new Parameter("Steer Strength", "The strength of the steering force applied to avoid other boids",
                            10, 0, 100, (value, simulation) -> simulation.setProtectedAvoidFactor(value / 100)))),
    ALIGNMENT(
            List.of(
                    new Parameter("Matching Factor",
                            "The strength of the steering force applied to match the direction of other boids",
                            5, 0, 100, (value, simulation) -> simulation.setMatchingFactor(value / 1000)),
                    new Parameter("Alignment Range",
                            "The range within which the boid matches the direction of other boids", 50, 0, 100,
                            (value, simulation) -> simulation.setAlignmentRange(value)))),
    COHESION(
            List.of(new Parameter("Matching Factor",
                    "The strength of the steering force applied to match the direction of other boids",
                    5, 0, 100, (value, simulation) -> simulation.setCenteringFactor(value / 1000)),
                    new Parameter("Alignment Range",
                            "The range within which the boid matches the direction of other boids", 50, 0, 100,
                            (value, simulation) -> simulation.setCohesionRange(value)))),
    GLOBAL(
            List.of(
                    new Parameter("Minimum Speed", "The minimum speed of the boid", 2, 1, 10,
                            (value, simulation) -> simulation.setMinSpeed(value)),
                    new Parameter("Maximum Speed", "The maximum speed of the boid", 4, 1, 10,
                            (value, simulation) -> simulation.setMaxSpeed(value))));

    private final List<Parameter> parameters;

    Behaviour(List<Parameter> parameters) {
        this.parameters = parameters;
    }

    public List<Parameter> getParameters() {
        return parameters;
    }
}