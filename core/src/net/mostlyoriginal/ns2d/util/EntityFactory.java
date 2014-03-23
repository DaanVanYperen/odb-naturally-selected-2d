package net.mostlyoriginal.ns2d.util;

import com.artemis.Entity;
import com.artemis.World;
import net.mostlyoriginal.ns2d.G;
import net.mostlyoriginal.ns2d.component.*;

/**
 * @author Daan van Yperen
 */
public class EntityFactory {

    private static final int PLAYER_WEAPON_MOUNT_X = 19;
    private static final int PLAYER_WEAPON_MOUNT_Y = 13;
    private static final int WEAPON_ROT_ORIGIN_X = 11;
    private static final int WEAPON_ROT_ORIGIN_Y = 17;

    private static final int COST_INFANTRY_PORTAL = 25;
    private static final int COST_ARMORY = 15;
    private static final int COST_RESOURCETOWER = 10;

    public static Entity createPlayer(final World world, final float x, final float y) {

        Entity player = newPositioned(world, x, y)
                .addComponent(new Anim("player", Anim.Layer.PLAYER))
                .addComponent(new Physics())
                .addComponent(new Health(10))
                .addComponent(new RespawnOnDeath())
                .addComponent(new Gravity())
                .addComponent(new WallSensor())
                .addComponent(new Wallet(20))
                .addComponent(new PlayerControlled())
                .addComponent(new Bounds(G.CELL_SIZE, G.CELL_SIZE));
        return player;
    }



    public static Entity createSkulkHead(World world, float x, float y, Entity skulk) {
        Weapon weapon = new Weapon();
        weapon.bulletLifetime = 1/30f;
        weapon.fireCooldown = 1f;
        weapon.enemyGroup = "player-friend";
        final int originX = 5;
        final int originY = 3;
        final int mountX = 23;
        final int mountY = 13;
        return newPositioned(world, x, y)
                .addComponent(new Anim("skulk-head", Anim.Layer.PLAYER_ARM, originX, originY))
                .addComponent(new Attached(skulk, mountX - originX, mountY - originY))
                .addComponent(weapon)
                .addComponent(new Bounds(G.CELL_SIZE, G.CELL_SIZE));
    }

    public static Entity createSkulk(final World world, final float x, final float y) {

        Entity skulk = newPositioned(world, x, y)
                .addComponent(new Anim("skulk", Anim.Layer.ENEMIES))
                .addComponent(new Health(2))
                .addComponent(new Focus())
                .addComponent(new Physics())
                .addComponent(new Gravity())
                .addComponent(new WallSensor())
                .addComponent(new SkulkControlled())
                .addComponent(new Bounds(32, 17));

        Entity head = EntityFactory.createSkulkHead(world, x, y, skulk)
                .addComponent(new Aim());

        head.addToWorld();
        Inventory inventory = new Inventory();
        inventory.weapon = head;
        skulk.addComponent(inventory);

        return skulk;
    }

    private static Entity newPositioned(final World world, final float x, final float y) {
        return world.createEntity()
                .addComponent(new Pos(x, y));
    }

    public static Entity createDuct(World world, float x, float y) {
        return world.createEntity()
                .addComponent(new Pos(x, y))
                .addComponent(new Bounds(G.CELL_SIZE, G.CELL_SIZE))
                .addComponent(new EntitySpawner("skulk"))
                .addComponent(new Anim("duct", Anim.Layer.ON_WALL));
    }

    public static Entity createBullet(World world, float x, float y) {
        Physics physics = new Physics();
        physics.friction=0.01f;
        return newPositioned(world, x, y)
                .addComponent(new Anim("bullet", Anim.Layer.BULLETS))
                .addComponent(physics)
                .addComponent(new Gravity(-4f))
                .addComponent(new Bounds(7,4));
    }

    public static Entity createMouseCursor(World world, float x, float y) {
        return newPositioned(world, x, y)
                .addComponent(new MouseCursor());
    }

    public static Entity createResourceTower(World world, float x, float y) {
        return newPositioned(world, x, y)
                .addComponent(new Bounds(16 * 3, 16 * 3))
                .addComponent(new Harvester())
                .addComponent(new HealthIndicator())
                .addComponent(new Buildable("resourcetower", "resourcetower-unbuilt", COST_RESOURCETOWER))
                .addComponent(new Anim("resourcetower-unbuilt", Anim.Layer.DIRECTLY_BEHIND_PLAYER));
    }

