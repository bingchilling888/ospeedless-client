package com.ospeedless.module.impl.hud;

import com.ospeedless.hud.HudElement;
import com.ospeedless.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class SprintIndicator extends HudModule {

    public SprintIndicator() {
        super("Sprint Indicator", "Shows whether you are sprinting", "SPR");
        element = new HudElement(this, 2, 272) {
            @Override
            public void render(ScaledResolution sr) {
                Minecraft mc = Minecraft.getMinecraft();
                if (mc.thePlayer == null) return;
                boolean sprinting = mc.thePlayer.isSprinting();
                String text = showLabel.getValue() ? (sprinting ? "Sprinting" : "Walking") : (sprinting ? "ON" : "OFF");
                width = mc.fontRendererObj.getStringWidth(text);
                height = 10;
                RenderUtil.drawString(text, x, y, sprinting ? 0x55FF55 : textColor.getRGB());
            }
        };
    }
}
