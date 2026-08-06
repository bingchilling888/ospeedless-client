package com.ospeedless.hud;

import com.ospeedless.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public abstract class HudElement {

    protected final Minecraft mc = Minecraft.getMinecraft();
    protected Module parent;
    protected float x;
    protected float y;
    protected float width;
    protected float height;
    protected float scale = 1.0f;
    protected boolean dragging;
    protected float dragX;
    protected float dragY;

    public HudElement(Module parent, float defaultX, float defaultY) {
        this.parent = parent;
        this.x = defaultX;
        this.y = defaultY;
    }

    public abstract void render(ScaledResolution sr);

    public void renderPreview(ScaledResolution sr) {
        render(sr);
    }

    public boolean isHovered(int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + width * scale && mouseY >= y && mouseY <= y + height * scale;
    }

    public void startDrag(int mouseX, int mouseY) {
        dragging = true;
        dragX = mouseX - x;
        dragY = mouseY - y;
    }

    public void updateDrag(int mouseX, int mouseY, boolean snap, int snapSize) {
        if (!dragging) return;
        float newX = mouseX - dragX;
        float newY = mouseY - dragY;
        if (snap) {
            newX = Math.round(newX / snapSize) * snapSize;
            newY = Math.round(newY / snapSize) * snapSize;
        }
        x = Math.max(0, newX);
        y = Math.max(0, newY);
    }

    public void stopDrag() {
        dragging = false;
    }

    public float getX() { return x; }
    public float getY() { return y; }
    public void setX(float x) { this.x = x; }
    public void setY(float y) { this.y = y; }
    public float getWidth() { return width; }
    public float getHeight() { return height; }
    public float getScale() { return scale; }
    public void setScale(float scale) { this.scale = scale; }
    public Module getParent() { return parent; }
    public boolean isDragging() { return dragging; }
}
