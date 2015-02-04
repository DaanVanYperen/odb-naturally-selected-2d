package net.mostlyoriginal.ns2d.system.render;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;
import net.mostlyoriginal.ns2d.G;
import net.mostlyoriginal.ns2d.component.Bounds;
import net.mostlyoriginal.ns2d.component.Harvester;
import net.mostlyoriginal.ns2d.component.Pos;
import net.mostlyoriginal.ns2d.system.passive.AssetSystem;
import net.mostlyoriginal.ns2d.system.passive.CameraSystem;
import net.mostlyoriginal.ns2d.system.passive.FramebufferManager;

/**
 * @author Daan van Yperen
 */
@Wire
public class LightRenderSystem extends EntityProcessingSystem {

	private FramebufferManager framebufferManager;

	private SpriteBatch batch;
	private CameraSystem cameraSystem;
	private ShaderProgram deferredShader;
	private AssetSystem assetSystem;
	private float age;

	protected ComponentMapper<Pos> mPos;
	protected ComponentMapper<Bounds> mBounds;

	public LightRenderSystem() {
		super(Aspect.getAspectForAll(Pos.class, Harvester.class));
	}

	@Override
	protected void initialize() {
		super.initialize();

		deferredShader = new ShaderProgram(Gdx.files.internal("shader/deferred.vertex"), Gdx.files.internal("shader/deferred.fragment"));
		if ( !deferredShader.isCompiled() ) throw new RuntimeException("Compilation failed." + deferredShader.getLog());
		this.batch = new SpriteBatch(100, deferredShader);
	}

	private void renderLight(float lightX, float lightY, float lightZ, float lightR, float lightG, float lightB, int lightRadius, float lightIntensity) {
		//deferredShader.setUniformf("iGlobalTime", age);
		deferredShader.setUniformf("lightX", lightX);
		deferredShader.setUniformf("lightY", lightY);
		deferredShader.setUniformf("lightZ", lightZ);
		deferredShader.setUniformf("lightR", lightR);
		deferredShader.setUniformf("lightG", lightG);
		deferredShader.setUniformf("lightB", lightB);
		deferredShader.setUniformf("screenWidth", Gdx.graphics.getWidth());
		deferredShader.setUniformf("screenHeight", Gdx.graphics.getHeight());
		deferredShader.setUniformf("lightIntensity", lightIntensity);
		deferredShader.setUniformf("lightRadius", lightRadius);
		FrameBuffer normalBuffer = framebufferManager.getFrameBuffer(G.NORMAL_FBO);
		bindShaderToTexture("u_texture2", 1, normalBuffer.getColorBufferTexture());
		Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);
		batch.setBlendFunction(GL20.GL_ONE, GL20.GL_ONE);
		batch.setColor(1f, 1f, 1f, 1f);
		FrameBuffer diffuseBuffer = framebufferManager.getFrameBuffer(G.DIFFUSE_FBO);
		batch.draw(diffuseBuffer.getColorBufferTexture(), 0, Gdx.graphics.getHeight(), Gdx.graphics.getWidth(), - (Gdx.graphics.getHeight()));
		batch.flush();
	}

	private void bindShaderToTexture(String parameter, int value, Texture texture) {
		texture.bind(value);
		deferredShader.setUniformi(parameter, value);
	}

	@Override
	protected void begin() {
		age += world.delta;

		batch.setProjectionMatrix(cameraSystem.guiCamera.combined);
		batch.begin();
		renderLight(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY(), 40, 60 / 255F, 110 / 255F, 22 / 255F, 100,10);
		//renderLight(Gdx.graphics.getWidth() - Gdx.input.getX() / 2f, Gdx.graphics.getHeight() - (Gdx.input.getY() / 2f), 100, 155 / 255f, 255 / 255f, 210 / 255f, 100);
	}

	@Override
	protected void end() {
		batch.end();
	}

	Vector3 tmpVector = new Vector3();

	@Override
	protected void process(Entity e) {

		Pos pos = mPos.get(e);
		Bounds bounds = mBounds.get(e);

		Vector3 project = cameraSystem.camera.project(tmpVector.set(pos.x +(bounds != null ? bounds.cx() : 0) , pos.y +(bounds != null ? bounds.cy() : 0), 0));
		renderLight(project.x, project.y , 30, 1.0f, 1.0f, 1.0f, 80,1);

	}
}
