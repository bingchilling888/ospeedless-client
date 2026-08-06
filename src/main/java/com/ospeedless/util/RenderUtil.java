package com.ospeedless.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

public class RenderUtil {

    private static final Minecraft mc = Minecraft.getMinecraft();

    public static void drawRect(float x, float y, float width, float height, int color) {
        float a = (color >> 24 & 255) / 255.0f;
        float r = (color >> 16 & 255) / 255.0f;
        float g = (color >> 8 & 255) / 255.0f;
        float b = (color & 255) / 255.0f;

        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(r, g, b, a);

        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer wr = tessellator.getWorldRenderer();
        wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
        wr.pos(x, y + height, 0).endVertex();
        wr.pos(x + width, y + height, 0).endVertex();
        wr.pos(x + width, y, 0).endVertex();
        wr.pos(x, y, 0).endVertex();
        tessellator.draw();

        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.color(1, 1, 1, 1);
    }

    public static void drawRoundedRect(float x, float y, float width, float height, float radius, int color) {
        drawRect(x + radius, y, width - radius * 2, height, color);
        drawRect(x, y + radius, radius, height - radius * 2, color);
        drawRect(x + width - radius, y + radius, radius, height - radius * 2, color);
        // Corners approximated with rects for 1.8.9 simplicity
        drawRect(x, y, radius, radius, color);
        drawRect(x + width - radius, y, radius, radius, color);
        drawRect(x, y + height - radius, radius, radius, color);
        drawRect(x + width - radius, y + height - radius, radius, radius, color);
    }

    public static void drawOutline(float x, float y, float width, float height, float thickness, int color) {
        drawRect(x, y, width, thickness, color);
        drawRect(x, y + height - thickness, width, thickness, color);
        drawRect(x, y, thickness, height, color);
        drawRect(x + width - thickness, y, thickness, height, color);
    }

    public static void drawHorizontalLine(float x, float y, float length, int color) {
        drawRect(x, y, length, 1, color);
    }

    public static void drawVerticalLine(float x, float y, float length, int color) {
        drawRect(x, y, 1, length, color);
    }

    public static void scissor(int x, int y, int width, int height) {
        ScaledResolution sr = new ScaledResolution(mc);
        int scale = sr.getScaleFactor();
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor(x * scale, (sr.getScaledHeight() - y - height) * scale, width * scale, height * scale);
    }

    public static void endScissor() {
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }

    public static int withAlpha(int color, int alpha) {
        return (alpha << 24) | (color & 0x00FFFFFF);
    }

    public static void drawCenteredString(String text, float x, float y, int color) {
        mc.fontRendererObj.drawStringWithShadow(text, x - mc.fontRendererObj.getStringWidth(text) / 2f, y, color);
    }

    public static void drawString(String text, float x, float y, int color) {
        mc.fontRendererObj.drawStringWithShadow(text, x, y, color);
    }
}
