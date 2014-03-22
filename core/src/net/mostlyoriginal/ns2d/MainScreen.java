package net.mostlyoriginal.ns2d;

import com.artemis.World;
import com.artemis.managers.GroupManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import net.mostlyoriginal.ns2d.system.active.*;
import net.mostlyoriginal.ns2d.system.passive.AssetSystem;
import net.mostlyoriginal.ns2d.system.passive.CameraSystem;
import net.mostlyoriginal.ns2d.system.passive.MapSystem;
import net.mostlyoriginal.ns2d.system.render.AnimRenderSystem;
import net.mostlyoriginal.ns2d.system.render.MapRenderSystem;

/**
 * @author Daan van Yperen
 */
public class MainScreen implements Screen {

    OrthographicCamera camera;

    public MainScreen() {
        G.screen = this;

        G.world = new World();

        G.world.setManager(new GroupManager());

        // Active - Cleanup
        G.world.setSystem(new TerminalSystem());
        G.world.setSystem(new EntitySpawnerSystem());

        // Passive System, loader helpers.
        G.world.setSystem(new AssetSystem());
        G.world.setSystem(new MapSystem());
        G.world.setSystem(new CameraSystem());

        // Active - Input/Logic
        G.world.setSystem(new PlayerControlSystem());
        G.world.setSystem(new SkulkControlSystem());

        // Active - Physics. Order is important! Alter velocity, then constrain.
        G.world.setSystem(new GravitySystem());
        G.world.setSystem(new MapCollisionSystem());
        G.world.setSystem(new AfterPhysicsSystem());

        // Active - Fixed movement
        G.world.setSystem(new AttachmentSystem());
        G.world.setSystem(new MouseCursorSystem());
        G.world.setSystem(new AimSystem());

        G.world.setSystem(new BulletCollisionSystem());

        // Active - Camera
        G.world.setSystem(new CameraFocusSystem());

        // Active - Render
        G.world.setSystem(new MapRenderSystem());
        G.world.setSystem(new AnimRenderSystem());

        G.world.initialize();
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0, 0, 0, 1);
  		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        G.world.setDelta(delta);
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
