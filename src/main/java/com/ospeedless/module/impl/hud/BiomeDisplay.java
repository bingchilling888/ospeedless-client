package com.ospeedless.module.impl.hud;

import com.ospeedless.hud.HudElement;
import com.ospeedless.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.BlockPos;
import net.minecraft.world.biome.BiomeGenBase;

public class BiomeDisplay extends HudModule {

    public BiomeDisplay() {
        super("Biome Display", "Shows current biome name", "BIO");
        element = new HudElement(this, 2, 296) {
            @Override
            public void render(ScaledResolution sr) {
                Minecraft mc = Minecraft.getMinecraft();
                if (mc.thePlayer == null || mc.theWorld == null) return;
                BiomeGenBase biome = mc.theWorld.getBiomeGenForCoords(new BlockPos(mc.thePlayer));
                String name = biome != null ? biome.biomeName : "Unknown";
                String text = showLabel.getValue() ? "Biome: " + name : name;
                width = mc.fontRendererObj.getStringWidth(text);
                height = 10;
                RenderUtil.drawString(text, x, y, textColor.getRGB());
            }
        };
    }
}
