package net.mostlyoriginal.ns2d.system.active;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import net.mostlyoriginal.ns2d.component.Anim;
import net.mostlyoriginal.ns2d.component.Physics;
import net.mostlyoriginal.ns2d.component.Pos;
import net.mostlyoriginal.ns2d.component.WallSensor;

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
    private ComponentMapper<Anim> am;
    private ComponentMapper<WallSensor> wm;


    public AfterPhysicsSystem() {
        super(Aspect.getAspectForAll(Physics.class, Pos.class));
    }

    @Override
    protected void process(Entity e) {
        final Physics physics = ym.get(e);
        final Pos pos = pm.get(e);

        pos.x += physics.vx * world.getDelta();
        pos.y += physics.vy * world.getDelta();

        if ( physics.vr != 0 && am.has(e))
        {
            am.get(e).rotation += physics.vr * world.delta;
        }
        
        if (physics.friction != 0) {
            float adjustedFriction = physics.friction * (wm.has(e) && !wm.get(e).onFloor ? 0.25f : 1 );

            if (Math.abs(physics.vx) > 0.005f) {
                physics.vx = physics.vx - (physics.vx * world.delta * adjustedFriction);
            } else {
                physics.vx = 0;
            }

            if (Math.abs(physics.vr) > 0.005f) {
                physics.vr = physics.vr - (physics.vr * world.delta * adjustedFriction);
            } else {
                physics.vr = 0;
            }

            if (Math.abs(physics.vy) > 0.005f) {
                physics.vy = physics.vy - (physics.vy * world.delta * adjustedFriction);
            } else {
                physics.vy = 0;
            }
        }
       
    }
}
