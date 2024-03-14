package models;

public class Parameter {
    private String category;
    private String name;
    private String description;
    private float min;
    private float max;
    private float value;

    public Parameter(String category, String name, String description, float min, float defaultValue, float max) {
        this.category = category;
        this.name = name;
        this.description = description;
        this.min = min;
        this.max = max;
        this.value = defaultValue;
    }

    public String getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    public float getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public float getMin() {
        return min;
    }

    public float getMax() {
        return max;
    }

    public void setValue(float value) {
        if (value >= min && value <= max) {
            this.value = value;
        }
    }
}
