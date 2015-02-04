package net.mostlyoriginal.ns2d.system.render;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
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

//	private SpriteBatch batch;
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
//		this.batch = new SpriteBatch(100, deferredShader);
	}

	private void renderLight(float lightX, float lightY, float lightZ, float lightR, float lightG, float lightB, int lightRadius, float lightIntensity) {

		deferredShader.setUniformMatrix("u_projTrans", cameraSystem.guiCamera.combined);

		deferredShader.setUniformf("lightX", lightX);
		deferredShader.setUniformf("lightY", lightY);
		deferredShader.setUniformf("lightZ", lightZ);
		deferredShader.setUniformf("lightR", lightR);
		deferredShader.setUniformf("lightG", lightG);
		deferredShader.setUniformf("lightB", lightB);

		int width = Gdx.graphics.getWidth();
		int height = Gdx.graphics.getHeight();

		deferredShader.setUniformf("screenWidth", width);
		deferredShader.setUniformf("screenHeight", height);
		deferredShader.setUniformf("lightIntensity", lightIntensity);
		deferredShader.setUniformf("lightRadius", lightRadius);

		FrameBuffer normalBuffer  = framebufferManager.getFrameBuffer(G.NORMAL_FBO);
		bindShaderToTexture("u_texture2", 1, normalBuffer.getColorBufferTexture());

		FrameBuffer diffuseBuffer = framebufferManager.getFrameBuffer(G.DIFFUSE_FBO);
		bindShaderToTexture("u_texture", 0, diffuseBuffer.getColorBufferTexture());

		draw(
				MathUtils.clamp(0,lightX - lightRadius,width),
				MathUtils.clamp(0,lightY - lightRadius,height),
				MathUtils.clamp(0,lightX + lightRadius,width),
				MathUtils.clamp(0,lightY + lightRadius,height));
	}

	private void bindShaderToTexture(String parameter, int value, Texture texture) {
		texture.bind(value);
		deferredShader.setUniformi(parameter, value);
	}

	@Override
	protected void begin() {
		age += world.delta;

		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_ONE, GL20.GL_ONE);
		Gdx.gl.glDepthMask(false);

		deferredShader.begin();
		renderLight(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY(), 40, 60 / 255F, 110 / 255F, 22 / 255F, 100,10);
	}

	@Override
	protected void end() {
		deferredShader.end();

		Gdx.gl.glDisable(GL20.GL_BLEND);
		Gdx.gl.glDepthMask(true);
	}

	Vector3 tmpVector = new Vector3();

	@Override
	protected void process(Entity e) {

		Pos pos = mPos.get(e);
		Bounds bounds = mBounds.get(e);

		Vector3 project = cameraSystem.camera.project(tmpVector.set(pos.x +(bounds != null ? bounds.cx() : 0) , pos.y +(bounds != null ? bounds.cy() : 0), 0));
		renderLight(project.x, project.y , 60f, 1.0f, 1.0f, 1.0f, 80, 1);

	}

	float[] vertices;
	private Mesh mesh;

	public void draw (float x, float y, float width, float height) {

		if ( mesh == null )
		{
			mesh = new Mesh(Mesh.VertexDataType.VertexArray, false, 4, 6,
					new VertexAttribute(VertexAttributes.Usage.Position, 2, "a_position"),
					new VertexAttribute(VertexAttributes.Usage.TextureCoordinates, 2, "a_texCoord0"));
			mesh.setIndices(new short[] { 0, (short)1, (short)2, (short)2, (short)3, 0 });
			vertices = new float[16];
		}

		int idx=0;

		final float fx2 = x + width;
		final float fy2 = y + height;
//		final float u = x/width;
//		final float v = height/(float)Gdx.graphics.getHeight();
//		final float u2 = width/(float)Gdx.graphics.getWidth();
//		final float v2 = y/height;
		final float u = x/(float)Gdx.graphics.getWidth();
		final float u2 = (x+width)/(float)Gdx.graphics.getWidth();

		final float v = y/(float)Gdx.graphics.getHeight();
		final float v2 = (y+height)/(float)Gdx.graphics.getHeight();

		vertices[idx++] = x;
		vertices[idx++] = y;
		vertices[idx++] = u;
		vertices[idx++] = v;

		vertices[idx++] = x;
		vertices[idx++] = fy2;
		vertices[idx++] = u;
		vertices[idx++] = v2;

		vertices[idx++] = fx2;
		vertices[idx++] = fy2;
		vertices[idx++] = u2;
		vertices[idx++] = v2;

		vertices[idx++] = fx2;
		vertices[idx++] = y;
		vertices[idx++] = u2;
		vertices[idx++] = v;

		mesh.setVertices(vertices, 0, idx);
		mesh.getIndicesBuffer().position(0);
		mesh.getIndicesBuffer().limit(6);

		mesh.render(deferredShader, GL20.GL_TRIANGLES, 0, 6);
	}

}
