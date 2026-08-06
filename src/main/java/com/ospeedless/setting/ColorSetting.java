package com.ospeedless.setting;

public class ColorSetting extends Setting<Integer> {

    public ColorSetting(String name, String description, int defaultColor) {
        super(name, description, defaultColor);
    }

    public int getRed() {
        return (getValue() >> 16) & 0xFF;
    }

    public int getGreen() {
        return (getValue() >> 8) & 0xFF;
    }

    public int getBlue() {
        return getValue() & 0xFF;
    }

    public int getAlpha() {
        return (getValue() >> 24) & 0xFF;
    }

    public int getRGB() {
        return getValue() & 0xFFFFFF;
    }

    @Override
    public String getDisplayValue() {
        return String.format("#%06X", getRGB());
    }
}
