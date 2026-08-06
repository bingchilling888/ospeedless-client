package com.ospeedless.module.impl.performance;

import com.ospeedless.module.Category;
import com.ospeedless.module.Module;
import net.minecraft.client.Minecraft;

public class Fullbright extends Module {

    private float prevGamma;

    public Fullbright() {
        super("Fullbright", "Increases gamma for better visibility", Category.PERFORMANCE, "FB");
    }

    @Override
    public void onEnable() {
        super.onEnable();
        prevGamma = Minecraft.getMinecraft().gameSettings.gammaSetting;
        Minecraft.getMinecraft().gameSettings.gammaSetting = 100.0f;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        Minecraft.getMinecraft().gameSettings.gammaSetting = prevGamma;
    }

    @Override
    public void onTick() {
        if (isEnabled()) {
            Minecraft.getMinecraft().gameSettings.gammaSetting = 100.0f;
        }
    }
}
