package net.mostlyoriginal.ns2d.system.active;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.managers.GroupManager;
import com.artemis.managers.TagManager;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.MathUtils;
import net.mostlyoriginal.ns2d.component.*;
import net.mostlyoriginal.ns2d.util.EntityFactory;

/**
 * @author Daan van Yperen
 */
@Wire
public class EntitySpawnerSystem extends EntityProcessingSystem {

    private ComponentMapper<EntitySpawner> sm;
    private ComponentMapper<Pos> pm;
    private ComponentMapper<Bounds> bm;

    private GroupManager groupManager;
    private TagManager tagManager;

    public EntitySpawnerSystem() {
        super(Aspect.getAspectForAll(EntitySpawner.class, Pos.class, Bounds.class));
    }

    public void spawnEntity(float x, float y, MapProperties properties) {

        final String entity = (String) properties.get("entity");

        spawnEntity(x, y, entity);
    }

    public void spawnEntity(float x, float y, String entity) {
        switch (entity) {
            case "player":
                assemblePlayer(x, y);
                break;
            case "resourcetower":
                Entity resourceTower = EntityFactory.createResourceTower(world, x, y);
                groupManager.add(resourceTower, "player-structure");
                resourceTower.addToWorld();
                break;
            case "techpoint":
                final Entity techpoint = EntityFactory.createTechpoint(world, x, y);
                groupManager.add(techpoint, "player-structure");
                techpoint.addToWorld();
                break;
            case "spawner":
                final Entity spawner = EntityFactory.createSpawner(world, x, y);
                groupManager.add(spawner, "player-structure");
                spawner.addToWorld();
                break;
            case "duct":
                EntityFactory.createDuct(world, x, y).addToWorld();
                break;
            case "skulk":
                Entity skulk = EntityFactory.createSkulk(world, x, y);
                groupManager.add(skulk, "enemy");
                skulk.addToWorld();
                break;
            default:
                throw new RuntimeException("No idea how to spawn entity of type " + entity);
        }
    }

    private void assemblePlayer(float x, float y) {
        Entity player = EntityFactory.createPlayer(world, x, y);
        player.addToWorld();

        Entity mouseCursor = EntityFactory.createMouseCursor(world, x, y);
        mouseCursor.addToWorld();

        // create an absolute tracker in between the player and the cursor that we will follow with the camera.
        Inbetween inbetween = new Inbetween(player, mouseCursor);
        inbetween.tween = 0.4f;
        Entity midpoint = world.createEntity()
                .addComponent(new Pos(0, 0))
                .addComponent(inbetween);
        midpoint.addToWorld();

        // now create a drone that will swerve towards the tracker which contains the camera. this will create a smooth moving camera.
        world.createEntity()
                .addComponent(new Pos(0, 0))
                .addComponent(new Physics())
                .addComponent(new Homing(midpoint))
                .addComponent(new CameraFocus())
                .addToWorld();

                EntityFactory.createRifle(world, x, y, player).addComponent(new Aim(mouseCursor)).addToWorld();
        tagManager.register("player", player);
    }


    @Override
    protected void process(Entity e) {
        final EntitySpawner spawner = sm.get(e);

        if ( spawner.cooldown == -1 ) {

            scheduleSpawn(spawner);
            spawner.cooldown /= 4;
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
