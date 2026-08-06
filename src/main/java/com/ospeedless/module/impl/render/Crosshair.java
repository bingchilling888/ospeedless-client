package com.ospeedless.module.impl.render;

import com.ospeedless.module.Category;
import com.ospeedless.module.Module;
import com.ospeedless.setting.BooleanSetting;
import com.ospeedless.setting.ColorSetting;
import com.ospeedless.setting.NumberSetting;
import com.ospeedless.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Crosshair extends Module {

    private final NumberSetting size;
    private final NumberSetting thickness;
    private final NumberSetting gap;
    private final ColorSetting color;
    private final BooleanSetting outline;

    public Crosshair() {
        super("Crosshair", "Custom crosshair overlay", Category.RENDER, "+");
        size = new NumberSetting("Size", "Length of crosshair arms", 4, 1, 15, 1);
        thickness = new NumberSetting("Thickness", "Thickness of lines", 1, 1, 5, 1);
        gap = new NumberSetting("Gap", "Gap from center", 2, 0, 10, 1);
        color = new ColorSetting("Color", "Crosshair color", 0xFFFFFF);
        outline = new BooleanSetting("Outline", "Draw black outline", true);
        addSetting(size);
        addSetting(thickness);
        addSetting(gap);
        addSetting(color);
        addSetting(outline);
    }

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Pre event) {
        if (event.type != RenderGameOverlayEvent.ElementType.CROSSHAIRS) return;
        event.setCanceled(true);
    }

    @SubscribeEvent
    public void onRenderPost(RenderGameOverlayEvent.Post event) {
        if (event.type != RenderGameOverlayEvent.ElementType.ALL) return;
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.currentScreen != null || mc.gameSettings.thirdPersonView != 0) return;
        ScaledResolution sr = new ScaledResolution(mc);
        float cx = sr.getScaledWidth() / 2f;
        float cy = sr.getScaledHeight() / 2f;
        float s = size.getFloatValue();
        float t = thickness.getFloatValue();
        float g = gap.getFloatValue();
        int col = color.getValue() | 0xFF000000;

        if (outline.getValue()) {
            int black = 0xFF000000;
            // horizontal
            RenderUtil.drawRect(cx - g - s - 1, cy - t / 2 - 1, s + 2, t + 2, black);
            RenderUtil.drawRect(cx + g - 1, cy - t / 2 - 1, s + 2, t + 2, black);
            // vertical
            RenderUtil.drawRect(cx - t / 2 - 1, cy - g - s - 1, t + 2, s + 2, black);
            RenderUtil.drawRect(cx - t / 2 - 1, cy + g - 1, t + 2, s + 2, black);
        }
        RenderUtil.drawRect(cx - g - s, cy - t / 2, s, t, col);
        RenderUtil.drawRect(cx + g, cy - t / 2, s, t, col);
        RenderUtil.drawRect(cx - t / 2, cy - g - s, t, s, col);
        RenderUtil.drawRect(cx - t / 2, cy + g, t, s, col);
    }
}
