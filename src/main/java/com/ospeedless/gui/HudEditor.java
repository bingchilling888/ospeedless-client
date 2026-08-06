package com.ospeedless.gui;

import com.ospeedless.OSpeedlessClient;
import com.ospeedless.client.ClientManager;
import com.ospeedless.hud.HudElement;
import com.ospeedless.module.Module;
import com.ospeedless.module.impl.hud.HudModule;
import com.ospeedless.util.RenderUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HudEditor extends GuiScreen {

    private final List<HudElement> elements = new ArrayList<HudElement>();
    private HudElement dragging = null;
    private static final int SNAP = 5;
    private static final int YELLOW = 0xFFFFD700;

    @Override
    public void initGui() {
        elements.clear();
        for (Module m : OSpeedlessClient.instance.getModuleManager().getModules()) {
            if (m instanceof HudModule && m.isEnabled()) {
                HudElement el = ((HudModule) m).getElement();
                if (el != null) elements.add(el);
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawRect(0, 0, width, height, 0x60000000);

        ClientManager cm = OSpeedlessClient.instance.getClientManager();
        ScaledResolution sr = new ScaledResolution(mc);

        // Guide lines
        if (cm.hudGuides) {
            int midX = width / 2;
            int midY = height / 2;
            RenderUtil.drawVerticalLine(midX, 0, height, 0x40FFFF00);
            RenderUtil.drawHorizontalLine(0, midY, width, 0x40FFFF00);
        }

        // Render all enabled HUD elements
        for (HudElement el : elements) {
            el.renderPreview(sr);
            boolean hovered = el.isHovered(mouseX, mouseY) || el == dragging;
            if (hovered) {
                RenderUtil.drawOutline(el.getX() - 1, el.getY() - 1,
                        el.getWidth() * el.getScale() + 2,
                        el.getHeight() * el.getScale() + 2,
                        1, YELLOW);
            } else {
                RenderUtil.drawOutline(el.getX(), el.getY(),
                        el.getWidth() * el.getScale(),
                        el.getHeight() * el.getScale(),
                        1, 0x60FFFFFF);
            }
        }

        // Header
        mc.fontRendererObj.drawStringWithShadow("§eoSpeedless HUD Editor", 4, 4, 0xFFFFFF);
        mc.fontRendererObj.drawStringWithShadow("§7Drag elements · ESC/H to close · Snapping: " + (cm.hudSnapping ? "ON" : "OFF"), 4, 16, 0xAAAAAA);

        if (dragging != null) {
            dragging.updateDrag(mouseX, mouseY, cm.hudSnapping, SNAP);
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (mouseButton == 0) {
            for (int i = elements.size() - 1; i >= 0; i--) {
                HudElement el = elements.get(i);
                if (el.isHovered(mouseX, mouseY)) {
                    dragging = el;
                    el.startDrag(mouseX, mouseY);
                    return;
                }
            }
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        if (dragging != null) {
            dragging.stopDrag();
            OSpeedlessClient.instance.getConfigManager().save();
            dragging = null;
        }
        super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == Keyboard.KEY_ESCAPE || keyCode == Keyboard.KEY_H) {
            if (dragging != null) {
                dragging.stopDrag();
                dragging = null;
            }
            OSpeedlessClient.instance.getConfigManager().save();
            mc.displayGuiScreen(null);
            return;
        }
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
