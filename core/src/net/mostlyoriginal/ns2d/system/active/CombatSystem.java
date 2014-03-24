package net.mostlyoriginal.ns2d.system.active;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.managers.GroupManager;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.math.MathUtils;
import net.mostlyoriginal.ns2d.api.PassiveSystem;
import net.mostlyoriginal.ns2d.component.*;
import net.mostlyoriginal.ns2d.system.render.UIStopwatchRenderSytem;

/**
 * @author Daan van Yperen
 */
@Wire
public class CombatSystem extends PassiveSystem {

    private ComponentMapper<Health> hm;
    private ComponentMapper<Buildable> bm;
    private ComponentMapper<RespawnOnDeath> rm;
    private ComponentMapper<Payload> pm;
    private ComponentMapper<Attached> am;
    private ComponentMapper<Pos> pom;
    private ComponentMapper<Bounds> bom;
    private ComponentMapper<Critical> cm;

    private BuildableSystem buildableSystem;
    private GroupManager groupManager;
    private AttachmentSystem attachmentSystem;
    private ParticleSystem particleSystem;
    private UIStopwatchRenderSytem uiStopwatchRenderSytem;

    @Override
    protected void initialize() {
        super.initialize();
    }

    public void damage(Entity victim, int damage) {
        if (hm.has(victim)) {
            Health health = hm.get(victim);

            health.damage += damage;
            if (am.has(victim)) {
                final Attached attached = am.get(victim);
                if (attached != null) {
                    attachmentSystem.push(attached, MathUtils.random(360), MathUtils.clamp(damage * 2, 1, 5));
                }
            }

            boolean dead = health.damage >= health.health;

            if ( health.woundParticle != null )
            {
                for ( int i=0, s=MathUtils.random( dead ? 6 : 1, dead ? 10 : 2); s>i; i++ )
                {
                    final Pos pos = pom.get(victim);
                    final Bounds bounds = bom.get(victim);
                    particleSystem.setRotation(MathUtils.random(20,160));
                    particleSystem.spawnParticle((int)(pos.x + bounds.cx()), (int)(pos.y + bounds.cy()), health.woundParticle );
                    particleSystem.setRotation(0);
                }
            }


            if (dead) {

                if (cm.has(victim))
                {
                    uiStopwatchRenderSytem.gameOver = true;
                }

                if (rm.has(victim)) {
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
        if (spawners.size() > 0) {
            Entity spawner = spawners.get(MathUtils.random(0, spawners.size() - 1));
            Pos spawnerPos = spawner.getComponent(Pos.class);
            Pos playerPos = victim.getComponent(Pos.class);
            playerPos.x = spawnerPos.x - 8;
            playerPos.y = spawnerPos.y + 6;
        }

    }
}
