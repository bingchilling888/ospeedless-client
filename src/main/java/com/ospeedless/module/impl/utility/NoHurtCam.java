package com.ospeedless.module.impl.utility;

import com.ospeedless.module.Category;
import com.ospeedless.module.Module;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class NoHurtCam extends Module {

    public NoHurtCam() {
        super("NoHurtCam", "Disables the hurt camera shake", Category.UTILITY, "NHC");
    }

    @SubscribeEvent
    public void onCameraSetup(EntityViewRenderEvent.CameraSetup event) {
        // Hurt cam is controlled by EntityRenderer; we zero the roll/shake via FOV/camera events where possible.
        // Full removal requires access transformer or mixin; for pure Forge we reduce visual impact.
        event.roll = 0;
    }
}
