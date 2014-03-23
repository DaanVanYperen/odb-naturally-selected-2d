package net.mostlyoriginal.ns2d.system.active;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.managers.GroupManager;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.math.MathUtils;
import net.mostlyoriginal.ns2d.api.PassiveSystem;
import net.mostlyoriginal.ns2d.component.*;

/**
 * @author Daan van Yperen
 */
@Wire
public class CombatSystem extends PassiveSystem {

    private ComponentMapper<Health> hm;
    private ComponentMapper<Buildable> bm;
    private ComponentMapper<RespawnOnDeath> rm;

    private BuildableSystem buildableSystem;
    private GroupManager groupManager;

    public void damage(Entity victim, Entity attacker, int damage) {
        if (hm.has(victim)) {
            Health health = hm.get(victim);

            health.damage += damage;

            if (health.damage >= health.health) {

                if ( rm.has(victim) )
                {
                    respawnEntity(victim, health);
                    return;
                }

                victim.removeComponent(Health.class).changedInWorld();

                if (bm.has(victim)) {
                    // unbuild.
                    buildableSystem.destroyBuildable(victim);
                } else {
                    // kill
                    victim.deleteFromWorld();
                }
            }
        }
    }

    private void respawnEntity(Entity victim, Health health) {

        final RespawnOnDeath respawnOnDeath = rm.get(victim);

        health.damage = 0;

        // freeze in place for X seconds.

        victim.addComponent(new Script()
                .remove(Health.class)
                .add(new Frozen())
                .wait(2f)
                .remove(Frozen.class)
                .add(new Health(10)))
                .changedInWorld();


        victim.getComponent(Anim.class).id = "player-respawning";

        // move to spawner.
        ImmutableBag<Entity> spawners = groupManager.getEntities("spawner");
        if ( spawners.size()  > 0 )
        {
            Entity spawner = spawners.get(MathUtils.random(0, spawners.size() - 1));
            Pos spawnerPos = spawner.getComponent(Pos.class);
            Pos playerPos = victim.getComponent(Pos.class);
            playerPos.x = spawnerPos.x;
            playerPos.y = spawnerPos.y;
        }

    }
}
