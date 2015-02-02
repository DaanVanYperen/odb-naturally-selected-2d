package net.mostlyoriginal.ns2d;

import com.artemis.World;
import com.artemis.managers.GroupManager;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
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

        G.world = new World();

        G.world.setManager(new GroupManager());
        G.world.setManager(new TagManager());

	    G.world.setSystem(new FramebufferManager());

        // Active - Cleanup
        G.world.setSystem(new TerminalSystem());
        G.world.setSystem(new EntitySpawnerSystem());
        G.world.setSystem(new ScriptSystem());

        // Passive System, loader helpers.
        G.world.setSystem(new AssetSystem());
        G.world.setSystem(new MapSystem());
        G.world.setSystem(new CameraSystem());
        G.world.setSystem(new CollisionSystem());
        G.world.setSystem(new ParticleSystem());

        // Active - Input/Logic
        G.world.setSystem(new PlayerControlSystem());
        G.world.setSystem(new SkulkControlSystem());
        G.world.setSystem(new WeaponSystem());

        // Active - Interactions
        G.world.setSystem(new BuildableSystem());
        G.world.setSystem(new CombatSystem());
        G.world.setSystem(new HarvesterSystem());

        // Active - Physics. Order is important! Alter velocity, then constrain.
        G.world.setSystem(new PhysicsSystem());
        G.world.setSystem(new GravitySystem());
        G.world.setSystem(new HomingSystem());
        G.world.setSystem(new InbetweenSystem());
        G.world.setSystem(new MapCollisionSystem());
        G.world.setSystem(new AfterPhysicsSystem());

        // Active - Fixed movement
        G.world.setSystem(new AttachmentSystem());
        G.world.setSystem(new MouseCursorSystem());
        G.world.setSystem(new AimSystem());

        // Active - Post Movement Calculations.
        G.world.setSystem(new WallSensorSystem());

        G.world.setSystem(new BulletCollisionSystem());

        // Active - Camera
        G.world.setSystem(new CameraFocusSystem());
        G.world.setSystem(new CameraShakeSystem());

        // Active - Render
	    //G.world.setSystem(new MapRenderSystem());
        //G.world.setSystem(new CostRenderSystem());
        //G.world.setSystem(new HealthRenderSystem());

	    MultipassRenderBatchingSystem multipassRenderBatchingSystem = new MultipassRenderBatchingSystem();
	    G.world.setSystem(multipassRenderBatchingSystem);
	    G.world.setSystem(new AnimRenderSystem(multipassRenderBatchingSystem), false);

        //G.world.setSystem(new MapRenderSystemInFront());

        G.world.setSystem(new DialogRenderSystem());
        G.world.setSystem(new UIRenderSystem());
        G.world.setSystem(new UIAlertActiveSpawnerSystem());
        G.world.setSystem(new UIAlertBuildableUnderAttack());

        G.world.setSystem(new UIAlertTechpointUnderAttack());
        G.world.setSystem(new UIStageRenderSystem());
        G.world.setSystem(new UIStopwatchRenderSytem());

	    G.world.setSystem(new LightRenderSystem());

        G.world.setSystem(new DirectorSystem());


        G.world.initialize();
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0, 0, 0, 1);
  		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        G.world.setDelta(MathUtils.clamp(delta,0, 1/15f));
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
