package net.mostlyoriginal.ns2d.system.render;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import net.mostlyoriginal.ns2d.component.Bounds;
import net.mostlyoriginal.ns2d.component.Health;
import net.mostlyoriginal.ns2d.component.HealthIndicator;
import net.mostlyoriginal.ns2d.component.Pos;
import net.mostlyoriginal.ns2d.system.passive.AssetSystem;
import net.mostlyoriginal.ns2d.system.passive.CameraSystem;

/**
 * @author Daan van Yperen
 */
@Wire
public final class HealthRenderSystem extends EntityProcessingSystem {
    private ComponentMapper<Pos> pm;
    private ComponentMapper<Bounds> om;
    private ComponentMapper<Health> hm;

    private CameraSystem cameraSystem;
    private AssetSystem assetSystem;

    private SpriteBatch batch = new SpriteBatch();
    public TextureRegion tick;
    public int tickWidth;

    public HealthRenderSystem() {
        super(Aspect.all(Pos.class, Bounds.class, Health.class, HealthIndicator.class));
    }

    @Override
    protected void initialize() {
        super.initialize();
        tick = assetSystem.get("health-tick").getKeyFrame(0);
        tickWidth = tick.getRegionWidth() - 1;
    }

    @Override
    protected void begin() {
        batch.setProjectionMatrix(cameraSystem.camera.combined);
        batch.begin();
        batch.setColor(1f, 1f, 1f, 1f);
    }

    @Override
    protected void end() {
        batch.end();
    }

    @Override
    protected void process(Entity e) {
        final Bounds bounds = om.get(e);
        final Pos pos = pm.get(e);
        final Health health = hm.get(e);

        final int width = bounds.x2 - bounds.x1;

        float factor = 1f - (health.damage / (float) health.health);

        int ticks = (int) ((width * factor) / tickWidth);

        for (int i = 0; i < ticks; i++) {
            batch.draw(tick, pos.x + i * tickWidth, pos.y + bounds.y2 + 10);
        }
    }
}
