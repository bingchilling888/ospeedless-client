package com.ospeedless.setting;

public class NumberSetting extends Setting<Double> {

    private final double min;
    private final double max;
    private final double increment;

    public NumberSetting(String name, String description, double defaultValue, double min, double max, double increment) {
        super(name, description, defaultValue);
        this.min = min;
        this.max = max;
        this.increment = increment;
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    public double getIncrement() {
        return increment;
    }

    public void setValueClamped(double value) {
        value = Math.max(min, Math.min(max, value));
        value = Math.round(value / increment) * increment;
        setValue(value);
    }

    public int getIntValue() {
        return getValue().intValue();
    }

    public float getFloatValue() {
        return getValue().floatValue();
    }

    @Override
    public String getDisplayValue() {
        if (increment >= 1.0) {
            return String.valueOf(getIntValue());
        }
        return String.format("%.2f", getValue());
    }
}
