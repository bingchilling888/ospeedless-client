package com.ospeedless.module.impl.hud;

import com.ospeedless.hud.HudElement;
import com.ospeedless.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class HealthDisplay extends HudModule {

    public HealthDisplay() {
        super("Health Display", "Shows player health and absorption", "HP");
        element = new HudElement(this, 2, 356) {
            @Override
            public void render(ScaledResolution sr) {
                Minecraft mc = Minecraft.getMinecraft();
                if (mc.thePlayer == null) return;
                float hp = mc.thePlayer.getHealth();
                float abs = mc.thePlayer.getAbsorptionAmount();
                String text = showLabel.getValue()
                        ? String.format("HP: %.1f%s", hp, abs > 0 ? " +" + (int) abs : "")
                        : String.format("%.1f%s", hp, abs > 0 ? " +" + (int) abs : "");
                width = mc.fontRendererObj.getStringWidth(text);
                height = 10;
                RenderUtil.drawString(text, x, y, textColor.getRGB());
            }
        };
    }
}
