package models;

import java.util.HashMap;
import java.util.Map;

public class ParameterGroup {
    private Map<String, Parameter> parameters;
    private String name;

    public ParameterGroup(String name) {
        this.name = name;
        this.parameters = new HashMap<>();
    }

    public void addParameter(Parameter parameter) {
        parameters.put(parameter.getName(), parameter);
    }

    public Parameter getParameter(String name) {
        return parameters.get(name);
    }

    public String getName() {
        return name;
    }

    public Map<String, Parameter> getParameters() {
        return parameters;
    }
}
