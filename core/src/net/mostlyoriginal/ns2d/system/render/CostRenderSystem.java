package net.mostlyoriginal.ns2d.system.render;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import net.mostlyoriginal.ns2d.component.Anim;
import net.mostlyoriginal.ns2d.component.Bounds;
import net.mostlyoriginal.ns2d.component.Buildable;
import net.mostlyoriginal.ns2d.component.Pos;
import net.mostlyoriginal.ns2d.system.passive.AssetSystem;
import net.mostlyoriginal.ns2d.system.passive.CameraSystem;

/**
 * @author Daan van Yperen
 */
@Wire
public class CostRenderSystem extends EntityProcessingSystem {

    private ComponentMapper<Pos> pm;
    private ComponentMapper<Anim> sm;
    private ComponentMapper<Buildable> bm;
    private ComponentMapper<Bounds> om;

    private static final Color HOLO_COLOR = Color.valueOf("73BCC9");

    private CameraSystem cameraSystem;
    private AssetSystem assetSystem;

    private SpriteBatch batch = new SpriteBatch();

    public CostRenderSystem() {
        super(Aspect.getAspectForAll(Pos.class, Anim.class, Bounds.class, Buildable.class));
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
        final Buildable buildable = bm.get(e);
        if ( !buildable.built )
        {
            final Bounds bounds = om.get(e);
            final Pos pos = pm.get(e);


            assetSystem.font.setColor(HOLO_COLOR);
            String cost = "" + buildable.resourceCost + "$";
            assetSystem.font.draw(batch, cost, pos.x + bounds.cx() - assetSystem.font.getBounds(cost).width/2, pos.y +  bounds.y2 + 20);
        }
    }
}
