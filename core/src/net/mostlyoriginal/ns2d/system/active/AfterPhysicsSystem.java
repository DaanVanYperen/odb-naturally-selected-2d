package net.mostlyoriginal.ns2d.system.active;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import net.mostlyoriginal.ns2d.component.Physics;
import net.mostlyoriginal.ns2d.component.Pos;

/**
 * Applies physics calculations. Must be run after physics clamps.
 *
 * @author Daan van Yperen
 */

@Wire
public class AfterPhysicsSystem extends EntityProcessingSystem {

    MapCollisionSystem mapCollisionSystem;

    private ComponentMapper<Physics> ym;
    private ComponentMapper<Pos> pm;


    public AfterPhysicsSystem() {
        super(Aspect.getAspectForAll(Physics.class, Pos.class));
    }

    @Override
    protected void process(Entity e) {
        final Physics physics = ym.get(e);
        final Pos pos = pm.get(e);

        pos.x += physics.vx * world.getDelta();
        pos.y += physics.vy * world.getDelta();
    }
}
