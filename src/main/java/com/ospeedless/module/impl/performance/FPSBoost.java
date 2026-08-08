package com.ospeedless.module.impl.performance;

import com.ospeedless.module.Category;
import com.ospeedless.module.Module;
import com.ospeedless.setting.BooleanSetting;
import com.ospeedless.setting.ModeSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;

public class FPSBoost extends Module {

    private final ModeSetting preset;
    private final BooleanSetting reduceParticles;
    private final BooleanSetting fastMath;

    private int prevParticles;
    private boolean prevFancy;

    public FPSBoost() {
        super("FPS Boost", "Applies safe performance optimizations for 1.8.9", Category.PERFORMANCE, "FPS");
        preset = new ModeSetting("Preset", "Performance preset", "Balanced", "Low", "Balanced", "High");
        reduceParticles = new BooleanSetting("Reduce Particles", "Lower particle count", true);
        fastMath = new BooleanSetting("Fast Math", "Use faster math approximations", true);
        addSetting(preset);
        addSetting(reduceParticles);
        addSetting(fastMath);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        Minecraft mc = Minecraft.getMinecraft();
        GameSettings gs = mc.gameSettings;
        prevParticles = gs.particleSetting;
        prevFancy = gs.fancyGraphics;

        if (preset.is("Low")) {
            gs.particleSetting = 2;
            gs.fancyGraphics = false;
            gs.ambientOcclusion = 0;
            gs.clouds = 0;
            gs.mipmapLevels = 0;
        } else if (preset.is("Balanced")) {
            gs.particleSetting = 1;
            gs.fancyGraphics = false;
            gs.ambientOcclusion = 1;
            gs.clouds = 1;
        } else {
            gs.particleSetting = 0;
            gs.fancyGraphics = true;
            gs.ambientOcclusion = 2;
        }
        if (reduceParticles.getValue() && gs.particleSetting < 2) {
            gs.particleSetting = Math.min(2, gs.particleSetting + 1);
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        Minecraft mc = Minecraft.getMinecraft();
        mc.gameSettings.particleSetting = prevParticles;
        mc.gameSettings.fancyGraphics = prevFancy;
    }
}
