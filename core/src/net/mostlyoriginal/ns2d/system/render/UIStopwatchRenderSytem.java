package net.mostlyoriginal.ns2d.system.render;

import com.artemis.annotations.Wire;
import com.artemis.systems.VoidEntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import net.mostlyoriginal.ns2d.system.active.DirectorSystem;
import net.mostlyoriginal.ns2d.system.passive.AssetSystem;
import net.mostlyoriginal.ns2d.system.passive.CameraSystem;

/**
 * @author Daan van Yperen
 */
@Wire
public class UIStopwatchRenderSytem extends VoidEntitySystem {

    private static final Color HOLO_COLOR = Color.valueOf("73BCC9");
    private static final float DISPLAY_DURATION = 4;

    private CameraSystem cameraSystem;
    private AssetSystem assetSystem;
    private DirectorSystem directorSystem;

    private SpriteBatch batch = new SpriteBatch();
    private float age =0;

    @Override
    protected void processSystem() {

            age += world.delta;

            batch.setProjectionMatrix(cameraSystem.guiCamera.combined);
            batch.begin();
            batch.setColor(1f, 1f, 1f, 1f);

            assetSystem.fontLarge.setColor(1f,1f,1f, 1f);

            String cost = String.format("%02d:%02d:%02d",
                    (int)(age / 60),
                    (int)(age % 60),
                    (int)((age * 100) % 100)
                    );
            BitmapFont.TextBounds bounds = assetSystem.fontLarge.getBounds(cost);
            assetSystem.fontLarge.draw(batch, cost, Gdx.graphics.getWidth() / 2 - bounds.width - 10, Gdx.graphics.getHeight()/2 + 10);
            batch.end();
    }
}
