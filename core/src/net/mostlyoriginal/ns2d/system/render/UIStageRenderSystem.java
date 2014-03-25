package net.mostlyoriginal.ns2d.system.render;

import com.artemis.annotations.Wire;
import com.artemis.systems.VoidEntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import net.mostlyoriginal.ns2d.system.active.DirectorSystem;
import net.mostlyoriginal.ns2d.system.passive.AssetSystem;
import net.mostlyoriginal.ns2d.system.passive.CameraSystem;

/**
 * @author Daan van Yperen
 */
@Wire
public class UIStageRenderSystem extends VoidEntitySystem {

    private static final Color HOLO_COLOR = Color.valueOf("73BCC9");
    private static final float DISPLAY_DURATION = 4;

    private CameraSystem cameraSystem;
    private AssetSystem assetSystem;
    private DirectorSystem directorSystem;

    private SpriteBatch batch = new SpriteBatch();
    private float cooldown = DISPLAY_DURATION;
    private int reportedStage = -1;

    @Override
    protected void processSystem() {
        if (directorSystem.activeStage > reportedStage) {
            reportedStage = directorSystem.activeStage;
            cooldown = DISPLAY_DURATION;
        }

        String cost = "Stage " + (directorSystem.activeStage + 1);
        cooldown -= world.delta;
        batch.begin();
        batch.setColor(1f, 1f, 1f, 1f);

        if (cooldown >= 0) {
            batch.setProjectionMatrix(cameraSystem.guiCamera.combined);
            assetSystem.fontLarge.setColor(1f, 1f, 1f, MathUtils.clamp(cooldown, 0, 1));
            assetSystem.fontLarge.draw(batch, cost, Gdx.graphics.getWidth() / 4 - assetSystem.fontLarge.getBounds(cost).width / 2, Gdx.graphics.getHeight() / 3 + 2);
        } else {
            BitmapFont.TextBounds bounds = assetSystem.font.getBounds(cost);
            assetSystem.font.draw(batch, cost, Gdx.graphics.getWidth() / 2 - bounds.width - 12, Gdx.graphics.getHeight() / 2 - 30);
        }
        batch.end();
    }
}
