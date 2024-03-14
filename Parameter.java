import java.util.function.BiConsumer;

class Parameter {
    private String name;
    private String description;
    private int minValue;
    private int maxValue;
    private int defaultValue;
    private BiConsumer<Float, FlockSimulation> onUpdate; // Adjusted for BiConsumer

    public Parameter(String name, String description, int minValue, int maxValue,
            BiConsumer<Float, FlockSimulation> onUpdate) {
        this.name = name;
        this.description = description;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.defaultValue = (maxValue + minValue) / 2;
        this.onUpdate = onUpdate;
    }

    public Parameter(String name, String description, int defaultValue, int minValue, int maxValue,
            BiConsumer<Float, FlockSimulation> onUpdate) {
        this.name = name;
        this.description = description;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.defaultValue = defaultValue;
        this.onUpdate = onUpdate;
    }

    // Method to execute the parameter update, passing in the simulation reference
    public void updateParameter(Float value, FlockSimulation simulation) {
        onUpdate.accept(value, simulation);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(int defaultValue) {
        this.defaultValue = defaultValue;
    }

    public int getMinValue() {
        return minValue;
    }

    public void SetMinValue(int minValue) {
        this.minValue = minValue;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public void SetMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    public BiConsumer<Float, FlockSimulation> getOnUpdate() {
        return onUpdate;
    }

    public void setOnUpdate(BiConsumer<Float, FlockSimulation> onUpdate) {
        this.onUpdate = onUpdate;
    }
}