package net.mostlyoriginal.ns2d.system.render;

import com.artemis.annotations.Wire;
import com.artemis.systems.VoidEntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import net.mostlyoriginal.ns2d.G;
import net.mostlyoriginal.ns2d.system.active.DirectorSystem;
import net.mostlyoriginal.ns2d.system.active.PlayerControlSystem;
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
    private UIStageRenderSystem stageRenderSystem;
    private PlayerControlSystem playerControlSystem;

    private SpriteBatch batch = new SpriteBatch();
    private float retryCooldown = 1;
    private float age = 0;
    public boolean gameOver = false;

    @Override
    protected void processSystem() {

        batch.setProjectionMatrix(cameraSystem.guiCamera.combined);
        batch.begin();
        batch.setColor(1f, 1f, 1f, 1f);
        assetSystem.fontLarge.setColor(1f, 1f, 1f, 1f);

        String cost = formatAge();
        BitmapFont.TextBounds bounds = assetSystem.fontLarge.getBounds(cost);
        if (gameOver) {

            // disable these systems
            stageRenderSystem.setEnabled(false);
            playerControlSystem.setEnabled(false);

            assetSystem.fontLarge.draw(batch, cost, Gdx.graphics.getWidth() / 4 - bounds.width / 2, Gdx.graphics.getHeight() / 4 + 24);

            String message = "Game Over! You survived for:";
            bounds = assetSystem.font.getBounds(message);
            assetSystem.font.draw(batch, message, Gdx.graphics.getWidth() / 4 - bounds.width / 2, Gdx.graphics.getHeight() / 4 + 40);

            retryCooldown -= world.delta;
            if (retryCooldown <= 0) {
                message = "Press space to try again";
                bounds = assetSystem.font.getBounds(message);
                assetSystem.font.draw(batch, message, Gdx.graphics.getWidth() / 4 - bounds.width / 2, Gdx.graphics.getHeight() / 4 - 30);
                if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                    G.game.restart();
                    return;
                }
            }
        } else {
            age += world.delta;
            assetSystem.fontLarge.draw(batch, cost, Gdx.graphics.getWidth() / 2 - bounds.width - 10, Gdx.graphics.getHeight() / 2 + 10);
        }
        batch.end();
    }

    private String formatAge() {
        return String.format("%02d:%02d:%02d",
                (int) (age / 60),
                (int) (age % 60),
                (int) ((age * 100) % 100)
        );
    }
}
