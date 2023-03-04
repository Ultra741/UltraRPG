package me.ultradev.ultrarpg.api.stats;

import java.util.Map;

public class StatInstance {

    private double value;
    private final Map<StatSource, Double> sources;

    public StatInstance(double value, Map<StatSource, Double> sources) {
        this.value = value;
        this.sources = sources;
    }

    public double getValue() {
        return value;
    }

    public Map<StatSource, Double> getSources() {
        return sources;
    }

    public void setValue(double value) {
        this.value = value;
    }

}
