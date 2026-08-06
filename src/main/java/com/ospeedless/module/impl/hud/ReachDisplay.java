package com.ospeedless.module.impl.hud;

import com.ospeedless.hud.HudElement;
import com.ospeedless.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.MovingObjectPosition;

public class ReachDisplay extends HudModule {

    public ReachDisplay() {
        super("Reach Display", "Shows distance to looked-at block/entity", "RCH");
        element = new HudElement(this, 2, 344) {
            @Override
            public void render(ScaledResolution sr) {
                Minecraft mc = Minecraft.getMinecraft();
                if (mc.thePlayer == null || mc.objectMouseOver == null) return;
                double dist = 0;
                if (mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                    dist = mc.thePlayer.getDistance(
                            mc.objectMouseOver.getBlockPos().getX() + 0.5,
                            mc.objectMouseOver.getBlockPos().getY() + 0.5,
                            mc.objectMouseOver.getBlockPos().getZ() + 0.5
                    );
                } else if (mc.objectMouseOver.entityHit != null) {
                    dist = mc.thePlayer.getDistanceToEntity(mc.objectMouseOver.entityHit);
                }
                String text = showLabel.getValue() ? String.format("Reach: %.1f", dist) : String.format("%.1f", dist);
                width = mc.fontRendererObj.getStringWidth(text);
                height = 10;
                RenderUtil.drawString(text, x, y, textColor.getRGB());
            }
        };
    }
}
