package models;

import java.util.HashMap;
import java.util.Map;

public class ParameterGroup<T extends Float> {
    private Map<String, Parameter<Float>> parameters;
    private String name;

    public ParameterGroup(String name) {
        this.name = name;
        this.parameters = new HashMap<>();
    }

    public void addParameter(Parameter<Float> parameter) {
        parameters.put(parameter.getName(), parameter);
    }

    public Parameter<Float> getParameter(String name) {
        return parameters.get(name);
    }

    public String getName() {
        return name;
    }

    public Map<String, Parameter<Float>> getParameters() {
        return parameters;
    }
}
