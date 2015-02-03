package net.mostlyoriginal.ns2d.system.render;

import com.artemis.annotations.Wire;
import com.artemis.systems.VoidEntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import net.mostlyoriginal.ns2d.G;
import net.mostlyoriginal.ns2d.system.passive.AssetSystem;
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
	private ShaderProgram deferredShader;
	private AssetSystem assetSystem;
	private float age;

	@Override
	protected void initialize() {
		super.initialize();

		deferredShader = new ShaderProgram(Gdx.files.internal("shader/deferred.vertex"), Gdx.files.internal("shader/deferred.fragment"));
		if ( !deferredShader.isCompiled() ) throw new RuntimeException("Compilation failed." + deferredShader.getLog());
		this.batch = new SpriteBatch(100, deferredShader);
	}

	@Override
	protected void processSystem() {

		age += world.delta;

		renderLight(Gdx.input.getX() / 2f, Gdx.graphics.getHeight() * CameraSystem.ZOOM - (Gdx.input.getY() / 2f), 10);
		renderLight(Gdx.graphics.getWidth() * CameraSystem.ZOOM - Gdx.input.getX() / 2f, (Gdx.input.getY() / 2f), 100);

	}

	private void renderLight(float lightX, float lightY, float lightZ ) {
		batch.setProjectionMatrix(cameraSystem.guiCamera.combined);
		batch.begin();
		//deferredShader.setUniformf("iGlobalTime", age);
		deferredShader.setUniformf("lightX", lightX);
		deferredShader.setUniformf("lightY", lightY);
		deferredShader.setUniformf("lightZ", lightZ);
		deferredShader.setUniformf("screenWidth", Gdx.graphics.getWidth() * CameraSystem.ZOOM);
		deferredShader.setUniformf("screenHeight", Gdx.graphics.getHeight() * CameraSystem.ZOOM);
		FrameBuffer normalBuffer = framebufferManager.getFrameBuffer(G.NORMAL_FBO);
		bindShaderToTexture("u_texture2", 1, normalBuffer.getColorBufferTexture());
		Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);
		batch.setBlendFunction(GL20.GL_ONE, GL20.GL_ONE);
		batch.setColor(1f, 1f, 1f, 1f);
		FrameBuffer diffuseBuffer = framebufferManager.getFrameBuffer(G.DIFFUSE_FBO);
		batch.draw(diffuseBuffer.getColorBufferTexture(), 0, (int) (Gdx.graphics.getHeight() * CameraSystem.ZOOM), (int) (Gdx.graphics.getWidth() * CameraSystem.ZOOM), -(int) (Gdx.graphics.getHeight() * CameraSystem.ZOOM));
		batch.end();
	}

	private void bindShaderToTexture(String parameter, int value, Texture texture) {
		texture.bind(value);
		deferredShader.setUniformi(parameter, value);
	}
}
