package com.ospeedless.module.impl.hud;

import com.ospeedless.hud.HudElement;
import com.ospeedless.setting.BooleanSetting;
import com.ospeedless.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;
import java.util.List;

public class CPSDisplay extends HudModule {

    private final BooleanSetting leftCPS;
    private final BooleanSetting rightCPS;
    private final List<Long> leftClicks = new ArrayList<Long>();
    private final List<Long> rightClicks = new ArrayList<Long>();

    public CPSDisplay() {
        super("CPS Display", "Shows clicks per second", "C");
        leftCPS = new BooleanSetting("Left CPS", "Show left click CPS", true);
        rightCPS = new BooleanSetting("Right CPS", "Show right click CPS", true);
        addSetting(leftCPS);
        addSetting(rightCPS);

        element = new HudElement(this, 2, 38) {
            @Override
            public void render(ScaledResolution sr) {
                long now = System.currentTimeMillis();
                leftClicks.removeIf(t -> now - t > 1000);
                rightClicks.removeIf(t -> now - t > 1000);

                StringBuilder sb = new StringBuilder();
                if (leftCPS.getValue()) {
                    if (showLabel.getValue()) sb.append("L-CPS: ");
                    sb.append(leftClicks.size());
                }
                if (leftCPS.getValue() && rightCPS.getValue()) sb.append("  ");
                if (rightCPS.getValue()) {
                    if (showLabel.getValue()) sb.append("R-CPS: ");
                    sb.append(rightClicks.size());
                }
                String text = sb.toString();
                width = Minecraft.getMinecraft().fontRendererObj.getStringWidth(text);
                height = 10;
                RenderUtil.drawString(text, x, y, textColor.getRGB());
            }
        };
    }

    @SubscribeEvent
    public void onMouse(MouseEvent event) {
        if (!isEnabled()) return;
        if (event.buttonstate) {
            if (event.button == 0) leftClicks.add(System.currentTimeMillis());
            if (event.button == 1) rightClicks.add(System.currentTimeMillis());
        }
    }

    @Override
    public void onTick() {
        // also poll for clicks outside of events if needed
        if (Mouse.isButtonDown(0) && leftClicks.isEmpty()) {
            // handled by event
        }
    }
}
