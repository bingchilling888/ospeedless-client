package com.ospeedless.module.impl.hud;

import com.ospeedless.hud.HudElement;
import com.ospeedless.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ComboCounter extends HudModule {

    private int combo = 0;
    private long lastHit = 0;
    private EntityLivingBase lastTarget;

    public ComboCounter() {
        super("Combo Counter", "Counts consecutive hits on the same target", "COMBO");
        element = new HudElement(this, 2, 260) {
            @Override
            public void render(ScaledResolution sr) {
                if (System.currentTimeMillis() - lastHit > 2000) {
                    combo = 0;
                }
                String label = showLabel.getValue() ? "Combo: " : "";
                String text = label + combo;
                width = Minecraft.getMinecraft().fontRendererObj.getStringWidth(text);
                height = 10;
                RenderUtil.drawString(text, x, y, textColor.getRGB());
            }
        };
    }

    @SubscribeEvent
    public void onAttack(AttackEntityEvent event) {
        if (!isEnabled()) return;
        if (event.target instanceof EntityLivingBase) {
            EntityLivingBase target = (EntityLivingBase) event.target;
            if (target == lastTarget && System.currentTimeMillis() - lastHit < 2000) {
                combo++;
            } else {
                combo = 1;
            }
            lastTarget = target;
            lastHit = System.currentTimeMillis();
        }
    }
}
