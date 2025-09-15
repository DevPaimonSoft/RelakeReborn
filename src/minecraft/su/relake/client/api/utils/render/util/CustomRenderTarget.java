package su.relake.client.api.utils.render.util;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import org.lwjgl.opengl.GL11;

public class CustomRenderTarget extends RenderTarget implements IMinecraft {
    private boolean linear;

    public CustomRenderTarget(boolean useDepth) {
        super(useDepth);
    }

    public CustomRenderTarget(int width, int height, boolean useDepth) {
        super(useDepth);
        this.resize(width, height);
    }


    public CustomRenderTarget setLinear() {
        this.linear = true;
        RenderSystem.recordRenderCall(() -> this.setFilterMode(GL11.GL_LINEAR));
        return this;
    }


    @Override
    public void setFilterMode(int framebufferFilterIn) {
        super.setFilterMode(this.linear ? GL11.GL_LINEAR : framebufferFilterIn);
    }


    private void resizeFramebuffer() {
        if (this.needsNewFramebuffer()) {
            this.createBuffers(Math.max(mw.getWidth(), 1), Math.max(mw.getHeight(), 1));
        }
    }


    public void setup(boolean clear) {
        this.resizeFramebuffer();
        if (clear) this.clear();
        this.bindWrite(false);
    }


    public void setup() {
        setup(true);
    }


    public void stop() {
        this.unbindWrite();
        mc.getMainRenderTarget().bindWrite(true);
    }


    private boolean needsNewFramebuffer() {
        return this.width != mw.getWidth() || this.height != mw.getHeight();
    }
}
