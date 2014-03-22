package net.mostlyoriginal.ns2d.system.active;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import net.mostlyoriginal.ns2d.api.PassiveSystem;
import net.mostlyoriginal.ns2d.component.Buildable;
import net.mostlyoriginal.ns2d.component.Health;

/**
 * @author Daan van Yperen
 */
@Wire
public class CombatSystem extends PassiveSystem {

    private ComponentMapper<Health> hm;
    private ComponentMapper<Buildable> bm;

    private BuildableSystem buildableSystem;

    public void damage(Entity victim, Entity attacker, int damage) {
        if (hm.has(victim)) {
            Health health = hm.get(victim);

            health.damage += damage;

            if (health.damage >= health.health) {
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
}
