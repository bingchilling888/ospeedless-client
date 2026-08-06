package com.ospeedless.module.impl.hud;

import com.ospeedless.hud.HudElement;
import com.ospeedless.setting.BooleanSetting;
import com.ospeedless.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;

public class ArmorStatus extends HudModule {

    private final BooleanSetting durability;

    public ArmorStatus() {
        super("Armor Status", "Shows equipped armor and durability", "A");
        durability = new BooleanSetting("Durability", "Show durability text", true);
        addSetting(durability);

        element = new HudElement(this, 2, 140) {
            @Override
            public void render(ScaledResolution sr) {
                Minecraft mc = Minecraft.getMinecraft();
                if (mc.thePlayer == null) return;
                float curY = y;
                int itemSize = 16;
                for (int i = 3; i >= 0; i--) {
                    ItemStack stack = mc.thePlayer.inventory.armorInventory[i];
                    if (stack != null) {
                        GlStateManager.pushMatrix();
                        GlStateManager.translate(x, curY, 0);
                        GlStateManager.scale(scale, scale, 1);
                        RenderHelper.enableGUIStandardItemLighting();
                        mc.getRenderItem().renderItemAndEffectIntoGUI(stack, 0, 0);
                        mc.getRenderItem().renderItemOverlayIntoGUI(mc.fontRendererObj, stack, 0, 0, null);
                        RenderHelper.disableStandardItemLighting();
                        GlStateManager.popMatrix();
                        if (durability.getValue() && stack.isItemStackDamageable()) {
                            int max = stack.getMaxDamage();
                            int dmg = max - stack.getItemDamage();
                            String dText = dmg + "/" + max;
                            RenderUtil.drawString(dText, x + 18 * scale, curY + 4, textColor.getRGB());
                        }
                        curY += (itemSize + 2) * scale;
                    }
                }
                width = 60;
                height = curY - y;
            }
        };
    }
}
