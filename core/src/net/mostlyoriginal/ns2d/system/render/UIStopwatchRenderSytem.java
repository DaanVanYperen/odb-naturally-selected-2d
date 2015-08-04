package net.mostlyoriginal.ns2d.system.render;

import com.artemis.BaseSystem;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.utils.Pools;

import net.mostlyoriginal.ns2d.G;
import net.mostlyoriginal.ns2d.system.active.PlayerControlSystem;
import net.mostlyoriginal.ns2d.system.passive.AssetSystem;
import net.mostlyoriginal.ns2d.system.passive.CameraSystem;

/**
 * @author Daan van Yperen
 */
@Wire
public final class UIStopwatchRenderSytem extends BaseSystem {
    private CameraSystem cameraSystem;
    private AssetSystem assetSystem;
    private UIStageRenderSystem stageRenderSystem;
    private PlayerControlSystem playerControlSystem;

    private SpriteBatch batch = new SpriteBatch();
    private float retryCooldown = 1;
    private float age = 0;
    private float improvement = 0;
    private float bounce;
    public boolean gameOver = false;

    @Override
    protected void processSystem() {
        bounce += world.delta;
        batch.setProjectionMatrix(cameraSystem.guiCamera.combined);
        batch.begin();
        batch.setColor(1f, 1f, 1f, 1f);
        assetSystem.fontLarge.setColor(1f, 1f, 1f, 1f);

        String cost = formatAge();
        GlyphLayout layout = Pools.obtain(GlyphLayout.class);
        layout.setText(assetSystem.fontLarge, cost);
        if (gameOver) {
            if (G.settings.personalHighscore < (int) age) {
                improvement = (int) age - G.settings.personalHighscore;
                G.settings.personalHighscore = (int) age;
                G.settings.save();
            }

            // disable these systems
            stageRenderSystem.setEnabled(false);
            playerControlSystem.setEnabled(false);

            assetSystem.fontLarge.getData().setScale(Interpolation.elastic.apply(3, (improvement > 0 ? 4 : 3), Math.abs((bounce % 2) - 1)));
            layout.setText(assetSystem.fontLarge, cost);
            assetSystem.fontLarge.draw(batch, cost, Gdx.graphics.getWidth() / 4 - layout.width / 2, Gdx.graphics.getHeight() / 4 + layout.height / 2 + 5);
            assetSystem.fontLarge.getData().setScale(3);

            String message = improvement > 0 ? "Game over! Personal highscore! You survived for:" : "Game Over! You survived for:";
            layout.setText(assetSystem.font, message);
            assetSystem.font.draw(batch, message, Gdx.graphics.getWidth() / 4 - layout.width / 2, Gdx.graphics.getHeight() / 4 + 40);

            retryCooldown -= world.delta;
            if (retryCooldown <= 0) {
                message = "Press space to try again";
                layout.setText(assetSystem.font, message);
                assetSystem.font.draw(batch, message, Gdx.graphics.getWidth() / 4 - layout.width / 2, Gdx.graphics.getHeight() / 4 - 30);
                if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                    G.game.restart();
                    return;
                }
            }
        } else {
            age += world.delta;
            assetSystem.fontLarge.draw(batch, cost, Gdx.graphics.getWidth() / 2 - layout.width - 10, Gdx.graphics.getHeight() / 2 + 10);
        }
        Pools.free(layout);
        batch.end();
    }

    private String formatAge() {
        return format((int) (age / 60)) + ":" + format((int) (age % 60)) + ":" + format((int) ((age * 100) % 100));
    }

    private String format(int value) {
        return value < 10 ? "0" + value : "" + value;
    }
}
