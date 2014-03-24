package net.mostlyoriginal.ns2d.system.active;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.managers.GroupManager;
import com.artemis.systems.VoidEntitySystem;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.math.MathUtils;
import net.mostlyoriginal.ns2d.component.EntitySpawner;

/**
 * Tries to make an interesting fight for the player.
 *
 * @author Daan van Yperen
 */
@Wire
public class DirectorSystem extends VoidEntitySystem {

    private ComponentMapper<EntitySpawner> sm;

    GroupManager groupManager;
    private ImmutableBag<Entity> ducts;
    private float nextStageCooldown = 0;

    @Override
    protected void initialize() {
        super.initialize();
        ducts = groupManager.getEntities("duct");
    }

    @Override
    protected void processSystem() {
        nextStageCooldown -= world.delta;
        if ( nextStageCooldown <= 0 )
        {
            nextStageCooldown = 20;
            activateRandomSpawners(1);
        }
    }

    private void activateRandomSpawners(int activeCount) {
        // disable all spawners.
        for (int i = 0; ducts.size() > i; i++) {
            final Entity spawner = ducts.get(i);
            if (spawner != null) {
                final EntitySpawner entitySpawner = sm.get(spawner);
                entitySpawner.enabled = false;
                entitySpawner.cooldown = 5;
            }
        }

        for (int c = 0; c < activeCount; c++) {
            Entity spawner = ducts.get(MathUtils.random(0, ducts.size() - 1));
            if (spawner != null) {
                final EntitySpawner entitySpawner = sm.get(spawner);
                entitySpawner.enabled = true;
                entitySpawner.cooldown = 5;
            }
        }
    }

}
