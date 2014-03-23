package net.mostlyoriginal.ns2d.system.active;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.math.Vector2;
import net.mostlyoriginal.ns2d.api.PassiveSystem;
import net.mostlyoriginal.ns2d.component.Physics;

/**
 * @author Daan van Yperen
 */
@Wire
public class PhysicsSystem extends PassiveSystem {

    private ComponentMapper<Physics> ym;

    private Vector2 vTmp = new Vector2();

    public void push(Entity entity, float rotation, float force) {
        if (ym.has(entity)) {
            vTmp.set(force, 0).setAngle(rotation);
            final Physics physics = ym.get(entity);
            physics.vx += vTmp.x;
            physics.vy += vTmp.y;
        }
    }

    public void clampVelocity(Entity entity, float minSpeed, float maxSpeed) {
        if (ym.has(entity)) {
            final Physics physics = ym.get(entity);
            vTmp.set(physics.vx, physics.vy).clamp(minSpeed,maxSpeed);
        }
    }

}
