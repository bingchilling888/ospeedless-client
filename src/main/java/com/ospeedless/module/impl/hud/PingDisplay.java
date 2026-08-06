package com.ospeedless.module.impl.hud;

import com.ospeedless.hud.HudElement;
import com.ospeedless.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.network.NetworkPlayerInfo;

public class PingDisplay extends HudModule {

    public PingDisplay() {
        super("Ping Display", "Shows your current latency to the server", "P");
        element = new HudElement(this, 2, 26) {
            @Override
            public void render(ScaledResolution sr) {
                int ping = 0;
                try {
                    if (Minecraft.getMinecraft().getNetHandler() != null && Minecraft.getMinecraft().thePlayer != null) {
                        NetworkPlayerInfo info = Minecraft.getMinecraft().getNetHandler().getPlayerInfo(Minecraft.getMinecraft().thePlayer.getUniqueID());
                        if (info != null) {
                            ping = info.getResponseTime();
                        }
                    }
                } catch (Exception ignored) {}
                String label = showLabel.getValue() ? "Ping: " : "";
                String text = label + ping + "ms";
                width = Minecraft.getMinecraft().fontRendererObj.getStringWidth(text);
                height = 10;
                RenderUtil.drawString(text, x, y, textColor.getRGB());
            }
        };
    }
}