    public static Entity createTechpoint(World world, float x, float y) {
        return newPositioned(world, x, y)
                .addComponent(new Bounds(64,64))
                .addComponent(new Health(100))
                .addComponent(new HealthIndicator())
                .addComponent(new Anim("techpoint", Anim.Layer.DIRECTLY_BEHIND_PLAYER));
    }

    public static Entity createSpawner(final World world, final float x, final float y) {
        return newPositioned(world, x, y)
                .addComponent(new Bounds(16, 16))
                .addComponent(new HealthIndicator())
                .addComponent(new Buildable("spawner", "spawner-unbuilt",COST_INFANTRY_PORTAL))
                .addComponent(new Anim("spawner-unbuilt", Anim.Layer.DIRECTLY_BEHIND_PLAYER));

    }

    public static Entity createArmory(World world, float x, float y) {
        return newPositioned(world, x, y)
                .addComponent(new Bounds(16 * 3, 16 * 3))
                .addComponent(new HealthIndicator())
                .addComponent(new Buildable("armory", "armory-unbuilt", COST_ARMORY))
                .addComponent(new Anim("armory-unbuilt", Anim.Layer.DIRECTLY_BEHIND_PLAYER));
    }



    public static Entity createShotgun(World world, float x, float y, Entity player) {
        Weapon weapon = new Weapon();
        weapon.fireCooldown = 0.25f;
        weapon.minBullets = 8;
        weapon.maxBullets = 10;
        weapon.spread = 20;
        weapon.bulletSpeed *= 0.9f;
        weapon.bulletAnimId = "slug";
        return newPositioned(world, x, y)
                .addComponent(new Anim("shotgun", Anim.Layer.PLAYER_ARM, WEAPON_ROT_ORIGIN_X, WEAPON_ROT_ORIGIN_Y))
                .addComponent(new Attached(player, PLAYER_WEAPON_MOUNT_X - WEAPON_ROT_ORIGIN_X, PLAYER_WEAPON_MOUNT_Y - WEAPON_ROT_ORIGIN_Y))
                .addComponent(weapon)
                .addComponent(new Bounds(G.CELL_SIZE, G.CELL_SIZE));
    }

    public static Entity createRifle(World world, float x, float y, Entity player) {
        Weapon weapon = new Weapon();
        weapon.recoil = 2;

        return newPositioned(world, x, y)
                .addComponent(new Anim("rifle", Anim.Layer.PLAYER_ARM, WEAPON_ROT_ORIGIN_X, WEAPON_ROT_ORIGIN_Y))
                .addComponent(new Attached(player, PLAYER_WEAPON_MOUNT_X - WEAPON_ROT_ORIGIN_X, PLAYER_WEAPON_MOUNT_Y - WEAPON_ROT_ORIGIN_Y))
                .addComponent(weapon)
                .addComponent(new Bounds(G.CELL_SIZE, G.CELL_SIZE));
    }

    public static Entity createGrenadeLauncher(World world, float x, float y, Entity player) {
        Weapon weapon = new Weapon();
        weapon.fireCooldown = 0.4f;
        weapon.minBullets = 1;
        weapon.maxBullets = 1;
        weapon.spread = 5;
        weapon.recoil *= 5;
        weapon.bulletSpeed *= 0.5f;
        weapon.bulletAnimId = "grenade";
        weapon.bulletFriction = 0.3f;
        weapon.bulletBounce = 0.8f;
        weapon.bulletPayload.radius = 50;
        weapon.bulletPayload.minDamage = weapon.bulletPayload.maxDamage = 5;
        return newPositioned(world, x, y)
                .addComponent(new Anim("grenadelauncher", Anim.Layer.PLAYER_ARM, WEAPON_ROT_ORIGIN_X, WEAPON_ROT_ORIGIN_Y))
                .addComponent(new Attached(player, PLAYER_WEAPON_MOUNT_X - WEAPON_ROT_ORIGIN_X, PLAYER_WEAPON_MOUNT_Y - WEAPON_ROT_ORIGIN_Y))
                .addComponent(weapon)
                .addComponent(new Bounds(G.CELL_SIZE, G.CELL_SIZE));
    }

}
