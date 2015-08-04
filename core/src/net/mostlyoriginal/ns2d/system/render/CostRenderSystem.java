package net.mostlyoriginal.ns2d.system.render;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.managers.TagManager;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Pools;

import net.mostlyoriginal.ns2d.component.*;
import net.mostlyoriginal.ns2d.system.passive.AssetSystem;
import net.mostlyoriginal.ns2d.system.passive.CameraSystem;
import net.mostlyoriginal.ns2d.system.passive.CollisionSystem;

/**
 * @author Daan van Yperen
 */
@Wire
public class CostRenderSystem extends EntityProcessingSystem {

    private ComponentMapper<Pos> pm;
    private ComponentMapper<Anim> sm;
    private ComponentMapper<Buildable> bm;
    private ComponentMapper<Bounds> om;
    private ComponentMapper<Wallet> wm;

    private static final Color HOLO_COLOR = Color.valueOf("73BCC9");
    private static final Color HOLO_COLOR_RED = Color.valueOf("FF7799");

    private CameraSystem cameraSystem;
    private AssetSystem assetSystem;
    private TagManager tagManager;

    private SpriteBatch batch = new SpriteBatch();
    private int walletCash;
    private CollisionSystem collisionSystem;
    public Entity player;

    public CostRenderSystem() {
        super(Aspect.all(Pos.class, Anim.class, Bounds.class, Buildable.class));
    }

    @Override
    protected void begin() {
        batch.setProjectionMatrix(cameraSystem.camera.combined);
        batch.begin();
        batch.setColor(1f, 1f, 1f, 1f);

        player = tagManager.getEntity("player");
        walletCash = wm.has(player) ? wm.get(player).resources : 0;

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

            boolean affordable = buildable.resourceCost <= walletCash;
            assetSystem.font.setColor(affordable ? HOLO_COLOR : HOLO_COLOR_RED );
            String cost = "" + buildable.resourceCost + "$";
            GlyphLayout layout = Pools.obtain(GlyphLayout.class);
            layout.setText(assetSystem.font, cost);
            assetSystem.font.draw(batch, cost, pos.x + bounds.cx() - layout.width/2, pos.y +  bounds.y2 + 20);

            if ( collisionSystem.overlaps(player, e) && affordable )
            {
                String msg = "'e' to purchase";
                assetSystem.font.draw(batch, msg, pos.x + bounds.cx() - layout.width/2, pos.y +  bounds.y2 + 32);
            }
            Pools.free(layout);
        }
    }
}
