package com.ospeedless.module;

import com.ospeedless.setting.Setting;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;

public abstract class Module {

    private final String name;
    private final String description;
    private final Category category;
    private final String icon;
    private boolean enabled;
    private int keybind;
    private final List<Setting<?>> settings = new ArrayList<Setting<?>>();

    public Module(String name, String description, Category category, String icon) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.icon = icon;
        this.enabled = false;
        this.keybind = Keyboard.KEY_NONE;
    }

    public void onEnable() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    public void onDisable() {
        MinecraftForge.EVENT_BUS.unregister(this);
    }

    public void toggle() {
        setEnabled(!enabled);
    }

    public void setEnabled(boolean enabled) {
        if (this.enabled == enabled) return;
        this.enabled = enabled;
        if (enabled) {
            onEnable();
        } else {
            onDisable();
        }
    }

    public void onTick() {
    }

    public void addSetting(Setting<?> setting) {
        settings.add(setting);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Category getCategory() {
        return category;
    }

    public String getIcon() {
        return icon;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public int getKeybind() {
        return keybind;
    }

    public void setKeybind(int keybind) {
        this.keybind = keybind;
    }

    public List<Setting<?>> getSettings() {
        return settings;
    }

    public Setting<?> getSetting(String name) {
        for (Setting<?> s : settings) {
            if (s.getName().equalsIgnoreCase(name)) {
                return s;
            }
        }
        return null;
    }
}
