package com.ospeedless.config;

import com.google.gson.*;
import com.ospeedless.OSpeedlessClient;
import com.ospeedless.client.ClientManager;
import com.ospeedless.hud.HudElement;
import com.ospeedless.module.Module;
import com.ospeedless.module.impl.hud.HudModule;
import com.ospeedless.setting.*;
import net.minecraft.client.Minecraft;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class ConfigManager {

    private final File configDir;
    private final File configFile;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public ConfigManager() {
        File mcDir = Minecraft.getMinecraft().mcDataDir;
        configDir = new File(mcDir, "ospeedlessclient");
        if (!configDir.exists()) {
            configDir.mkdirs();
        }
        configFile = new File(configDir, "config.json");
    }

    public void load() {
        if (!configFile.exists()) {
            save();
            return;
        }
        try (Reader reader = new InputStreamReader(new FileInputStream(configFile), StandardCharsets.UTF_8)) {
            JsonObject root = new JsonParser().parse(reader).getAsJsonObject();

            // Modules
            if (root.has("modules")) {
                JsonObject modules = root.getAsJsonObject("modules");
                for (Module m : OSpeedlessClient.instance.getModuleManager().getModules()) {
                    if (modules.has(m.getName())) {
                        JsonObject modObj = modules.getAsJsonObject(m.getName());
                        if (modObj.has("enabled")) {
                            m.setEnabled(modObj.get("enabled").getAsBoolean());
                        }
                        if (modObj.has("keybind")) {
                            m.setKeybind(modObj.get("keybind").getAsInt());
                        }
                        if (modObj.has("settings")) {
                            JsonObject settings = modObj.getAsJsonObject("settings");
                            for (Setting<?> s : m.getSettings()) {
                                if (settings.has(s.getName())) {
                                    loadSetting(s, settings.get(s.getName()));
                                }
                            }
                        }
                        if (m instanceof HudModule) {
                            HudElement el = ((HudModule) m).getElement();
                            if (el != null && modObj.has("hud")) {
                                JsonObject hud = modObj.getAsJsonObject("hud");
                                if (hud.has("x")) el.setX(hud.get("x").getAsFloat());
                                if (hud.has("y")) el.setY(hud.get("y").getAsFloat());
                                if (hud.has("scale")) el.setScale(hud.get("scale").getAsFloat());
                            }
                        }
                    }
                }
            }

            // Client settings
            if (root.has("client")) {
                JsonObject client = root.getAsJsonObject("client");
                ClientManager cm = OSpeedlessClient.instance.getClientManager();
                if (client.has("blurBackground")) cm.blurBackground = client.get("blurBackground").getAsBoolean();
                if (client.has("disableHotbarScroll")) cm.disableHotbarScroll = client.get("disableHotbarScroll").getAsBoolean();
                if (client.has("capeDisplay")) cm.capeDisplay = client.get("capeDisplay").getAsBoolean();
                if (client.has("borderlessFullscreen")) cm.borderlessFullscreen = client.get("borderlessFullscreen").getAsBoolean();
                if (client.has("hudSnapping")) cm.hudSnapping = client.get("hudSnapping").getAsBoolean();
                if (client.has("hudGuides")) cm.hudGuides = client.get("hudGuides").getAsBoolean();
                if (client.has("performancePreset")) cm.performancePreset = client.get("performancePreset").getAsString();
                if (client.has("panelOpacity")) cm.panelOpacity = client.get("panelOpacity").getAsFloat();
                if (client.has("yellowTheme")) cm.yellowTheme = client.get("yellowTheme").getAsBoolean();
            }

            System.out.println("[oSpeedless] Config loaded successfully.");
        } catch (Exception e) {
            System.err.println("[oSpeedless] Failed to load config: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            JsonObject root = new JsonObject();

            JsonObject modules = new JsonObject();
            for (Module m : OSpeedlessClient.instance.getModuleManager().getModules()) {
                JsonObject modObj = new JsonObject();
                modObj.addProperty("enabled", m.isEnabled());
                modObj.addProperty("keybind", m.getKeybind());

                JsonObject settings = new JsonObject();
                for (Setting<?> s : m.getSettings()) {
                    saveSetting(s, settings);
                }
                modObj.add("settings", settings);

                if (m instanceof HudModule) {
                    HudElement el = ((HudModule) m).getElement();
                    if (el != null) {
                        JsonObject hud = new JsonObject();
                        hud.addProperty("x", el.getX());
                        hud.addProperty("y", el.getY());
                        hud.addProperty("scale", el.getScale());
                        modObj.add("hud", hud);
                    }
                }
                modules.add(m.getName(), modObj);
            }
            root.add("modules", modules);

            JsonObject client = new JsonObject();
            ClientManager cm = OSpeedlessClient.instance.getClientManager();
            client.addProperty("blurBackground", cm.blurBackground);
            client.addProperty("disableHotbarScroll", cm.disableHotbarScroll);
            client.addProperty("capeDisplay", cm.capeDisplay);
            client.addProperty("borderlessFullscreen", cm.borderlessFullscreen);
            client.addProperty("hudSnapping", cm.hudSnapping);
            client.addProperty("hudGuides", cm.hudGuides);
            client.addProperty("performancePreset", cm.performancePreset);
            client.addProperty("panelOpacity", cm.panelOpacity);
            client.addProperty("yellowTheme", cm.yellowTheme);
            root.add("client", client);

            try (Writer writer = new OutputStreamWriter(new FileOutputStream(configFile), StandardCharsets.UTF_8)) {
                gson.toJson(root, writer);
            }
        } catch (Exception e) {
            System.err.println("[oSpeedless] Failed to save config: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void saveSetting(Setting<?> s, JsonObject obj) {
        if (s instanceof BooleanSetting) {
            obj.addProperty(s.getName(), ((BooleanSetting) s).getValue());
        } else if (s instanceof NumberSetting) {
            obj.addProperty(s.getName(), ((NumberSetting) s).getValue());
        } else if (s instanceof ModeSetting) {
            obj.addProperty(s.getName(), ((ModeSetting) s).getValue());
        } else if (s instanceof ColorSetting) {
            obj.addProperty(s.getName(), ((ColorSetting) s).getValue());
        } else if (s instanceof TextSetting) {
            obj.addProperty(s.getName(), ((TextSetting) s).getValue());
        }
    }

    @SuppressWarnings("unchecked")
    private void loadSetting(Setting<?> s, JsonElement el) {
        try {
            if (s instanceof BooleanSetting) {
                ((BooleanSetting) s).setValue(el.getAsBoolean());
            } else if (s instanceof NumberSetting) {
                ((NumberSetting) s).setValue(el.getAsDouble());
            } else if (s instanceof ModeSetting) {
                ((ModeSetting) s).setValue(el.getAsString());
            } else if (s instanceof ColorSetting) {
                ((ColorSetting) s).setValue(el.getAsInt());
            } else if (s instanceof TextSetting) {
                ((TextSetting) s).setValue(el.getAsString());
            }
        } catch (Exception ignored) {
        }
    }
}
