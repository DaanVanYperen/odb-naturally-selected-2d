package net.mostlyoriginal.ns2d.system;

import com.artemis.systems.VoidEntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;

/**
 * @author Daan van Yperen
 */
public class CameraSystem extends VoidEntitySystem {

    public final OrthographicCamera camera;

    public CameraSystem() {
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.update();
    }

    @Override
    protected void processSystem() {

    }
}
