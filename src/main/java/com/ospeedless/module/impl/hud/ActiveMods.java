package com.ospeedless.module.impl.hud;

import com.ospeedless.OSpeedlessClient;
import com.ospeedless.hud.HudElement;
import com.ospeedless.module.Module;
import com.ospeedless.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class ActiveMods extends HudModule {

    public ActiveMods() {
        super("Active Mods", "Lists currently enabled modules", "MODS");
        element = new HudElement(this, 2, 404) {
            @Override
            public void render(ScaledResolution sr) {
                if (OSpeedlessClient.instance == null) return;
                float curY = y;
                float maxW = 0;
                for (Module m : OSpeedlessClient.instance.getModuleManager().getModules()) {
                    if (m.isEnabled() && m != ActiveMods.this) {
                        String name = m.getName();
                        int w = Minecraft.getMinecraft().fontRendererObj.getStringWidth(name);
                        if (w > maxW) maxW = w;
                        RenderUtil.drawString(name, x, curY, textColor.getRGB());
                        curY += 10 * scale;
                    }
                }
                width = maxW;
                height = curY - y;
            }
        };
    }
}
