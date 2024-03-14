package models;

import java.util.List;
import java.util.ArrayList;

public class Parameter<T extends Float> {
    private String category;
    private String name;
    private String description;
    private T min;
    private T max;
    private T value;
    private List<ParameterListener<T>> listeners = new ArrayList<>();

    public Parameter(String category, String name, String description, T min, T defaultValue, T max) {
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

    public T getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public T getMin() {
        return min;
    }

    public T getMax() {
        return max;
    }

    public void setValue(T value) {
        if (value.floatValue() >= min.floatValue() && value.floatValue() <= max.floatValue()) {
            this.value = value;
            notifyListeners();
        }
    }

    public void addListener(ParameterListener<T> listener) {
        listeners.add(listener);
    }

    private void notifyListeners() {
        for (ParameterListener<T> listener : listeners) {
            listener.onParameterChanged(this);
        }
    }

    public interface ParameterListener<T extends Float> {
        void onParameterChanged(Parameter<T> parameter);
    }
}
