package com.ospeedless.module.impl.hud;

import com.ospeedless.OSpeedlessClient;
import com.ospeedless.hud.HudElement;
import com.ospeedless.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class Playtime extends HudModule {

    public Playtime() {
        super("Playtime", "Shows session playtime", "PT");
        element = new HudElement(this, 2, 392) {
            @Override
            public void render(ScaledResolution sr) {
                long secs = 0;
                if (OSpeedlessClient.instance != null && OSpeedlessClient.instance.getClientManager() != null) {
                    secs = OSpeedlessClient.instance.getClientManager().getPlaytimeSeconds();
                }
                long h = secs / 3600;
                long m = (secs % 3600) / 60;
                long s = secs % 60;
                String text = showLabel.getValue()
                        ? String.format("Playtime: %02d:%02d:%02d", h, m, s)
                        : String.format("%02d:%02d:%02d", h, m, s);
                width = Minecraft.getMinecraft().fontRendererObj.getStringWidth(text);
                height = 10;
                RenderUtil.drawString(text, x, y, textColor.getRGB());
            }
        };
    }
}
