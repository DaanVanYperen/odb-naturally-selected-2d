package net.mostlyoriginal.ns2d.system.render;

import com.artemis.annotations.Wire;
import com.artemis.systems.VoidEntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import net.mostlyoriginal.ns2d.G;
import net.mostlyoriginal.ns2d.system.passive.CameraSystem;
import net.mostlyoriginal.ns2d.system.passive.FramebufferManager;

/**
 * @author Daan van Yperen
 */
@Wire
public class LightRenderSystem extends VoidEntitySystem {

	private FramebufferManager framebufferManager;

	private SpriteBatch batch;
	private CameraSystem cameraSystem;

	@Override
	protected void initialize() {
		super.initialize();
		this.batch = new SpriteBatch(100);
	}

	@Override
	protected void processSystem() {

		batch.setProjectionMatrix(cameraSystem.guiCamera.combined);
		batch.begin();
//		shimmerProgram.setUniformf("iGlobalTime", age);
		batch.setColor(1f, 1f, 1f, 1f);

		FrameBuffer diffuseBuffer = framebufferManager.getFrameBuffer(G.DIFFUSE_FBO);
		FrameBuffer normalBuffer = framebufferManager.getFrameBuffer(G.NORMAL_FBO);

		batch.draw(diffuseBuffer.getColorBufferTexture(),0,Gdx.graphics.getHeight()* CameraSystem.ZOOM, Gdx.graphics.getWidth() * CameraSystem.ZOOM, -Gdx.graphics.getHeight()* CameraSystem.ZOOM);

		batch.end();
	}
}
