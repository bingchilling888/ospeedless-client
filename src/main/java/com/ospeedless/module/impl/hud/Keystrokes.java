package com.ospeedless.module.impl.hud;

import com.ospeedless.hud.HudElement;
import com.ospeedless.setting.BooleanSetting;
import com.ospeedless.setting.ColorSetting;
import com.ospeedless.setting.NumberSetting;
import com.ospeedless.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class Keystrokes extends HudModule {

    private final BooleanSetting showMouse;
    private final BooleanSetting showSpace;
    private final NumberSetting boxSize;
    private final ColorSetting bgColor;
    private final ColorSetting pressedColor;
    private final ColorSetting keyTextColor;

    public Keystrokes() {
        super("Keystrokes", "Displays WASD and mouse key states", "K");
        showMouse = new BooleanSetting("Show Mouse", "Show LMB/RMB boxes", true);
        showSpace = new BooleanSetting("Show Space", "Show spacebar", true);
        boxSize = new NumberSetting("Box Size", "Size of key boxes", 18, 12, 30, 1);
        bgColor = new ColorSetting("Background", "Key background color", 0x80000000);
        pressedColor = new ColorSetting("Pressed Color", "Color when key is pressed", 0x80FFFF00);
        keyTextColor = new ColorSetting("Key Text", "Color of key labels", 0xFFFFFF);
        addSetting(showMouse);
        addSetting(showSpace);
        addSetting(boxSize);
        addSetting(bgColor);
        addSetting(pressedColor);
        addSetting(keyTextColor);

        element = new HudElement(this, 2, 50) {
            @Override
            public void render(ScaledResolution sr) {
                int size = boxSize.getIntValue();
                int gap = 2;
                Minecraft mc = Minecraft.getMinecraft();

                // W
                drawKey("W", x + size + gap, y, size, isKeyDown(mc.gameSettings.keyBindForward));
                // A S D
                drawKey("A", x, y + size + gap, size, isKeyDown(mc.gameSettings.keyBindLeft));
                drawKey("S", x + size + gap, y + size + gap, size, isKeyDown(mc.gameSettings.keyBindBack));
                drawKey("D", x + (size + gap) * 2, y + size + gap, size, isKeyDown(mc.gameSettings.keyBindRight));

                float curY = y + (size + gap) * 2;
                if (showSpace.getValue()) {
                    drawKey("SPACE", x, curY, size * 3 + gap * 2, isKeyDown(mc.gameSettings.keyBindJump));
                    curY += size + gap;
                }
                if (showMouse.getValue()) {
                    drawKey("LMB", x, curY, (size * 3 + gap * 2) / 2 - 1, Mouse.isButtonDown(0));
                    drawKey("RMB", x + (size * 3 + gap * 2) / 2 + 1, curY, (size * 3 + gap * 2) / 2 - 1, Mouse.isButtonDown(1));
                    curY += size + gap;
                }
                width = size * 3 + gap * 2;
                height = curY - y;
            }

            private void drawKey(String label, float kx, float ky, float kw, boolean pressed) {
                int color = pressed ? pressedColor.getValue() : bgColor.getValue();
                RenderUtil.drawRect(kx, ky, kw, boxSize.getIntValue(), color);
                float tx = kx + (kw - Minecraft.getMinecraft().fontRendererObj.getStringWidth(label)) / 2f;
                float ty = ky + (boxSize.getIntValue() - 8) / 2f;
                RenderUtil.drawString(label, tx, ty, keyTextColor.getRGB());
            }

            private boolean isKeyDown(KeyBinding bind) {
                try {
                    return Keyboard.isKeyDown(bind.getKeyCode());
                } catch (Exception e) {
                    return false;
                }
            }
        };
    }
}
