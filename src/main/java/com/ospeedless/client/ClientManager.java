package com.ospeedless.client;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class ClientManager {

    private final Minecraft mc = Minecraft.getMinecraft();

    // Client settings
    public boolean blurBackground = true;
    public boolean disableHotbarScroll = false;
    public boolean capeDisplay = true;
    public boolean borderlessFullscreen = false;
    public boolean hudSnapping = true;
    public boolean hudGuides = true;
    public String performancePreset = "Balanced";
    public float panelOpacity = 0.85f;
    public boolean yellowTheme = true;

    private long startTime = System.currentTimeMillis();

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        // Apply performance presets if needed
    }

    public long getPlaytimeSeconds() {
        return (System.currentTimeMillis() - startTime) / 1000L;
    }

    public void resetPlaytime() {
        startTime = System.currentTimeMillis();
    }
}
