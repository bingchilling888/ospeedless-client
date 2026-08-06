package com.ospeedless.gui;

import com.ospeedless.OSpeedlessClient;
import com.ospeedless.util.RenderUtil;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.io.IOException;

public class TitleScreen extends GuiScreen {

    private static final ResourceLocation LOGO = new ResourceLocation("ospeedlessclient", "logo.png");
    private static final int ACCENT = 0xFFFFD700;
    private long openTime;

    @Override
    public void initGui() {
        openTime = System.currentTimeMillis();
        buttonList.clear();
        int cx = width / 2;
        int by = height / 2 + 10;
        int bw = 160;
        int bh = 22;
        int gap = 6;

        buttonList.add(new GuiButton(0, cx - bw / 2, by, bw, bh, "Singleplayer"));
        buttonList.add(new GuiButton(1, cx - bw / 2, by + (bh + gap), bw, bh, "Multiplayer"));
        buttonList.add(new GuiButton(2, cx - bw / 2, by + (bh + gap) * 2, bw, bh, "Client Settings"));
        buttonList.add(new GuiButton(3, cx - bw / 2, by + (bh + gap) * 3, bw, bh, "Options"));
        buttonList.add(new GuiButton(4, cx - bw / 2, by + (bh + gap) * 4, bw, bh, "Quit"));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        // Gradient background
        drawGradientRect(0, 0, width, height, 0xFF0A0A12, 0xFF1A1A28);

        // Subtle accent lines
        RenderUtil.drawRect(0, 0, width, 2, ACCENT);
        RenderUtil.drawRect(0, height - 2, width, 2, ACCENT);

        // Logo
        GlStateManager.color(1, 1, 1, 1);
        try {
            mc.getTextureManager().bindTexture(LOGO);
            int logoSize = 48;
            int lx = width / 2 - logoSize / 2;
            int ly = height / 2 - 90;
            GlStateManager.enableBlend();
            drawModalRectWithCustomSizedTexture(lx, ly, 0, 0, logoSize, logoSize, logoSize, logoSize);
            GlStateManager.disableBlend();
        } catch (Exception e) {
            // Fallback text logo
            drawCenteredString(fontRendererObj, "§eoS", width / 2, height / 2 - 70, ACCENT);
        }

        // Title
        String title = "§eoSpeedless Client";
        fontRendererObj.drawStringWithShadow(title, width / 2 - fontRendererObj.getStringWidth(title) / 2, height / 2 - 36, 0xFFFFFF);
        fontRendererObj.drawStringWithShadow("§7v1.0  ·  Premium Legit Utility", width / 2 - fontRendererObj.getStringWidth("v1.0  ·  Premium Legit Utility") / 2, height / 2 - 24, 0xAAAAAA);

        // Custom button rendering
        for (GuiButton btn : buttonList) {
            drawCustomButton(btn, mouseX, mouseY);
        }

        // Footer
        fontRendererObj.drawStringWithShadow("§8Right Shift · Client Settings   |   H · HUD Editor", 4, height - 12, 0x666666);
        fontRendererObj.drawStringWithShadow("§8Minecraft 1.8.9", width - fontRendererObj.getStringWidth("Minecraft 1.8.9") - 4, height - 12, 0x666666);
    }

    private void drawCustomButton(GuiButton btn, int mouseX, int mouseY) {
        boolean hover = mouseX >= btn.xPosition && mouseX <= btn.xPosition + btn.width
                && mouseY >= btn.yPosition && mouseY <= btn.yPosition + btn.height;
        int bg = hover ? 0xE0282820 : 0xC015151C;
        int border = hover ? ACCENT : 0x60FFD700;
        RenderUtil.drawRect(btn.xPosition, btn.yPosition, btn.width, btn.height, bg);
        RenderUtil.drawOutline(btn.xPosition, btn.yPosition, btn.width, btn.height, 1, border);
        int textCol = hover ? ACCENT : 0xFFFFFF;
        fontRendererObj.drawStringWithShadow(btn.displayString,
                btn.xPosition + (btn.width - fontRendererObj.getStringWidth(btn.displayString)) / 2,
                btn.yPosition + (btn.height - 8) / 2, textCol);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        switch (button.id) {
            case 0:
                mc.displayGuiScreen(new GuiSelectWorld(this));
                break;
            case 1:
                mc.displayGuiScreen(new GuiMultiplayer(this));
                break;
            case 2:
                mc.displayGuiScreen(OSpeedlessClient.instance.getClickGui());
                break;
            case 3:
                mc.displayGuiScreen(new GuiOptions(this, mc.gameSettings));
                break;
            case 4:
                mc.shutdown();
                break;
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        // Prevent ESC from closing title
    }
}
