package net.mostlyoriginal.ns2d.system.active;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.MathUtils;
import net.mostlyoriginal.ns2d.component.Bounds;
import net.mostlyoriginal.ns2d.component.EntitySpawner;
import net.mostlyoriginal.ns2d.component.Pos;
import net.mostlyoriginal.ns2d.util.EntityFactory;

/**
 * @author Daan van Yperen
 */
@Wire
public class EntitySpawnerSystem extends EntityProcessingSystem {

    private ComponentMapper<EntitySpawner> sm;
    private ComponentMapper<Pos> pm;
    private ComponentMapper<Bounds> bm;

    public EntitySpawnerSystem() {
        super(Aspect.getAspectForAll(EntitySpawner.class, Pos.class, Bounds.class));
    }

    public void spawnEntity(float x, float y, MapProperties properties) {

        final String entity = (String) properties.get("entity");

        spawnEntity(x, y, entity);
    }

    private void spawnEntity(float x, float y, String entity) {
        switch (entity) {
            case "player":
                EntityFactory.createPlayer(world, x, y).addToWorld();
                break;
            case "spawner":
                EntityFactory.createSpawner(world, x, y).addToWorld();
                break;
            case "duct":
                EntityFactory.createDuct(world, x, y).addToWorld();
                break;
            case "skulk":
                EntityFactory.createSkulk(world, x, y).addToWorld();
                break;
            default:
                throw new RuntimeException("No idea how to spawn entity of type " + entity);
        }
    }


    @Override
    protected void process(Entity e) {
        final EntitySpawner spawner = sm.get(e);

        if ( spawner.cooldown == -1 ) {
            scheduleSpawn(spawner);
        }

        spawner.cooldown -= world.delta;
        if (spawner.cooldown <= 0) {
            final Pos pos = pm.get(e);
            final Bounds bounds = bm.get(e);

            scheduleSpawn(spawner);

            for (int i = 0, s = MathUtils.random(spawner.minCount, spawner.maxCount); i < s; i++) {
                spawnEntity(pos.x + bounds.cx(), pos.y + bounds.cy(), spawner.entityId);
            }
        }
    }

    private void scheduleSpawn(EntitySpawner spawner) {
        spawner.cooldown = MathUtils.random(spawner.minInterval, spawner.maxInterval);
    }
}
