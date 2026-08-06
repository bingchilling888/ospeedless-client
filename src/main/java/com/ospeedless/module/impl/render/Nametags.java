package com.ospeedless.module.impl.render;

import com.ospeedless.module.Category;
import com.ospeedless.module.Module;
import com.ospeedless.setting.BooleanSetting;
import com.ospeedless.setting.ColorSetting;
import com.ospeedless.setting.NumberSetting;
import com.ospeedless.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

public class Nametags extends Module {

    private final NumberSetting scaleSetting;
    private final BooleanSetting background;
    private final BooleanSetting healthText;
    private final BooleanSetting distanceText;
    private final BooleanSetting armorPreview;
    private final BooleanSetting showLogo;
    private final ColorSetting textColor;
    private final ColorSetting bgColor;
    private final ColorSetting sameClientColor;

    public Nametags() {
        super("Nametags", "Enhanced player nametags with optional client logo", Category.RENDER, "NT");
        scaleSetting = new NumberSetting("Scale", "Nametag scale", 1.0, 0.5, 2.0, 0.1);
        background = new BooleanSetting("Background", "Draw background behind name", true);
        healthText = new BooleanSetting("Health", "Show health text", true);
        distanceText = new BooleanSetting("Distance", "Show distance", false);
        armorPreview = new BooleanSetting("Armor Preview", "Show armor items", false);
        showLogo = new BooleanSetting("Client Logo", "Show logo for same-client players", true);
        textColor = new ColorSetting("Text Color", "Name text color", 0xFFFFFF);
        bgColor = new ColorSetting("Background Color", "Background color", 0x80000000);
        sameClientColor = new ColorSetting("Same Client", "Highlight color for same client", 0xFFFF00);
        addSetting(scaleSetting);
        addSetting(background);
        addSetting(healthText);
        addSetting(distanceText);
        addSetting(armorPreview);
        addSetting(showLogo);
        addSetting(textColor);
        addSetting(bgColor);
        addSetting(sameClientColor);
    }

    @SubscribeEvent
    public void onRenderLiving(RenderLivingEvent.Specials.Pre event) {
        if (!(event.entity instanceof EntityPlayer)) return;
        event.setCanceled(true);

        EntityPlayer player = (EntityPlayer) event.entity;
        Minecraft mc = Minecraft.getMinecraft();
        if (player == mc.thePlayer) return;
        if (player.isInvisible()) return;

        RenderManager rm = mc.getRenderManager();
        double x = event.x;
        double y = event.y + player.height + 0.5;
        double z = event.z;

        float scale = scaleSetting.getFloatValue() * 0.02666667f;
        FontRenderer fr = mc.fontRendererObj;

        String name = player.getDisplayName().getFormattedText();
        boolean sameClient = isSameClient(player);
        if (sameClient && showLogo.getValue()) {
            name = "§e★ §r" + name;
        }

        StringBuilder extra = new StringBuilder();
        if (healthText.getValue()) {
            extra.append(" §c").append(String.format("%.1f", player.getHealth()));
        }
        if (distanceText.getValue()) {
            double dist = mc.thePlayer.getDistanceToEntity(player);
            extra.append(" §7[").append(String.format("%.1f", dist)).append("]");
        }
        String full = name + extra.toString();
        int width = fr.getStringWidth(full);

        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x, (float) y, (float) z);
        GL11.glNormal3f(0, 1, 0);
        GlStateManager.rotate(-rm.playerViewY, 0, 1, 0);
        GlStateManager.rotate(rm.playerViewX, 1, 0, 0);
        GlStateManager.scale(-scale, -scale, scale);
        GlStateManager.disableLighting();
        GlStateManager.depthMask(false);
        GlStateManager.disableDepth();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);

        if (background.getValue()) {
            int bg = sameClient ? (sameClientColor.getValue() & 0x00FFFFFF) | 0x60000000 : bgColor.getValue();
            RenderUtil.drawRect(-width / 2f - 2, -2, width + 4, 10, bg);
        }

        int col = sameClient ? sameClientColor.getRGB() : textColor.getRGB();
        fr.drawString(full, -width / 2, 0, col | 0xFF000000);

        if (armorPreview.getValue()) {
            float armorY = -20;
            float armorX = -32;
            RenderHelper.enableGUIStandardItemLighting();
            for (int i = 3; i >= 0; i--) {
                ItemStack stack = player.inventory.armorInventory[i];
                if (stack != null) {
                    mc.getRenderItem().renderItemAndEffectIntoGUI(stack, (int) armorX, (int) armorY);
                    armorX += 16;
                }
            }
            ItemStack held = player.getHeldItem();
            if (held != null) {
                mc.getRenderItem().renderItemAndEffectIntoGUI(held, (int) armorX, (int) armorY);
            }
            RenderHelper.disableStandardItemLighting();
        }

        GlStateManager.enableDepth();
        GlStateManager.depthMask(true);
        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
        GlStateManager.color(1, 1, 1, 1);
        GlStateManager.popMatrix();
    }

    /**
     * Same-client detection. Uses a simple display name / custom name prefix check
     * as a graceful fallback when no plugin-messaging channel is available.
     * On servers without oSpeedless support this simply returns false.
     */
    private boolean isSameClient(EntityPlayer player) {
        try {
            // Check for a custom name tag indicator that other oSpeedless clients could set
            // via scoreboard or display name. Fail gracefully.
            String dn = player.getDisplayName().getUnformattedText();
            if (dn != null && dn.contains("§e★")) {
                return true;
            }
            // Future: plugin message channel "OS|ID" handshake could be added here
        } catch (Exception ignored) {
        }
        return false;
    }
}
