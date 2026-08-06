package com.ospeedless.module;

import com.ospeedless.module.impl.hud.*;
import com.ospeedless.module.impl.performance.*;
import com.ospeedless.module.impl.render.*;
import com.ospeedless.module.impl.utility.*;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;

public class ModuleManager {

    private final List<Module> modules = new ArrayList<Module>();

    public void init() {
        // HUD Modules
        register(new FPSDisplay());
        register(new PingDisplay());
        register(new CPSDisplay());
        register(new Keystrokes());
        register(new Coordinates());
        register(new ArmorStatus());
        register(new PotionEffects());
        register(new ComboCounter());
        register(new SprintIndicator());
        register(new Direction());
        register(new BiomeDisplay());
        register(new TimeDisplay());
        register(new MemoryUsage());
        register(new SpeedDisplay());
        register(new ReachDisplay());
        register(new HealthDisplay());
        register(new ServerAddress());
        register(new PlayerCount());
        register(new Playtime());
        register(new ActiveMods());
        register(new ArrowCounter());
        register(new BlocksCounter());
        register(new GappleCounter());
        register(new PotionCounter());

        // Render Modules
        register(new Crosshair());
        register(new Hitbox());
        register(new Nametags());
        register(new BlockOverlay());
        register(new MotionBlur());

        // Performance Modules
        register(new FPSBoost());
        register(new FrameUnlock());
        register(new Fullbright());

        // Utility Modules
        register(new Zoom());
        register(new NoHurtCam());
        register(new ToggleSprint());
    }

    private void register(Module module) {
        modules.add(module);
    }

    public List<Module> getModules() {
        return modules;
    }

    public List<Module> getModulesByCategory(Category category) {
        List<Module> result = new ArrayList<Module>();
        for (Module m : modules) {
            if (m.getCategory() == category) {
                result.add(m);
            }
        }
        return result;
    }

    public Module getModule(String name) {
        for (Module m : modules) {
            if (m.getName().equalsIgnoreCase(name)) {
                return m;
            }
        }
        return null;
    }

    public Module getModule(Class<? extends Module> clazz) {
        for (Module m : modules) {
            if (m.getClass() == clazz) {
                return m;
            }
        }
        return null;
    }

    public void onTick() {
        for (Module m : modules) {
            if (m.isEnabled()) {
                m.onTick();
            }
        }
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (!Keyboard.getEventKeyState()) return;
        int key = Keyboard.getEventKey();
        for (Module m : modules) {
            if (m.getKeybind() != Keyboard.KEY_NONE && m.getKeybind() == key) {
                m.toggle();
            }
        }
    }
}
