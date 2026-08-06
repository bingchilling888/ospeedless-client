package com.ospeedless.setting;

public class TextSetting extends Setting<String> {

    public TextSetting(String name, String description, String defaultValue) {
        super(name, description, defaultValue);
    }

    @Override
    public String getDisplayValue() {
        return getValue();
    }
}
