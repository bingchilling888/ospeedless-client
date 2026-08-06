package com.ospeedless.module.impl.performance;

import com.ospeedless.module.Category;
import com.ospeedless.module.Module;
import com.ospeedless.setting.NumberSetting;
import net.minecraft.client.Minecraft;

public class FrameUnlock extends Module {

    private final NumberSetting limit;
    private int prevLimit;

    public FrameUnlock() {
        super("Frame Unlock", "Sets a custom FPS limit (0 = unlimited)", Category.PERFORMANCE, "FU");
        limit = new NumberSetting("FPS Limit", "Maximum FPS (0 = uncapped)", 0, 0, 1000, 5);
        addSetting(limit);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        Minecraft mc = Minecraft.getMinecraft();
        prevLimit = mc.gameSettings.limitFramerate;
        apply();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        Minecraft.getMinecraft().gameSettings.limitFramerate = prevLimit;
    }

    @Override
    public void onTick() {
        apply();
    }

    private void apply() {
        int val = limit.getIntValue();
        if (val <= 0) {
            Minecraft.getMinecraft().gameSettings.limitFramerate = 260; // high practical cap
        } else {
            Minecraft.getMinecraft().gameSettings.limitFramerate = val;
        }
    }
}
