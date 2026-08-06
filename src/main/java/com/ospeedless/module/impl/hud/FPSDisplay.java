package com.ospeedless.module.impl.hud;

import com.ospeedless.hud.HudElement;
import com.ospeedless.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class FPSDisplay extends HudModule {

    public FPSDisplay() {
        super("FPS Display", "Shows current frames per second", "F");
        element = new HudElement(this, 2, 14) {
            @Override
            public void render(ScaledResolution sr) {
                String label = showLabel.getValue() ? "FPS: " : "";
                String text = label + Minecraft.getDebugFPS();
                width = Minecraft.getMinecraft().fontRendererObj.getStringWidth(text);
                height = 10;
                RenderUtil.drawString(text, x, y, textColor.getRGB());
            }
        };
    }
}
