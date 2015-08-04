package net.mostlyoriginal.ns2d.system.passive;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;

import net.mostlyoriginal.api.system.core.PassiveSystem;

/**
 * @author Daan van Yperen
 */
public final class CameraSystem extends PassiveSystem {
    public final OrthographicCamera camera;
    public final OrthographicCamera guiCamera;

    public static final float ZOOM = 0.5f;

    public CameraSystem() {
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.update();

        guiCamera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        guiCamera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        guiCamera.update();
    }
}
