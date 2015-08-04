package net.mostlyoriginal.ns2d.system.render;

import com.artemis.annotations.Wire;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

import net.mostlyoriginal.api.system.graphics.RenderBatchingSystem;
import net.mostlyoriginal.ns2d.G;
import net.mostlyoriginal.ns2d.MyMapRendererImpl;
import net.mostlyoriginal.ns2d.system.passive.AssetSystem;
import net.mostlyoriginal.ns2d.system.passive.CameraSystem;
import net.mostlyoriginal.ns2d.system.passive.FramebufferManager;
import net.mostlyoriginal.ns2d.system.passive.MapSystem;

/**
 * Multipass render batching system.
 *
 * @author Daan van Yperen
 */
@Wire(injectInherited = true)
public class MultipassRenderBatchingSystem extends RenderBatchingSystem {
	private MapSystem mapSystem;
	private CameraSystem cameraSystem;
	private AssetSystem assetSystem;

	public MyMapRendererImpl renderer;

	FramebufferManager framebufferManager;
	AnimRenderSystem animRenderSystem;

	@Override
	protected void initialize() {
		renderer = new MyMapRendererImpl(mapSystem.map);
	}

	protected void renderMapBehind(Texture texture) {
		for (MapLayer layer : mapSystem.map.getLayers()) {
			if (layer.isVisible()) {
				if (!layer.getName().equals("infront")) {
					renderLayer((TiledMapTileLayer) layer, texture);
				}
			}
		}
	}

	protected void renderMapInFront(Texture texture) {
		for (MapLayer layer : mapSystem.map.getLayers()) {
			if (layer.isVisible()) {
				if (layer.getName().equals("infront")) {
					renderLayer((TiledMapTileLayer) layer, texture);
				}
			}
		}
	}

	private void renderLayer(final TiledMapTileLayer layer, Texture texture) {
		renderer.setView(cameraSystem.camera);
		renderer.renderLayer(layer, texture);
	}

	public MultipassRenderBatchingSystem() {
	}

	@Override
	protected void processSystem() {
		FrameBuffer diffuseBuffer = framebufferManager.getFrameBuffer(G.DIFFUSE_FBO);

		diffuseBuffer.begin();
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		renderMapBehind(assetSystem.tileset);
		animRenderSystem.renderNormals(false);
		super.processSystem();
		renderMapInFront(assetSystem.tileset);
		diffuseBuffer.end();

		FrameBuffer normalBuffer = framebufferManager.getFrameBuffer(G.NORMAL_FBO);
		animRenderSystem.renderNormals(true);
		normalBuffer.begin();
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		renderMapBehind(assetSystem.tilesetNormal);
		super.processSystem();
		renderMapInFront(assetSystem.tilesetNormal);
		normalBuffer.end();
	}
}
