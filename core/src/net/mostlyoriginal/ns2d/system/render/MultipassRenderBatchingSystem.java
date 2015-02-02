package net.mostlyoriginal.ns2d.system.render;

import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import net.mostlyoriginal.api.system.graphics.RenderBatchingSystem;
import net.mostlyoriginal.ns2d.G;
import net.mostlyoriginal.ns2d.system.passive.FramebufferManager;

/**
 * Multipass render batching system.
 *
 * @author Daan van Yperen
 */
@Wire(injectInherited = true)
public class MultipassRenderBatchingSystem extends RenderBatchingSystem {

	FramebufferManager framebufferManager;
	AnimRenderSystem animRenderSystem;

	public MultipassRenderBatchingSystem() {
	}

	@Override
	protected void processEntities(ImmutableBag<Entity> entities) {

		FrameBuffer diffuseBuffer = framebufferManager.getFrameBuffer(G.DIFFUSE_FBO);

		diffuseBuffer.begin();
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		animRenderSystem.renderNormals(false);
		super.processEntities(entities);
		diffuseBuffer.end();

		FrameBuffer normalBuffer = framebufferManager.getFrameBuffer(G.NORMAL_FBO);
		animRenderSystem.renderNormals(true);
		normalBuffer.begin();
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		super.processEntities(entities);
		normalBuffer.end();
	}
}
