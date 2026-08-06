package com.ospeedless.module.impl.hud;

import com.ospeedless.hud.HudElement;
import com.ospeedless.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class PlayerCount extends HudModule {

    public PlayerCount() {
        super("Player Count", "Shows players on the server", "PLY");
        element = new HudElement(this, 2, 380) {
            @Override
            public void render(ScaledResolution sr) {
                Minecraft mc = Minecraft.getMinecraft();
                int count = 1;
                if (mc.getNetHandler() != null) {
                    count = mc.getNetHandler().getPlayerInfoMap().size();
                }
                String text = showLabel.getValue() ? "Players: " + count : String.valueOf(count);
                width = mc.fontRendererObj.getStringWidth(text);
                height = 10;
                RenderUtil.drawString(text, x, y, textColor.getRGB());
            }
        };
    }
}
