package com.ospeedless.module.impl.utility;

import com.ospeedless.module.Category;
import com.ospeedless.module.Module;
import com.ospeedless.setting.ModeSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

public class ToggleSprint extends Module {

    private final ModeSetting mode;
    private boolean sprintToggled;
    private boolean sneakToggled;

    public ToggleSprint() {
        super("Toggle Sprint", "Toggle sprint and/or sneak", Category.UTILITY, "TS");
        mode = new ModeSetting("Mode", "What to toggle", "Sprint", "Sprint", "Sneak", "Both");
        addSetting(mode);
        setKeybind(Keyboard.KEY_V);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        sprintToggled = false;
        sneakToggled = false;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        Minecraft mc = Minecraft.getMinecraft();
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindSprint.getKeyCode(), false);
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), false);
    }

    @Override
    public void onTick() {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null) return;

        if (mode.is("Sprint") || mode.is("Both")) {
            if (sprintToggled) {
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindSprint.getKeyCode(), true);
                mc.thePlayer.setSprinting(true);
            }
        }
        if (mode.is("Sneak") || mode.is("Both")) {
            if (sneakToggled) {
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), true);
            }
        }
    }

    @Override
    public void toggle() {
        // Custom toggle behavior for modes
        if (!isEnabled()) {
            setEnabled(true);
            if (mode.is("Sprint") || mode.is("Both")) sprintToggled = true;
            if (mode.is("Sneak") || mode.is("Both")) sneakToggled = true;
        } else {
            if (mode.is("Sprint") || mode.is("Both")) {
                sprintToggled = !sprintToggled;
            }
            if (mode.is("Sneak") || mode.is("Both")) {
                sneakToggled = !sneakToggled;
            }
            if (!sprintToggled && !sneakToggled) {
                setEnabled(false);
            }
        }
    }
}
