package com.ospeedless.module.impl.utility;

import com.ospeedless.module.Category;
import com.ospeedless.module.Module;
import com.ospeedless.setting.BooleanSetting;
import com.ospeedless.setting.NumberSetting;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

public class Zoom extends Module {

    private final NumberSetting factor;
    private final BooleanSetting smooth;
    private float currentZoom = 1.0f;
    private boolean zooming;

    public Zoom() {
        super("Zoom", "Hold key to zoom in (default: C)", Category.UTILITY, "Z");
        factor = new NumberSetting("Factor", "Zoom multiplier", 3.0, 1.5, 10.0, 0.5);
        smooth = new BooleanSetting("Smooth", "Smooth zoom transition", true);
        addSetting(factor);
        addSetting(smooth);
        setKeybind(Keyboard.KEY_C);
    }

    @Override
    public void onTick() {
        Minecraft mc = Minecraft.getMinecraft();
        boolean keyDown = Keyboard.isKeyDown(getKeybind());
        if (keyDown && !zooming) {
            zooming = true;
        } else if (!keyDown && zooming) {
            zooming = false;
        }
        float target = zooming ? 1.0f / factor.getFloatValue() : 1.0f;
        if (smooth.getValue()) {
            currentZoom += (target - currentZoom) * 0.25f;
        } else {
            currentZoom = target;
        }
    }

    @SubscribeEvent
    public void onFOV(FOVUpdateEvent event) {
        if (!isEnabled()) return;
        event.newfov *= currentZoom;
    }
}
