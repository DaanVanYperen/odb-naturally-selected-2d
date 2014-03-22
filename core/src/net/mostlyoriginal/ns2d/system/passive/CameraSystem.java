package net.mostlyoriginal.ns2d.system.passive;

import com.artemis.systems.VoidEntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;

/**
 * @author Daan van Yperen
 */
public class CameraSystem extends VoidEntitySystem {

    public final OrthographicCamera camera;

    private static final float ZOOM = 0.5f;

    public CameraSystem() {
        camera = new OrthographicCamera(Gdx.graphics.getWidth() * ZOOM, Gdx.graphics.getHeight() * ZOOM);
        camera.setToOrtho(false, Gdx.graphics.getWidth() * ZOOM, Gdx.graphics.getHeight() * ZOOM);
        camera.update();
    }

    @Override
    protected void processSystem() {

    }
}
