package net.mostlyoriginal.ns2d;

import com.artemis.World;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import net.mostlyoriginal.ns2d.system.CameraFocusSystem;
import net.mostlyoriginal.ns2d.system.CameraSystem;
import net.mostlyoriginal.ns2d.system.passive.AssetSystem;
import net.mostlyoriginal.ns2d.system.passive.EntitySpawnSystem;
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

        // Passive System, loader helpers.
        G.world.setSystem(new AssetSystem());
        G.world.setSystem(new MapSystem());
        G.world.setSystem(new EntitySpawnSystem());
        G.world.setSystem(new CameraSystem());

        // Active
        G.world.setSystem(new CameraFocusSystem());

        // Render
        G.world.setSystem(new MapRenderSystem());
        G.world.setSystem(new AnimRenderSystem());

        G.world.initialize();
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(1, 0, 0, 1);
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
