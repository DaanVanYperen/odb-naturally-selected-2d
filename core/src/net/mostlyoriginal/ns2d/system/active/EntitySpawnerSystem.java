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
public final class EntitySpawnerSystem extends EntityProcessingSystem {
    private ComponentMapper<EntitySpawner> sm;
    private ComponentMapper<Pos> pm;
    private ComponentMapper<Bounds> bm;
    private ComponentMapper<Anim> am;
    private ComponentMapper<Inventory> im;

    private GroupManager groupManager;
    private TagManager tagManager;
    private CombatSystem combatSystem;

    public EntitySpawnerSystem() {
        super(Aspect.all(EntitySpawner.class, Pos.class, Bounds.class));
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
            groupManager.add(resourceTower, "player-friend");
            break;
        case "armory":
            Entity armory = EntityFactory.createArmory(world, x, y);
            groupManager.add(armory, "player-structure");
            groupManager.add(armory, "player-friend");
            break;
        case "techpoint":
            final Entity techpoint = EntityFactory.createTechpoint(world, x, y);
            groupManager.add(techpoint, "player-structure");
            groupManager.add(techpoint, "player-friend");
            break;
        case "spawner":
            final Entity spawner = EntityFactory.createSpawner(world, x, y);
            groupManager.add(spawner, "player-structure");
            groupManager.add(spawner, "player-friend");
            groupManager.add(spawner, "spawner");
            break;
        case "duct":
            Entity duct = EntityFactory.createDuct(world, x, y);
            groupManager.add(duct, "duct");
            break;
        case "sentry":
            final Entity sentry = EntityFactory.createSentry(world, x, y);
            groupManager.add(sentry, "player-structure");
            groupManager.add(sentry, "player-friend");
            break;
        case "sentry2":
            final Entity sentry2 = EntityFactory.createSentry2(world, x, y);
            groupManager.add(sentry2, "player-structure");
            groupManager.add(sentry2, "player-friend");
            break;
        case "skulk":
            Entity skulk = EntityFactory.createSkulk(world, x, y);
            groupManager.add(skulk, "enemy");
            break;
        case "babbler":
            Entity babbler = EntityFactory.createBabbler(world, x, y, tagManager.getEntity("player"));
            groupManager.add(babbler, "enemy");
            break;
        case "gorge":
            Entity gorge = EntityFactory.createGorge(world, x, y);
            groupManager.add(gorge, "enemy");
            break;
        default:
            throw new RuntimeException("No idea how to spawn entity of type " + entity);
        }
    }

    public void giveWeapon(Entity entity, String type) {
        Inventory inventory = im.get(entity);

        if (inventory.weapon != null) {
            inventory.weapon.deleteFromWorld();
            inventory.weapon = null;
        }

        Entity weapon = null;
        switch (type) {
        case "rifle":
            weapon = EntityFactory.createRifle(world, 0, 0, entity);
            break;
        case "shotgun":
            weapon = EntityFactory.createShotgun(world, 0, 0, entity);
            break;
        case "grenadelauncher":
            weapon = EntityFactory.createGrenadeLauncher(world, 0, 0, entity);
            break;
        case "flamethrower":
            weapon = EntityFactory.createFlamethrower(world, 0, 0, entity);
            break;
        }
        if (weapon != null) {
            inventory.weapon = weapon.edit().add(new Aim(tagManager.getEntity("cursor"))).getEntity();
        }
    }

    private void assemblePlayer(float x, float y) {
        Entity player = EntityFactory.createPlayer(world, x, y);

        groupManager.add(player, "player-friend");

        Entity mouseCursor = EntityFactory.createMouseCursor(world, x, y);
        tagManager.register("cursor", mouseCursor);

        // create an absolute tracker in between the player and the cursor that we will follow with the camera.
        final Inbetween inbetween = new Inbetween(player, mouseCursor);
        inbetween.tween = 0.4f;
        Entity midpoint = world.createEntity()
                .edit()
                .add(new Pos(0, 0))
                .add(inbetween)
                .getEntity();

        // now create a drone that will swerve towards the tracker which contains the camera. this will create a smooth moving camera.
        world.createEntity()
                .edit()
                .add(new Pos(0, 0))
                .add(new Physics())
                .add(new Homing(midpoint))
                .add(new CameraFocus()).getEntity();

        Inventory inventory = new Inventory();
        player.edit().add(inventory);

        giveWeapon(player, "rifle");

        tagManager.register("player", player);
        groupManager.add(player, "player");

        combatSystem.respawnEntity(player);
    }

    @Override
    protected void process(Entity e) {
        final EntitySpawner spawner = sm.get(e);

        if (am.has(e)) {
            Anim anim = am.get(e);
            anim.id = spawner.enabled ? "duct-hot" : "duct";
        }

        if (!spawner.enabled) {
            return;
        }

        if (spawner.cooldown == -1) {
            scheduleSpawn(spawner);
            spawner.cooldown /= 4;
        }

        spawner.cooldown -= world.delta;
        if (spawner.cooldown <= 0) {
            final Pos pos = pm.get(e);
            final Bounds bounds = bm.get(e);

            scheduleSpawn(spawner);

            for (int i = 0, s = MathUtils.random(spawner.minCount, spawner.maxCount); i < s; i++) {
                spawnEntity(pos.x + bounds.cx(), pos.y + bounds.cy(), MathUtils.random(100) < spawner.entityId2Chance ? spawner.entityId2 : spawner.entityId);
            }
        }
    }

    private void scheduleSpawn(EntitySpawner spawner) {
        spawner.cooldown = MathUtils.random(spawner.minInterval, spawner.maxInterval);
    }
}
