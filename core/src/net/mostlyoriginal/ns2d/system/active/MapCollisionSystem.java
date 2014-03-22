package net.mostlyoriginal.ns2d.system.active;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import net.mostlyoriginal.ns2d.component.Bounds;
import net.mostlyoriginal.ns2d.component.Physics;
import net.mostlyoriginal.ns2d.component.Pos;
import net.mostlyoriginal.ns2d.system.passive.MapSystem;
import net.mostlyoriginal.ns2d.util.MapMask;

/**
 * Constrain movement to map collision.
 *
 * Inteded to clamp physics calculations.
 *
 * @author Daan van Yperen
 */
@Wire
public class MapCollisionSystem extends EntityProcessingSystem {

    private MapSystem mapSystem;

    private boolean initialized;
    private MapMask solidMask;

    private ComponentMapper<Physics> ym;
    private ComponentMapper<Pos> pm;
    private ComponentMapper<Bounds> bm;

    public MapCollisionSystem() {
        super(Aspect.getAspectForAll(Physics.class, Pos.class, Bounds.class));
    }

    @Override
    protected void begin() {
        if ( !initialized )
        {
            initialized=true;
            solidMask = mapSystem.getMask("solid");
        }
    }

    @Override
    protected void process(Entity e) {
        final Physics physics = ym.get(e);
        final Pos pos = pm.get(e);
        final Bounds bounds = bm.get(e);

        float px = pos.x + physics.vx * world.delta;
        float py = pos.y + physics.vy * world.delta;

        if ( (physics.vx > 0 && solidMask.atScreen( px + bounds.x2, py + bounds.y1 + (bounds.y2 - bounds.y1) * 0.5f )) ||
             (physics.vx < 0 && solidMask.atScreen( px + bounds.x1, py + bounds.y1 + (bounds.y2 - bounds.y1) * 0.5f )) )
        {
            physics.vx = 0;
            px = pos.x;
        }

        if ( (physics.vy > 0 && solidMask.atScreen( px + bounds.x1 +  (bounds.x2 - bounds.x1) * 0.5f, py + bounds.y2 )) ||
             (physics.vy < 0 && solidMask.atScreen( px + bounds.x1 +  (bounds.x2 - bounds.x1) * 0.5f, py + bounds.y1 )) )
        {
            physics.vy = 0;
            py = pos.x;
        }

    }
}
