package com.ospeedless.module.impl.hud;

import com.ospeedless.hud.HudElement;
import com.ospeedless.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.MathHelper;

public class SpeedDisplay extends HudModule {

    public SpeedDisplay() {
        super("Speed Display", "Shows current movement speed", "SPD");
        element = new HudElement(this, 2, 332) {
            @Override
            public void render(ScaledResolution sr) {
                Minecraft mc = Minecraft.getMinecraft();
                if (mc.thePlayer == null) return;
                double dx = mc.thePlayer.posX - mc.thePlayer.prevPosX;
                double dz = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;
                double speed = MathHelper.sqrt_double(dx * dx + dz * dz) * 20.0;
                String text = showLabel.getValue() ? String.format("Speed: %.1f", speed) : String.format("%.1f", speed);
                width = mc.fontRendererObj.getStringWidth(text);
                height = 10;
                RenderUtil.drawString(text, x, y, textColor.getRGB());
            }
        };
    }
}
