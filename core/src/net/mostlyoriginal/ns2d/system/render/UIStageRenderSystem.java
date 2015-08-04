package net.mostlyoriginal.ns2d.system.render;

import com.artemis.BaseSystem;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Pools;

import net.mostlyoriginal.ns2d.system.active.DirectorSystem;
import net.mostlyoriginal.ns2d.system.passive.AssetSystem;
import net.mostlyoriginal.ns2d.system.passive.CameraSystem;

/**
 * @author Daan van Yperen
 */
@Wire
public final class UIStageRenderSystem extends BaseSystem {
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
        GlyphLayout layout = Pools.obtain(GlyphLayout.class);

        if (cooldown >= 0) {
            layout.setText(assetSystem.font, cost);
            batch.setProjectionMatrix(cameraSystem.guiCamera.combined);
            assetSystem.fontLarge.setColor(1f, 1f, 1f, MathUtils.clamp(cooldown, 0, 1));
            assetSystem.fontLarge.draw(batch, cost, Gdx.graphics.getWidth() / 4 - layout.width / 2, Gdx.graphics.getHeight() / 3 + 2);
        } else {
            layout.setText(assetSystem.font, cost);
            assetSystem.font.draw(batch, cost, Gdx.graphics.getWidth() / 2 - layout.width - 12, Gdx.graphics.getHeight() / 2 - 30);
        }
        Pools.free(layout);
        batch.end();
    }
}
