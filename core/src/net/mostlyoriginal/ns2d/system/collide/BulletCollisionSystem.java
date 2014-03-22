package net.mostlyoriginal.ns2d.system.collide;

import com.artemis.Entity;
import com.artemis.annotations.Wire;
import net.mostlyoriginal.ns2d.api.DualEntityProcessSystem;
import net.mostlyoriginal.ns2d.system.passive.CollisionSystem;

/**
 * @author Daan van Yperen
 */
@Wire
public class BulletCollisionSystem extends DualEntityProcessSystem {

    CollisionSystem collisionSystem;

    public BulletCollisionSystem() {
        super("bullet", "enemy");
    }

    @Override
    protected void processEntity(Entity entityA, Entity entityB) {
        if ( collisionSystem.overlaps(entityA, entityB) )
        {
            entityA.deleteFromWorld();
            entityB.deleteFromWorld();
        }
    }
}
