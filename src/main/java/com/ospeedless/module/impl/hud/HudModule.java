package com.ospeedless.module.impl.hud;

import com.ospeedless.hud.HudElement;
import com.ospeedless.module.Category;
import com.ospeedless.module.Module;
import com.ospeedless.setting.BooleanSetting;
import com.ospeedless.setting.ColorSetting;
import com.ospeedless.setting.NumberSetting;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public abstract class HudModule extends Module {

    protected HudElement element;
    protected BooleanSetting showLabel;
    protected ColorSetting textColor;
    protected NumberSetting scale;

    public HudModule(String name, String description, String icon) {
        super(name, description, Category.HUD, icon);
        showLabel = new BooleanSetting("Show Label", "Show the label text", true);
        textColor = new ColorSetting("Text Color", "Color of the text", 0xFFFFFF);
        scale = new NumberSetting("Scale", "Size scale of the element", 1.0, 0.5, 2.0, 0.1);
        addSetting(showLabel);
        addSetting(textColor);
        addSetting(scale);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        if (element != null) {
            element.setScale(scale.getFloatValue());
        }
    }

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Post event) {
        if (event.type != RenderGameOverlayEvent.ElementType.ALL) return;
        if (mc == null || mc.thePlayer == null) return;
        if (mc.currentScreen != null) return;
        if (mc.gameSettings.showDebugInfo) return;
        if (element != null) {
            element.setScale(scale.getFloatValue());
            element.render(new ScaledResolution(mc));
        }
    }

    protected net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getMinecraft();

    public HudElement getElement() {
        return element;
    }
}
