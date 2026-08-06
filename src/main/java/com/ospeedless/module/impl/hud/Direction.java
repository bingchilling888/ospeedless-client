package com.ospeedless.module.impl.hud;

import com.ospeedless.hud.HudElement;
import com.ospeedless.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.MathHelper;

public class Direction extends HudModule {

    private static final String[] DIRS = {"S", "SW", "W", "NW", "N", "NE", "E", "SE"};

    public Direction() {
        super("Direction", "Shows facing direction", "DIR");
        element = new HudElement(this, 2, 284) {
            @Override
            public void render(ScaledResolution sr) {
                Minecraft mc = Minecraft.getMinecraft();
                if (mc.thePlayer == null) return;
                int yaw = MathHelper.floor_double((double) (mc.thePlayer.rotationYaw * 8.0F / 360.0F) + 0.5D) & 7;
                String dir = DIRS[yaw];
                String text = showLabel.getValue() ? "Facing: " + dir : dir;
                width = mc.fontRendererObj.getStringWidth(text);
                height = 10;
                RenderUtil.drawString(text, x, y, textColor.getRGB());
            }
        };
    }
}
