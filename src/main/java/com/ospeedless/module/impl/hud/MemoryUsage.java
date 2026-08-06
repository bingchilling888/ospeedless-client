package com.ospeedless.module.impl.hud;

import com.ospeedless.hud.HudElement;
import com.ospeedless.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class MemoryUsage extends HudModule {

    public MemoryUsage() {
        super("Memory Usage", "Shows JVM memory usage", "MEM");
        element = new HudElement(this, 2, 320) {
            @Override
            public void render(ScaledResolution sr) {
                Runtime rt = Runtime.getRuntime();
                long used = (rt.totalMemory() - rt.freeMemory()) / (1024 * 1024);
                long max = rt.maxMemory() / (1024 * 1024);
                String text = showLabel.getValue() ? "Mem: " + used + "/" + max + "MB" : used + "/" + max + "MB";
                width = Minecraft.getMinecraft().fontRendererObj.getStringWidth(text);
                height = 10;
                RenderUtil.drawString(text, x, y, textColor.getRGB());
            }
        };
    }
}
