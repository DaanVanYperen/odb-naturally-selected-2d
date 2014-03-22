package net.mostlyoriginal.ns2d.system.active;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import net.mostlyoriginal.ns2d.component.Gravity;
import net.mostlyoriginal.ns2d.component.Physics;

/**
 * @author Daan van Yperen
 */
@Wire
public class GravitySystem extends EntityProcessingSystem {

    ComponentMapper<Physics> pm;
    ComponentMapper<Gravity> gm;

    public GravitySystem()
    {
        super(Aspect.getAspectForAll(Gravity.class, Physics.class));
    }

    @Override
    protected void process(Entity e) {
        final Physics physics = pm.get(e);
        final Gravity gravity = gm.get(e);

        physics.vy += gravity.y * world.delta;
        physics.vx += gravity.x * world.delta;
    }
}
