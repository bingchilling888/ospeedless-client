package com.ospeedless;

import com.ospeedless.client.ClientManager;
import com.ospeedless.config.ConfigManager;
import com.ospeedless.gui.ClickGui;
import com.ospeedless.gui.HudEditor;
import com.ospeedless.gui.TitleScreen;
import com.ospeedless.module.ModuleManager;
import com.ospeedless.util.KeybindManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

@Mod(modid = OSpeedlessClient.MODID, name = OSpeedlessClient.NAME, version = OSpeedlessClient.VERSION, clientSideOnly = true)
public class OSpeedlessClient {

    public static final String MODID = "ospeedlessclient";
    public static final String NAME = "oSpeedless Client";
    public static final String VERSION = "1.0";
    public static final String THEME_COLOR = "§e";

    @Mod.Instance(MODID)
    public static OSpeedlessClient instance;

    private Minecraft mc;
    private ModuleManager moduleManager;
    private ConfigManager configManager;
    private ClientManager clientManager;
    private KeybindManager keybindManager;
    private ClickGui clickGui;
    private HudEditor hudEditor;

    private boolean watermarkEnabled = true;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        System.out.println("========================================");
        System.out.println("  oSpeedless Client v" + VERSION);
        System.out.println("  Premium Legit PvP Utility Client");
        System.out.println("  Minecraft 1.8.9 | Forge 11.15.1.2318");
        System.out.println("========================================");
        this.mc = Minecraft.getMinecraft();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        this.moduleManager = new ModuleManager();
        this.configManager = new ConfigManager();
        this.clientManager = new ClientManager();
        this.keybindManager = new KeybindManager();
        this.clickGui = new ClickGui();
        this.hudEditor = new HudEditor();

        this.moduleManager.init();
        this.configManager.load();
        this.keybindManager.init();

        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(this.moduleManager);
        MinecraftForge.EVENT_BUS.register(this.clientManager);
        MinecraftForge.EVENT_BUS.register(this.keybindManager);
        MinecraftForge.EVENT_BUS.register(this.hudEditor);

        System.out.println("[oSpeedless] All systems initialized successfully.");
        System.out.println("[oSpeedless] Modules loaded: " + this.moduleManager.getModules().size());
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("[oSpeedless] Saving configuration...");
            if (configManager != null) {
                configManager.save();
            }
            System.out.println("[oSpeedless] Shutdown complete. Goodbye!");
        }));
        System.out.println("[oSpeedless] Ready. Press Right Shift for Client Settings, H for HUD Editor.");
    }

    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent event) {
        if (event.gui instanceof GuiMainMenu) {
            event.gui = new TitleScreen();
        }
    }

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Post event) {
        if (event.type != RenderGameOverlayEvent.ElementType.ALL) return;
        if (mc.currentScreen != null && !(mc.currentScreen instanceof HudEditor)) return;
        if (mc.gameSettings.showDebugInfo) return;

        if (watermarkEnabled) {
            mc.fontRendererObj.drawStringWithShadow(
                    THEME_COLOR + "oSpeedless Client " + VERSION,
                    2, 2, 0xFFFFFF
            );
        }
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (mc.currentScreen != null) return;

        if (Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
            mc.displayGuiScreen(clickGui);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_H)) {
            mc.displayGuiScreen(hudEditor);
        }
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (mc.thePlayer == null) return;
        moduleManager.onTick();
    }

    public ModuleManager getModuleManager() {
        return moduleManager;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public ClientManager getClientManager() {
        return clientManager;
    }

    public KeybindManager getKeybindManager() {
        return keybindManager;
    }

    public ClickGui getClickGui() {
        return clickGui;
    }

    public HudEditor getHudEditor() {
        return hudEditor;
    }

    public Minecraft getMc() {
        return mc;
    }
}
