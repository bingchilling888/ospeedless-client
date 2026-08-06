package com.ospeedless.module.impl.hud;

import com.ospeedless.hud.HudElement;
import com.ospeedless.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class TimeDisplay extends HudModule {

    public TimeDisplay() {
        super("Time Display", "Shows in-game time", "TIME");
        element = new HudElement(this, 2, 308) {
            @Override
            public void render(ScaledResolution sr) {
                Minecraft mc = Minecraft.getMinecraft();
                if (mc.theWorld == null) return;
                long time = mc.theWorld.getWorldTime() % 24000;
                int hours = (int) ((time / 1000 + 6) % 24);
                int minutes = (int) ((time % 1000) * 60 / 1000);
                String text = showLabel.getValue() ? String.format("Time: %02d:%02d", hours, minutes) : String.format("%02d:%02d", hours, minutes);
                width = mc.fontRendererObj.getStringWidth(text);
                height = 10;
                RenderUtil.drawString(text, x, y, textColor.getRGB());
            }
        };
    }
}
