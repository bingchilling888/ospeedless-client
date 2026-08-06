package com.ospeedless.module.impl.hud;

import com.ospeedless.hud.HudElement;
import com.ospeedless.setting.BooleanSetting;
import com.ospeedless.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.I18n;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

import java.util.Collection;

public class PotionEffects extends HudModule {

    private final BooleanSetting showDuration;
    private final BooleanSetting showAmplifier;

    public PotionEffects() {
        super("Potion Effects", "Displays active potion effects", "PE");
        showDuration = new BooleanSetting("Duration", "Show remaining duration", true);
        showAmplifier = new BooleanSetting("Amplifier", "Show amplifier level", true);
        addSetting(showDuration);
        addSetting(showAmplifier);

        element = new HudElement(this, 2, 220) {
            @Override
            public void render(ScaledResolution sr) {
                Minecraft mc = Minecraft.getMinecraft();
                if (mc.thePlayer == null) return;
                Collection<PotionEffect> effects = mc.thePlayer.getActivePotionEffects();
                float curY = y;
                float maxW = 0;
                for (PotionEffect effect : effects) {
                    Potion potion = Potion.potionTypes[effect.getPotionID()];
                    if (potion == null) continue;
                    String name = I18n.format(potion.getName());
                    if (showAmplifier.getValue() && effect.getAmplifier() > 0) {
                        name += " " + (effect.getAmplifier() + 1);
                    }
                    if (showDuration.getValue()) {
                        name += " " + Potion.getDurationString(effect);
                    }
                    int w = mc.fontRendererObj.getStringWidth(name);
                    if (w > maxW) maxW = w;
                    RenderUtil.drawString(name, x, curY, textColor.getRGB());
                    curY += 10 * scale;
                }
                width = maxW;
                height = curY - y;
            }
        };
    }
}
