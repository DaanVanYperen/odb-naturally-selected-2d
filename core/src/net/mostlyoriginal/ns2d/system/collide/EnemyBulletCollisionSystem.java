package net.mostlyoriginal.ns2d.system.collide;

import com.artemis.Entity;
import com.artemis.annotations.Wire;
import net.mostlyoriginal.ns2d.api.DualEntityProcessSystem;
import net.mostlyoriginal.ns2d.system.active.CombatSystem;
import net.mostlyoriginal.ns2d.system.passive.CollisionSystem;

/**
 * @author Daan van Yperen
 */
@Wire
public class EnemyBulletCollisionSystem extends DualEntityProcessSystem {

    private CollisionSystem collisionSystem;
    private CombatSystem combatSystem;

    public EnemyBulletCollisionSystem() {
        super("enemy-bullet", "player-friend");
    }

    @Override
    protected void processEntity(Entity entityA, Entity entityB) {
        if ( collisionSystem.overlaps(entityA, entityB) )
        {
            combatSystem.damage(entityB, entityA, 1);
            entityA.deleteFromWorld();
        }
    }
}
