package net.mostlyoriginal.ns2d.system.active;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.math.Vector2;
import net.mostlyoriginal.ns2d.component.Homing;
import net.mostlyoriginal.ns2d.component.Physics;
import net.mostlyoriginal.ns2d.component.Pos;

/**
 * @author Daan van Yperen
 */
@Wire
public class HomingSystem extends EntityProcessingSystem {

    ComponentMapper<Homing> hm;
    ComponentMapper<Pos> pm;
    ComponentMapper<Physics> ym;

    PhysicsSystem physicsSystem;

    public HomingSystem() {
        super(Aspect.getAspectForAll(Homing.class, Pos.class, Physics.class));
    }


    Vector2 tmp = new Vector2();

    @Override
    protected void process(Entity e) {

        Homing homing = hm.get(e);

        if ( homing.target != null && homing.target.isActive() )
        {
            Pos myPos = pm.get(e);
            Pos tPos = pm.get(homing.target);

            // vector of required traversal
            tmp.set(tPos.x, tPos.y).sub(myPos.x,myPos.y).scl(homing.speedFactor).clamp(0, homing.maxVelocity);

            Physics physics = ym.get(e);
            physics.vx = tmp.x;
            physics.vy = tmp.y;

        } else homing.target = null;
    }
}
