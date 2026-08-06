package com.ospeedless.util;

import org.lwjgl.input.Keyboard;

public class KeybindManager {

    public void init() {
        // Keybinds are handled per-module
    }

    public static String getKeyName(int key) {
        if (key == Keyboard.KEY_NONE) return "NONE";
        try {
            return Keyboard.getKeyName(key);
        } catch (Exception e) {
            return "KEY" + key;
        }
    }
}
