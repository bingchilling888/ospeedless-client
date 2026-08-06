package com.ospeedless.gui;

import com.ospeedless.OSpeedlessClient;
import com.ospeedless.client.ClientManager;
import com.ospeedless.module.Category;
import com.ospeedless.module.Module;
import com.ospeedless.setting.*;
import com.ospeedless.util.KeybindManager;
import com.ospeedless.util.RenderUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.List;

public class ClickGui extends GuiScreen {

    private enum Tab { MODS, SETTINGS }
    private Tab currentTab = Tab.MODS;
    private Category selectedCategory = Category.HUD;
    private Module selectedModule = null;
    private int scrollOffset = 0;
    private int settingsScroll = 0;
    private boolean bindingKey = false;
    private float openAnim = 0f;
    private long openTime;

    private static final int PANEL_W = 420;
    private static final int PANEL_H = 280;
    private static final int YELLOW = 0xFFFFD700;
    private static final int DARK = 0xE0101018;
    private static final int DARKER = 0xF0080810;
    private static final int ACCENT = 0xFFFFCC00;

    public ClickGui() {
        openTime = System.currentTimeMillis();
    }

    @Override
    public void initGui() {
        openTime = System.currentTimeMillis();
        openAnim = 0f;
        bindingKey = false;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        openAnim = Math.min(1f, (System.currentTimeMillis() - openTime) / 200f);
        float anim = easeOut(openAnim);

        ScaledResolution sr = new ScaledResolution(mc);
        int cx = sr.getScaledWidth() / 2;
        int cy = sr.getScaledHeight() / 2;
        int px = (int) (cx - PANEL_W / 2 * anim);
        int py = (int) (cy - PANEL_H / 2 * anim);
        int pw = (int) (PANEL_W * anim);
        int ph = (int) (PANEL_H * anim);

        // Dim background
        drawRect(0, 0, width, height, 0x90000000);

        // Main panel
        RenderUtil.drawRect(px, py, pw, ph, DARK);
        RenderUtil.drawOutline(px, py, pw, ph, 1, ACCENT);

        // Header
        RenderUtil.drawRect(px, py, pw, 22, DARKER);
        mc.fontRendererObj.drawStringWithShadow("§eoSpeedless Client", px + 8, py + 7, 0xFFFFFF);
        mc.fontRendererObj.drawStringWithShadow("§7v1.0", px + pw - 30, py + 7, 0xAAAAAA);

        // Tabs
        int tabY = py + 24;
        drawTab("Mods", px + 8, tabY, currentTab == Tab.MODS, mouseX, mouseY);
        drawTab("Settings", px + 70, tabY, currentTab == Tab.SETTINGS, mouseX, mouseY);

        // Edit GUI button
        int editX = px + pw - 70;
        boolean editHover = mouseX >= editX && mouseX <= editX + 60 && mouseY >= tabY && mouseY <= tabY + 14;
        RenderUtil.drawRect(editX, tabY, 60, 14, editHover ? 0xFF333340 : 0xFF1A1A22);
        mc.fontRendererObj.drawString("Edit GUI", editX + 8, tabY + 3, ACCENT);

        if (currentTab == Tab.MODS) {
            drawModsTab(px, py, pw, ph, mouseX, mouseY);
        } else {
            drawSettingsTab(px, py, pw, ph, mouseX, mouseY);
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private void drawTab(String name, int x, int y, boolean selected, int mx, int my) {
        boolean hover = mx >= x && mx <= x + 50 && my >= y && my <= y + 14;
        int col = selected ? ACCENT : (hover ? 0xFFFFFF : 0xAAAAAA);
        mc.fontRendererObj.drawStringWithShadow(name, x, y + 3, col);
        if (selected) {
            RenderUtil.drawRect(x, y + 13, mc.fontRendererObj.getStringWidth(name), 1, ACCENT);
        }
    }

    private void drawModsTab(int px, int py, int pw, int ph, int mouseX, int mouseY) {
        // Category bar
        int catY = py + 42;
        int catX = px + 8;
        for (Category cat : Category.values()) {
            boolean sel = cat == selectedCategory;
            boolean hover = mouseX >= catX && mouseX <= catX + 55 && mouseY >= catY && mouseY <= catY + 12;
            int col = sel ? ACCENT : (hover ? 0xFFFFFF : 0x888888);
            mc.fontRendererObj.drawString(cat.getName(), catX, catY + 2, col);
            if (sel) RenderUtil.drawRect(catX, catY + 11, mc.fontRendererObj.getStringWidth(cat.getName()), 1, ACCENT);
            catX += 60;
        }

        // Module list (left)
        int listX = px + 8;
        int listY = py + 58;
        int listW = 160;
        int listH = ph - 70;
        RenderUtil.drawRect(listX, listY, listW, listH, 0xC0080812);

        List<Module> mods = OSpeedlessClient.instance.getModuleManager().getModulesByCategory(selectedCategory);
        int itemH = 16;
        int maxVisible = listH / itemH;
        int maxScroll = Math.max(0, mods.size() - maxVisible);
        scrollOffset = Math.max(0, Math.min(scrollOffset, maxScroll));

        RenderUtil.scissor(listX, listY, listW, listH);
        for (int i = 0; i < mods.size(); i++) {
            int iy = listY + (i - scrollOffset) * itemH;
            if (iy + itemH < listY || iy > listY + listH) continue;
            Module m = mods.get(i);
            boolean sel = m == selectedModule;
            boolean hover = mouseX >= listX && mouseX <= listX + listW && mouseY >= iy && mouseY <= iy + itemH;
            if (sel) RenderUtil.drawRect(listX, iy, listW, itemH, 0x40FFD700);
            else if (hover) RenderUtil.drawRect(listX, iy, listW, itemH, 0x20FFFFFF);

            String state = m.isEnabled() ? "§a●" : "§7○";
            mc.fontRendererObj.drawString(state + " §f" + m.getName(), listX + 4, iy + 4, 0xFFFFFF);
        }
        RenderUtil.endScissor();

        // Settings panel (right)
        int setX = listX + listW + 8;
        int setW = pw - listW - 24;
        RenderUtil.drawRect(setX, listY, setW, listH, 0xC0080812);

        if (selectedModule != null) {
            mc.fontRendererObj.drawStringWithShadow("§e" + selectedModule.getName(), setX + 6, listY + 4, 0xFFFFFF);
            mc.fontRendererObj.drawString("§7" + selectedModule.getDescription(), setX + 6, listY + 16, 0xAAAAAA);

            // Keybind
            String keyName = bindingKey ? "§ePress a key..." : "§7Key: §f" + KeybindManager.getKeyName(selectedModule.getKeybind());
            mc.fontRendererObj.drawString(keyName, setX + 6, listY + 30, 0xFFFFFF);

            int sy = listY + 46;
            for (Setting<?> s : selectedModule.getSettings()) {
                String line = "§7" + s.getName() + ": §f" + s.getDisplayValue();
                mc.fontRendererObj.drawString(line, setX + 6, sy, 0xFFFFFF);
                sy += 12;
                if (sy > listY + listH - 10) break;
            }
        } else {
            mc.fontRendererObj.drawString("§7Select a module", setX + 6, listY + 10, 0xAAAAAA);
        }
    }

    private void drawSettingsTab(int px, int py, int pw, int ph, int mouseX, int mouseY) {
        ClientManager cm = OSpeedlessClient.instance.getClientManager();
        int sx = px + 16;
        int sy = py + 50;
        int lineH = 16;

        String[] labels = {
                "Blur Background: " + (cm.blurBackground ? "§aON" : "§cOFF"),
                "Disable Hotbar Scroll: " + (cm.disableHotbarScroll ? "§aON" : "§cOFF"),
                "Cape Display: " + (cm.capeDisplay ? "§aON" : "§cOFF"),
                "Borderless Fullscreen: " + (cm.borderlessFullscreen ? "§aON" : "§cOFF"),
                "HUD Snapping: " + (cm.hudSnapping ? "§aON" : "§cOFF"),
                "HUD Guides: " + (cm.hudGuides ? "§aON" : "§cOFF"),
                "Performance Preset: §e" + cm.performancePreset,
                "Panel Opacity: §e" + String.format("%.0f%%", cm.panelOpacity * 100),
                "Yellow Theme: " + (cm.yellowTheme ? "§aON" : "§cOFF")
        };
        String[] descs = {
                "Blur behind GUI panels",
                "Prevent accidental hotbar scroll",
                "Show player capes",
                "Borderless windowed mode",
                "Snap HUD elements to grid",
                "Show guide lines in HUD editor",
                "Global performance profile",
                "Opacity of GUI panels",
                "Use yellow accent color"
        };

        for (int i = 0; i < labels.length; i++) {
            boolean hover = mouseX >= sx && mouseX <= sx + 300 && mouseY >= sy && mouseY <= sy + lineH;
            if (hover) RenderUtil.drawRect(sx - 2, sy - 1, 300, lineH, 0x20FFD700);
            mc.fontRendererObj.drawString(labels[i], sx, sy + 3, 0xFFFFFF);
            mc.fontRendererObj.drawString("§8" + descs[i], sx + 200, sy + 3, 0x666666);
            sy += lineH;
        }
    }

    private float easeOut(float t) {
        return 1f - (1f - t) * (1f - t);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        ScaledResolution sr = new ScaledResolution(mc);
        int cx = sr.getScaledWidth() / 2;
        int cy = sr.getScaledHeight() / 2;
        int px = cx - PANEL_W / 2;
        int py = cy - PANEL_H / 2;

        // Tabs
        if (mouseY >= py + 24 && mouseY <= py + 38) {
            if (mouseX >= px + 8 && mouseX <= px + 50) currentTab = Tab.MODS;
            if (mouseX >= px + 70 && mouseX <= px + 120) currentTab = Tab.SETTINGS;
            if (mouseX >= px + PANEL_W - 70 && mouseX <= px + PANEL_W - 10) {
                mc.displayGuiScreen(OSpeedlessClient.instance.getHudEditor());
                return;
            }
        }

        if (currentTab == Tab.MODS) {
            // Categories
            int catX = px + 8;
            int catY = py + 42;
            for (Category cat : Category.values()) {
                if (mouseX >= catX && mouseX <= catX + 55 && mouseY >= catY && mouseY <= catY + 12) {
                    selectedCategory = cat;
                    selectedModule = null;
                    scrollOffset = 0;
                    return;
                }
                catX += 60;
            }

            // Module list
            int listX = px + 8;
            int listY = py + 58;
            int listW = 160;
            int listH = PANEL_H - 70;
            List<Module> mods = OSpeedlessClient.instance.getModuleManager().getModulesByCategory(selectedCategory);
            int itemH = 16;
            for (int i = 0; i < mods.size(); i++) {
                int iy = listY + (i - scrollOffset) * itemH;
                if (mouseX >= listX && mouseX <= listX + listW && mouseY >= iy && mouseY <= iy + itemH) {
                    Module m = mods.get(i);
                    if (mouseButton == 0) {
                        selectedModule = m;
                    } else if (mouseButton == 1) {
                        m.toggle();
                        OSpeedlessClient.instance.getConfigManager().save();
                    }
                    return;
                }
            }

            // Keybind click
            if (selectedModule != null) {
                int setX = listX + listW + 8;
                if (mouseX >= setX + 6 && mouseX <= setX + 120 && mouseY >= listY + 28 && mouseY <= listY + 40) {
                    bindingKey = true;
                    return;
                }
                // Settings click
                int sy = listY + 46;
                for (Setting<?> s : selectedModule.getSettings()) {
                    if (mouseX >= setX + 6 && mouseX <= setX + 180 && mouseY >= sy && mouseY <= sy + 12) {
                        handleSettingClick(s, mouseButton);
                        OSpeedlessClient.instance.getConfigManager().save();
                        return;
                    }
                    sy += 12;
                }
            }
        } else {
            // Settings tab clicks
            ClientManager cm = OSpeedlessClient.instance.getClientManager();
            int sx = px + 16;
            int sy = py + 50;
            int lineH = 16;
            for (int i = 0; i < 9; i++) {
                if (mouseX >= sx && mouseX <= sx + 300 && mouseY >= sy && mouseY <= sy + lineH) {
                    switch (i) {
                        case 0: cm.blurBackground = !cm.blurBackground; break;
                        case 1: cm.disableHotbarScroll = !cm.disableHotbarScroll; break;
                        case 2: cm.capeDisplay = !cm.capeDisplay; break;
                        case 3: cm.borderlessFullscreen = !cm.borderlessFullscreen; break;
                        case 4: cm.hudSnapping = !cm.hudSnapping; break;
                        case 5: cm.hudGuides = !cm.hudGuides; break;
                        case 6:
                            if (cm.performancePreset.equals("Low")) cm.performancePreset = "Balanced";
                            else if (cm.performancePreset.equals("Balanced")) cm.performancePreset = "High";
                            else cm.performancePreset = "Low";
                            break;
                        case 7:
                            cm.panelOpacity = cm.panelOpacity >= 0.95f ? 0.5f : cm.panelOpacity + 0.1f;
                            break;
                        case 8: cm.yellowTheme = !cm.yellowTheme; break;
                    }
                    OSpeedlessClient.instance.getConfigManager().save();
                    return;
                }
                sy += lineH;
            }
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    private void handleSettingClick(Setting<?> s, int button) {
        if (s instanceof BooleanSetting) {
            ((BooleanSetting) s).toggle();
        } else if (s instanceof ModeSetting) {
            ((ModeSetting) s).cycle();
        } else if (s instanceof NumberSetting) {
            NumberSetting ns = (NumberSetting) s;
            double step = ns.getIncrement();
            if (button == 0) ns.setValueClamped(ns.getValue() + step);
            else ns.setValueClamped(ns.getValue() - step);
        } else if (s instanceof ColorSetting) {
            // Cycle through some preset colors
            ColorSetting cs = (ColorSetting) s;
            int[] presets = {0xFFFFFF, 0xFFFF00, 0x00FF00, 0x00FFFF, 0xFF00FF, 0xFF5555, 0x5555FF};
            int cur = cs.getRGB();
            int next = presets[0];
            for (int i = 0; i < presets.length; i++) {
                if (presets[i] == cur) {
                    next = presets[(i + 1) % presets.length];
                    break;
                }
            }
            cs.setValue(0xFF000000 | next);
        }
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        int wheel = Mouse.getEventDWheel();
        if (wheel != 0) {
            if (wheel > 0) scrollOffset = Math.max(0, scrollOffset - 1);
            else scrollOffset++;
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (bindingKey && selectedModule != null) {
            if (keyCode == Keyboard.KEY_ESCAPE || keyCode == Keyboard.KEY_DELETE) {
                selectedModule.setKeybind(Keyboard.KEY_NONE);
            } else {
                selectedModule.setKeybind(keyCode);
            }
            bindingKey = false;
            OSpeedlessClient.instance.getConfigManager().save();
            return;
        }
        if (keyCode == Keyboard.KEY_ESCAPE || keyCode == Keyboard.KEY_RSHIFT) {
            mc.displayGuiScreen(null);
            OSpeedlessClient.instance.getConfigManager().save();
            return;
        }
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
