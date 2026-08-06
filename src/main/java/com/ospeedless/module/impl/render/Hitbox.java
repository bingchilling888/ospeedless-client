package com.ospeedless.module.impl.render;

import com.ospeedless.module.Category;
import com.ospeedless.module.Module;
import com.ospeedless.setting.BooleanSetting;
import com.ospeedless.setting.ColorSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

public class Hitbox extends Module {

    private final ColorSetting color;
    private final BooleanSetting fill;
    private final BooleanSetting outline;

    public Hitbox() {
        super("Hitbox", "Renders entity hitboxes", Category.RENDER, "HB");
        color = new ColorSetting("Color", "Hitbox color", 0xFFFF00);
        fill = new BooleanSetting("Fill", "Fill the hitbox", false);
        outline = new BooleanSetting("Outline", "Draw outline", true);
        addSetting(color);
        addSetting(fill);
        addSetting(outline);
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.theWorld == null || mc.thePlayer == null) return;
        float partial = event.partialTicks;
        double px = mc.getRenderManager().viewerPosX;
        double py = mc.getRenderManager().viewerPosY;
        double pz = mc.getRenderManager().viewerPosZ;

        GlStateManager.pushMatrix();
        GlStateManager.disableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GL11.glLineWidth(1.5f);

        for (Entity entity : mc.theWorld.loadedEntityList) {
            if (entity == mc.thePlayer || !(entity instanceof EntityLivingBase)) continue;
            if (entity.isInvisible()) continue;

            double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partial - px;
            double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partial - py;
            double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partial - pz;

            AxisAlignedBB bb = entity.getEntityBoundingBox().offset(-entity.posX, -entity.posY, -entity.posZ).offset(x, y, z);

            float r = color.getRed() / 255f;
            float g = color.getGreen() / 255f;
            float b = color.getBlue() / 255f;

            if (fill.getValue()) {
                GlStateManager.color(r, g, b, 0.15f);
                // simple fill via expanded render
                RenderGlobal.drawOutlinedBoundingBox(bb, (int)(r*255), (int)(g*255), (int)(b*255), 40);
            }
            if (outline.getValue()) {
                GlStateManager.color(r, g, b, 1.0f);
                RenderGlobal.drawOutlinedBoundingBox(bb, (int)(r*255), (int)(g*255), (int)(b*255), 255);
            }
        }

        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }
}
