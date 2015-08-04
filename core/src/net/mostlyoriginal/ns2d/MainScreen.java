package net.mostlyoriginal.ns2d;

import com.artemis.World;
import com.artemis.managers.GroupManager;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;

import net.mostlyoriginal.api.utils.builder.WorldConfigurationBuilder;
import net.mostlyoriginal.ns2d.system.active.*;
import net.mostlyoriginal.ns2d.system.collide.BulletCollisionSystem;
import net.mostlyoriginal.ns2d.system.passive.*;
import net.mostlyoriginal.ns2d.system.render.*;

/**
 * @author Daan van Yperen
 */
public class MainScreen implements Screen {
	OrthographicCamera camera;

	public MainScreen() {
		G.screen = this;

		WorldConfigurationBuilder builder = new WorldConfigurationBuilder();

		builder.with(new GroupManager());
		builder.with(new TagManager());
		builder.with(new FramebufferManager());

		// Active - Cleanup
		builder.with(new TerminalSystem());
		builder.with(new EntitySpawnerSystem());
		builder.with(new ScriptSystem());

		// Passive System, loader helpers.
		builder.with(new AssetSystem());
		builder.with(new MapSystem());
		builder.with(new CameraSystem());
		builder.with(new CollisionSystem());
		builder.with(new ParticleSystem());

		// Active - Input/Logic
		builder.with(new PlayerControlSystem());
		builder.with(new SkulkControlSystem());
		builder.with(new WeaponSystem());

		// Active - Interactions
		builder.with(new BuildableSystem());
		builder.with(new CombatSystem());
		builder.with(new HarvesterSystem());

		// Active - Physics. Order is important! Alter velocity, then constrain.
		builder.with(new PhysicsSystem());
		builder.with(new GravitySystem());
		builder.with(new HomingSystem());
		builder.with(new InbetweenSystem());
		builder.with(new MapCollisionSystem());
		builder.with(new AfterPhysicsSystem());

		// Active - Fixed movement
		builder.with(new AttachmentSystem());
		builder.with(new MouseCursorSystem());
		builder.with(new AimSystem());

		// Active - Post Movement Calculations.
		builder.with(new WallSensorSystem());

		builder.with(new BulletCollisionSystem());

		// Active - Camera
		builder.with(new CameraFocusSystem());
		builder.with(new CameraShakeSystem());

		// Active - Render
		// G.world.setSystem(new MapRenderSystem());
		// G.world.setSystem(new CostRenderSystem());
		// G.world.setSystem(new HealthRenderSystem());

		MultipassRenderBatchingSystem multipassRenderBatchingSystem = new MultipassRenderBatchingSystem();
		builder.with(multipassRenderBatchingSystem);
		builder.with(new AnimRenderSystem(multipassRenderBatchingSystem));

		// G.world.setSystem(new MapRenderSystemInFront());

		builder.with(new DialogRenderSystem());
		builder.with(new UIRenderSystem());
		builder.with(new UIAlertActiveSpawnerSystem());
		builder.with(new UIAlertBuildableUnderAttack());

		builder.with(new UIAlertTechpointUnderAttack());
		builder.with(new UIStageRenderSystem());
		builder.with(new UIStopwatchRenderSytem());

		builder.with(new LightRenderSystem());

		builder.with(new DirectorSystem());

		G.world = new World(builder.build());
	}

	@Override
	public void render(float delta) {

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		G.world.setDelta(MathUtils.clamp(delta, 0, 1 / 15f));
		G.world.process();
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void show() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {

	}
}
