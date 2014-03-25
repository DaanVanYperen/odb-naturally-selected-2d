package net.mostlyoriginal.ns2d.system.render;

import com.artemis.annotations.Wire;
import com.artemis.systems.VoidEntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
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
    public boolean gameOver =false;
    private float improvement=0;
    float bounce;

    @Override
    protected void processSystem() {

        bounce += world.delta;
        batch.setProjectionMatrix(cameraSystem.guiCamera.combined);
        batch.begin();
        batch.setColor(1f, 1f, 1f, 1f);
        assetSystem.fontLarge.setColor(1f, 1f, 1f, 1f);

        String cost = formatAge();
        BitmapFont.TextBounds bounds = assetSystem.fontLarge.getBounds(cost);
        if (gameOver) {

            if ( G.settings.personalHighscore < (int)age )
            {
                improvement = (int)age - G.settings.personalHighscore;
                G.settings.personalHighscore = (int)age;
                G.settings.save();
            }

            // disable these systems
            stageRenderSystem.setEnabled(false);
            playerControlSystem.setEnabled(false);

            assetSystem.fontLarge.setScale(Interpolation.elastic.apply(3,( improvement > 0 ? 4 : 3),Math.abs((bounce%2)-1)));
            BitmapFont.TextBounds bounds2 = assetSystem.fontLarge.getBounds(cost);
            assetSystem.fontLarge.draw(batch, cost, Gdx.graphics.getWidth() / 4 - bounds2.width / 2, Gdx.graphics.getHeight() / 4 + bounds2.height/2 + 5);
            assetSystem.fontLarge.setScale(3);

            String message = improvement > 0 ? "Game over! Personal highscore! You survived for:" : "Game Over! You survived for:";
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
            String message = "Stage " + (directorSystem.activeStage + 1);
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
