package net.mostlyoriginal.ns2d.system.collide;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.managers.GroupManager;
import com.artemis.managers.TagManager;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import net.mostlyoriginal.ns2d.component.Payload;
import net.mostlyoriginal.ns2d.component.Pos;
import net.mostlyoriginal.ns2d.component.Wallet;
import net.mostlyoriginal.ns2d.system.active.CameraShakeSystem;
import net.mostlyoriginal.ns2d.system.active.CombatSystem;
import net.mostlyoriginal.ns2d.system.active.ParticleSystem;
import net.mostlyoriginal.ns2d.system.passive.CollisionSystem;
import net.mostlyoriginal.ns2d.util.EntityUtil;

/**
 * @author Daan van Yperen
 */
@Wire
public class BulletCollisionSystem extends EntityProcessingSystem {

    private CameraShakeSystem cameraShakeSystem;
    private ComponentMapper<Payload> pm;
    private ComponentMapper<Pos> om;
    private ComponentMapper<Wallet> wm;
    private GroupManager groupManager;
    private CollisionSystem collisionSystem;
    private CombatSystem combatSystem;
    private ParticleSystem particleSystem;
    private TagManager tagManager;


    public BulletCollisionSystem() {
        super(Aspect.getAspectForAll(Payload.class));
    }

    @Override
    protected void process(Entity bullet) {


        final Payload payload = pm.get(bullet);

        payload.age += world.delta;
        if ( payload.age >= payload.maxLifetime )
        {
            if ( payload.radius > 0 )
            {
                damageArea(bullet, payload.triggerGroup, payload.radius,MathUtils.random(payload.minDamage, payload.maxDamage) );
            }
            bullet.deleteFromWorld();
            return;
        }

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

        switch ( payload.type )
        {

            case RESOURCE:
                Entity player = tagManager.getEntity("player");
                if ( wm.has(player) ) wm.get(player).resources += damage;
                break;
            case EXPLOSIVE:
                if ( radius == 0 )
                {
                    //cameraShakeSystem.shake(1);
                    combatSystem.damage(victim, damage);
                } else {
                    damageArea(bullet, payload.triggerGroup, radius, damage);
                }
                break;
        }

    }

    Vector2 vTmp = new Vector2();

    private void damageArea(Entity bullet, String groupId, float radius, int damage) {

        Payload payload = pm.get(bullet);
        Pos pos = om.get(bullet);

        cameraShakeSystem.shake( 1 + radius / 20);
        particleSystem.spawnParticle((int) pos.x, (int) pos.y, "explosion"); 
        for ( int i=0, s=MathUtils.random(3,5); i<s; i++ ) {
            vTmp.set(MathUtils.random(0,radius),0).rotate(MathUtils.random(0,360)).add(pos.x,pos.y);
            particleSystem.spawnParticle(
                    (int)vTmp.x, (int)vTmp.y, "tiny-explosion");
        }

        final ImmutableBag<Entity> targets = groupManager.getEntities(groupId);
        for (int i = 0, s = targets.size(); s > i; i++) {
            final Entity victim = targets.get(i);
            if ( victim != null && EntityUtil.distance2(bullet, victim) <= radius*radius) {
                combatSystem.damage(victim, damage);
            }
        }
    }
}
