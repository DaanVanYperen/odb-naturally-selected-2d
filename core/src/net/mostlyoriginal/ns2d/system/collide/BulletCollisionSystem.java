package net.mostlyoriginal.ns2d.system.collide;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.managers.GroupManager;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.math.MathUtils;
import net.mostlyoriginal.ns2d.component.Payload;
import net.mostlyoriginal.ns2d.system.active.CameraShakeSystem;
import net.mostlyoriginal.ns2d.system.active.CombatSystem;
import net.mostlyoriginal.ns2d.system.passive.CollisionSystem;
import net.mostlyoriginal.ns2d.util.EntityUtil;

/**
 * @author Daan van Yperen
 */
@Wire
public class BulletCollisionSystem extends EntityProcessingSystem {

    private CameraShakeSystem cameraShakeSystem;
    private ComponentMapper<Payload> pm;
    private GroupManager groupManager;
    private CollisionSystem collisionSystem;
    private CombatSystem combatSystem;

    public BulletCollisionSystem() {
        super(Aspect.getAspectForAll(Payload.class));
    }

    @Override
    protected void process(Entity bullet) {
        final Payload payload = pm.get(bullet);
        final ImmutableBag<Entity> targets = groupManager.getEntities(payload.triggerGroup);
        for (int i = 0, s = targets.size(); s > i; i++) {
            final Entity victim = targets.get(i);
            if ( victim != null && collisionSystem.overlaps(bullet, victim)) {
                triggerPayload(victim, bullet);
                bullet.deleteFromWorld();
                return;
            }
        }
    }

    private void triggerPayload(Entity victim, Entity bullet) {

        final Payload payload = pm.get(bullet);

        int damage = MathUtils.random(payload.minDamage, payload.maxDamage);
        float radius = payload.radius;

        if ( radius == 0 )
        {
            cameraShakeSystem.shake(1);
            combatSystem.damage(victim, damage);
        } else {
            cameraShakeSystem.shake( 1 + radius / 20);
            damageArea(bullet, payload.triggerGroup, radius, damage);
        }
    }

    private void damageArea(Entity bullet, String groupId, float radius, int damage) {

        final ImmutableBag<Entity> targets = groupManager.getEntities(groupId);
        for (int i = 0, s = targets.size(); s > i; i++) {
            final Entity victim = targets.get(i);
            if ( victim != null && EntityUtil.distance2(bullet, victim) <= radius*radius) {
                combatSystem.damage(victim, damage);
            }
        }
    }
}
