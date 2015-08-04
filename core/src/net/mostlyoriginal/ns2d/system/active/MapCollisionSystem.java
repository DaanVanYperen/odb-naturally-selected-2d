package net.mostlyoriginal.ns2d.system.active;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;

import net.mostlyoriginal.api.utils.MapMask;
import net.mostlyoriginal.ns2d.component.Anim;
import net.mostlyoriginal.ns2d.component.Bounds;
import net.mostlyoriginal.ns2d.component.Physics;
import net.mostlyoriginal.ns2d.component.Pos;
import net.mostlyoriginal.ns2d.component.Terminal;
import net.mostlyoriginal.ns2d.system.passive.MapSystem;

/**
 * Constrain movement to map collision.
 * <p/>
 * Inteded to clamp physics calculations.
 *
 * @author Daan van Yperen
 */
@Wire
public final class MapCollisionSystem extends EntityProcessingSystem {
    private static boolean DEBUG = false;

    private MapSystem mapSystem;

    private boolean initialized;
    private MapMask solidMask;

    private ComponentMapper<Physics> ym;
    private ComponentMapper<Pos> pm;
    private ComponentMapper<Bounds> bm;

    public MapCollisionSystem() {
        super(Aspect.all(Physics.class, Pos.class, Bounds.class));
    }

    @Override
    protected void begin() {
        if (!initialized) {
            initialized = true;
            solidMask = mapSystem.getMask("solid");
        }
    }

    @Override
    protected void end() {
    }

    @Override
    protected void process(Entity e) {
        final Physics physics = ym.get(e);
        final Pos pos = pm.get(e);
        final Bounds bounds = bm.get(e);

        // no math required here.
        if (physics.vx != 0 || physics.vy != 0) {

            float px = pos.x + physics.vx * world.delta;
            float py = pos.y + physics.vy * world.delta;

            if ((physics.vx > 0 && collides(px + bounds.x2, py + bounds.y1 + (bounds.y2 - bounds.y1) * 0.5f)) ||
                    (physics.vx < 0 && collides(px + bounds.x1, py + bounds.y1 + (bounds.y2 - bounds.y1) * 0.5f))) {
                physics.vx = physics.bounce > 0 ? -physics.vx * physics.bounce : 0;
                px = pos.x;
            }

            if ((physics.vy > 0 && collides(px + bounds.x1 + (bounds.x2 - bounds.x1) * 0.5f, py + bounds.y2)) ||
                    (physics.vy < 0 && collides(px + bounds.x1 + (bounds.x2 - bounds.x1) * 0.5f, py + bounds.y1))) {
                physics.vy = physics.bounce > 0 ? -physics.vy * physics.bounce : 0;
            }

        }

    }

    private boolean collides(final float x, final float y) {
        if (DEBUG) {
            world.createEntity()
                    .edit()
                    .add(new Pos(x - 1, y - 1))
                    .add(new Anim("debug-marker"))
                    .add(new Terminal(1));
        }

        return solidMask.atScreen(x, y, false);
    }
}
