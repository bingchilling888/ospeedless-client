package com.ospeedless.module.impl.hud;

import com.ospeedless.hud.HudElement;
import com.ospeedless.setting.BooleanSetting;
import com.ospeedless.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class Coordinates extends HudModule {

    private final BooleanSetting commas;
    private final BooleanSetting labels;

    public Coordinates() {
        super("Coordinates", "Shows player XYZ position", "XYZ");
        commas = new BooleanSetting("Commas", "Use thousand separators", false);
        labels = new BooleanSetting("Labels", "Show X/Y/Z labels", true);
        addSetting(commas);
        addSetting(labels);

        element = new HudElement(this, 2, 120) {
            @Override
            public void render(ScaledResolution sr) {
                Minecraft mc = Minecraft.getMinecraft();
                if (mc.thePlayer == null) return;
                double px = mc.thePlayer.posX;
                double py = mc.thePlayer.posY;
                double pz = mc.thePlayer.posZ;
                String xs = format(px);
                String ys = format(py);
                String zs = format(pz);
                String text;
                if (labels.getValue()) {
                    text = "X: " + xs + " Y: " + ys + " Z: " + zs;
                } else {
                    text = xs + " " + ys + " " + zs;
                }
                width = mc.fontRendererObj.getStringWidth(text);
                height = 10;
                RenderUtil.drawString(text, x, y, textColor.getRGB());
            }

            private String format(double v) {
                int i = (int) Math.floor(v);
                if (commas.getValue()) {
                    return String.format("%,d", i);
                }
                return String.valueOf(i);
            }
        };
    }
}
