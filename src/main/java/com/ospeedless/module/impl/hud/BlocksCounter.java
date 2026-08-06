package com.ospeedless.module.impl.hud;

import com.ospeedless.hud.HudElement;
import com.ospeedless.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class BlocksCounter extends HudModule {

    public BlocksCounter() {
        super("Blocks Counter", "Counts placeable blocks in inventory", "BLK");
        element = new HudElement(this, 2, 428) {
            @Override
            public void render(ScaledResolution sr) {
                Minecraft mc = Minecraft.getMinecraft();
                if (mc.thePlayer == null) return;
                int count = 0;
                for (ItemStack stack : mc.thePlayer.inventory.mainInventory) {
                    if (stack != null && stack.getItem() instanceof ItemBlock) {
                        count += stack.stackSize;
                    }
                }
                String text = showLabel.getValue() ? "Blocks: " + count : String.valueOf(count);
                width = mc.fontRendererObj.getStringWidth(text);
                height = 10;
                RenderUtil.drawString(text, x, y, textColor.getRGB());
            }
        };
    }
}
