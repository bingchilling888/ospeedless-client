package com.ospeedless.module;

public enum Category {
    HUD("HUD"),
    RENDER("Render"),
    PERFORMANCE("Performance"),
    UTILITY("Utility");

    private final String name;

    Category(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
