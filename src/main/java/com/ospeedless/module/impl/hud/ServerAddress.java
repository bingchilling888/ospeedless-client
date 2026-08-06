package com.ospeedless.module.impl.hud;

import com.ospeedless.hud.HudElement;
import com.ospeedless.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.ServerData;

public class ServerAddress extends HudModule {

    public ServerAddress() {
        super("Server Address", "Shows current server IP", "IP");
        element = new HudElement(this, 2, 368) {
            @Override
            public void render(ScaledResolution sr) {
                Minecraft mc = Minecraft.getMinecraft();
                String addr = "Singleplayer";
                ServerData data = mc.getCurrentServerData();
                if (data != null) {
                    addr = data.serverIP;
                }
                String text = showLabel.getValue() ? "Server: " + addr : addr;
                width = mc.fontRendererObj.getStringWidth(text);
                height = 10;
                RenderUtil.drawString(text, x, y, textColor.getRGB());
            }
        };
    }
}
