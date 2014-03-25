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
    private static final int COST_SENTRY = 5;

    public static Entity createPlayer(final World world, final float x, final float y) {

        Entity player = newPositioned(world, x, y)
                .addComponent(new Anim("player-idle", Anim.Layer.PLAYER))
                .addComponent(new Physics())
                .addComponent(new Health(10))
                .addComponent(new RespawnOnDeath())
                .addComponent(new Gravity())
                .addComponent(new WallSensor())
                .addComponent(new Wallet(12))
                .addComponent(new PlayerControlled())
                .addComponent(new Bounds(G.CELL_SIZE, G.CELL_SIZE));
        return player;
    }

    public static Entity createBabbler(World world, float x, float y, Entity target) {
        Weapon weapon = new Weapon();
        weapon.bulletPayload.maxLifetime = 1/30f;
        weapon.fireCooldown = 1f;
        weapon.enemyGroup = "player-friend";
        weapon.muzzleFlare = false;
        final int originX = 5;
        final int originY = 3;
        final int mountX = 23;
        final int mountY = 13;

        Homing homing = new Homing(target);
        homing.maxDistance = 300;
        homing.maxVelocity = 1000;
        homing.speedFactor = 4;

        Health health = new Health(1);
        health.woundParticle = "alienblood";
        health.deathSfxId = new String[] {"ns2d_sfx_skulk_die1","ns2d_sfx_skulk_die2","ns2d_sfx_skulk_die3"};
        return newPositioned(world, x, y)
                .addComponent(new Anim("babbler", Anim.Layer.PLAYER_ARM, originX, originY))
                .addComponent(weapon)
                .addComponent(health)
                .addComponent(new Focus())
                .addComponent(new Physics())
                .addComponent(new Gravity())
                .addComponent(new WallSensor())
                .addComponent(new Aim())
                .addComponent(homing)
                .addComponent(new SkulkControlled())
                .addComponent(new Bounds(32, 17));
    }

    public static Entity createSkulkHead(World world, float x, float y, Entity skulk) {
        Weapon weapon = new Weapon();
        weapon.bulletPayload.maxLifetime = 1/30f;
        weapon.fireCooldown = 1f;
        weapon.enemyGroup = "player-friend";
        weapon.muzzleFlare = false;
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

        Health health = new Health(1);
        health.woundParticle = "alienblood";
        health.deathSfxId = new String[] {"ns2d_sfx_skulk_die1","ns2d_sfx_skulk_die2","ns2d_sfx_skulk_die3"};
        Entity skulk = newPositioned(world, x, y)
                .addComponent(new Anim("skulk", Anim.Layer.ENEMIES))
                .addComponent(health)
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

    public static Entity createGorgeHead(World world, float x, float y, Entity skulk) {

        Weapon weapon = new Weapon();
        weapon.fireCooldown = 4f;
        weapon.minBullets = 1;
        weapon.maxBullets = 1;
        weapon.bulletPayload.maxLifetime = 8f;
        weapon.spread = 5;
        weapon.recoil *= 10;
        weapon.bulletSpeed *= 0.2f;
        weapon.bulletAnimId = "gorge-spit";
        weapon.fireSfxId = null;
        weapon.bulletFriction = 0.01f;
        weapon.bulletBounce = 0.8f;
        weapon.bulletPayload.radius = 20;
        weapon.bulletPayload.minDamage = weapon.bulletPayload.maxDamage = 2;
        weapon.bulletGravityFactor = 2;
        weapon.bulletPayload.explodeSfxId = "ns2d_sfx_gl_explode";
        weapon.bulletBounce = 0;
        weapon.enemyGroup = "player-friend";
        weapon.muzzleFlare = false;
        final int originX = 5;
        final int originY = 3;
        final int mountX = 23;
        final int mountY = 13;
        return newPositioned(world, x, y)
                .addComponent(new Anim("gorge-head", Anim.Layer.PLAYER_ARM, originX, originY))
                .addComponent(new Attached(skulk, mountX - originX, mountY - originY))
                .addComponent(weapon)
                .addComponent(new Bounds(G.CELL_SIZE, G.CELL_SIZE));
    }

    public static Entity createGorge(final World world, final float x, final float y) {

        Health health = new Health(2);
        health.woundParticle = "alienblood";
        health.deathSfxId = new String[] {"ns2d_sfx_skulk_die1","ns2d_sfx_skulk_die2","ns2d_sfx_skulk_die3"};
        SkulkControlled enemy = new SkulkControlled();
        enemy.closestEnemyApproach = 100;
        Entity skulk = newPositioned(world, x, y)
                .addComponent(new Anim("gorge", Anim.Layer.ENEMIES))
                .addComponent(health)
                .addComponent(new Focus())
                .addComponent(new Physics())
                .addComponent(new Gravity())
                .addComponent(new WallSensor())
                .addComponent(enemy)
                .addComponent(new Bounds(32, 17));

        Entity head = EntityFactory.createGorgeHead(world, x, y, skulk)
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
                .addComponent(new Bounds(48, 48))
                .addComponent(new EntitySpawner("skulk","gorge"))
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

        Entity structureSocket = createStructureSocket(world, x, y);
        structureSocket.addToWorld();
        return newPositioned(world, x, y)
                .addComponent(new Bounds(16 * 3, 16 * 3))
                .addComponent(new Harvester())
                .addComponent(resourceDispenserWeapon(5, 2,3))
                .addComponent(new Attached(structureSocket))
                .addComponent(new HealthIndicator())
                .addComponent(new Buildable("resourcetower", "resourcetower-unbuilt", COST_RESOURCETOWER))
                .addComponent(new Anim("resourcetower-unbuilt", Anim.Layer.DIRECTLY_BEHIND_PLAYER));
    }

    private static Weapon resourceDispenserWeapon( float cooldown, int min, int max ) {
        Weapon weapon = new Weapon();
        weapon.cooldown = weapon.fireCooldown = cooldown;
        weapon.aimRotation = 90;
        weapon.minBullets = min;
        weapon.cooldownWhileNotFiring = false;
        weapon.maxBullets = max;
        weapon.bulletPayload.type = Payload.DamageType.RESOURCE;
        weapon.bulletPayload.maxLifetime = 240f;
        weapon.recoil = 3f;
        weapon.spread = 40;
        weapon.bulletSpeed = 140f;
        weapon.firing = false;
        weapon.bulletBounce = 1;
        weapon.bulletAnimId = "resource";
        weapon.enemyGroup = "player";
        return weapon;
    }

    private static Entity createStructureSocket(World world, float x, float y) {
        return newPositioned(world,x,y);
    }

    public static Entity createTechpoint(World world, float x, float y) {
        Entity structureSocket = createStructureSocket(world, x, y);
        structureSocket.addToWorld();
        Health health = new Health(100);
        health.woundParticle = "debris";
        health.damageSfxId = new String[]{"ns2d_sfx_structure_damage1","ns2d_sfx_structure_damage2","ns2d_sfx_structure_damage1"};
        Buildable buildable = new Buildable("techpoint", "techpoint-unbuilt", 999);
        buildable.built=true;
        return newPositioned(world, x, y)
                .addComponent(new Bounds(64,64))
                .addComponent(health)
                .addComponent(buildable)
                .addComponent(resourceDispenserWeapon(12, 1, 1))
                .addComponent(new Critical())
                .addComponent(new Attached(structureSocket))
                .addComponent(new HealthIndicator())
                .addComponent(new Anim("techpoint", Anim.Layer.DIRECTLY_BEHIND_PLAYER));
    }

    public static Entity createSpawner(final World world, final float x, final float y) {
        Entity structureSocket = createStructureSocket(world, x, y);
        structureSocket.addToWorld();
        Buildable buildable = new Buildable("spawner", "spawner-unbuilt", COST_INFANTRY_PORTAL);
        buildable.built = true;
        return newPositioned(world, x, y)
                .addComponent(new Bounds(16, 16))
                .addComponent(new Attached(structureSocket))
                .addComponent(buildable)
                .addComponent(new Anim("spawner", Anim.Layer.DIRECTLY_BEHIND_PLAYER));

    }

    public static Entity createArmory(World world, float x, float y) {
        Entity structureSocket = createStructureSocket(world, x, y);
        structureSocket.addToWorld();

        Weapon weapon = new Weapon();
        weapon.cooldown = 5;
        weapon.fireCooldown = 30;
        weapon.aimRotation = 90;
        weapon.cooldownWhileNotFiring = false;
        weapon.minBullets = 1;
        weapon.maxBullets = 1;
        weapon.bulletPayload.type = Payload.DamageType.WEAPON_PICKUP;
        weapon.bulletPayload.maxLifetime = 240f;
        weapon.recoil = 3f;
        weapon.spread = 40;
        weapon.bulletSpeed = 140f;
        weapon.firing = false;
        weapon.bulletBounce = 1;
        weapon.bulletAnimId = "shotgun";
        weapon.enemyGroup = "player";

        return newPositioned(world, x, y)
                .addComponent(new Bounds(16 * 3, 16 * 3))
                .addComponent(new HealthIndicator())
                .addComponent(new Attached(structureSocket))
                .addComponent(weapon)
                .addComponent(new Buildable("armory", "armory-unbuilt", COST_ARMORY))
                .addComponent(new Anim("armory-unbuilt", Anim.Layer.DIRECTLY_BEHIND_PLAYER));
    }



    public static Entity createSentryHead(World world, float x, float y, Entity player) {
        Weapon weapon = new Weapon();
        weapon.shellParticle = "bulletcasing";
        weapon.recoil = 2;
        weapon.bulletLifetime = 1.5f;
        weapon.fireCooldown = 0.1f;
        weapon.fireSfxId = "ns2d_sfx_sentry_fire";
        weapon.bulletPayload.maxLifetime = 1.5f;

        Buildable sentry = new Buildable("sentry", "sentry-frame-unbuilt", COST_SENTRY);
        sentry.weaponUseCausesDamage=true;
        return newPositioned(world, x, y)
                .addComponent(new Anim("sentry-frame-unbuilt", Anim.Layer.PLAYER_ARM, WEAPON_ROT_ORIGIN_X, WEAPON_ROT_ORIGIN_Y))
                .addComponent(new Attached(player, WEAPON_ROT_ORIGIN_X - 10, PLAYER_WEAPON_MOUNT_Y - WEAPON_ROT_ORIGIN_Y + 4))
                .addComponent(new HealthIndicator())
                .addComponent(sentry)
                .addComponent(weapon)
                .addComponent(new Bounds(32, 32));
    }


    public static Entity createSentry(World world, float x, float y) {
        Entity structureSocket = createStructureSocket(world, x, y);
        structureSocket.addToWorld();

        Entity sentry = newPositioned(world, x, y)
                .addComponent(new Bounds(32,32))
                .addComponent(new Attached(structureSocket))
                .addComponent(new Anim("sentry-frame", Anim.Layer.DIRECTLY_BEHIND_PLAYER));

        Entity head = EntityFactory.createSentryHead(world, 0, 0, sentry);

        Inventory inventory = new Inventory();
        sentry.addComponent(inventory);
        inventory.weapon = head;

        head.addToWorld();

        return sentry;
    }

    public static Entity createSentryHead2(World world, float x, float y, Entity player) {
        Weapon weapon = new Weapon();
        weapon.shellParticle = "bulletcasing";
        weapon.recoil = 2;
        weapon.bulletLifetime = 1.5f;
        weapon.fireCooldown = 0.1f;
        weapon.fireSfxId = "ns2d_sfx_sentry_fire";
        weapon.bulletPayload.maxLifetime = 1.5f;
        weapon.aimRotation = 180;

        Buildable sentry2 = new Buildable("sentry2", "sentry2-frame-unbuilt", COST_SENTRY);
        sentry2.weaponUseCausesDamage=true;
        sentry2.defaultHealth *= 2;
        return newPositioned(world, x, y)
                .addComponent(new Anim("sentry2-frame-unbuilt", Anim.Layer.PLAYER_ARM, WEAPON_ROT_ORIGIN_X, WEAPON_ROT_ORIGIN_Y))
                .addComponent(new Attached(player, WEAPON_ROT_ORIGIN_X - 10, PLAYER_WEAPON_MOUNT_Y - WEAPON_ROT_ORIGIN_Y + 4))
                .addComponent(sentry2)
                .addComponent(new HealthIndicator())
                .addComponent(weapon)
                .addComponent(new Bounds(32, 32));
    }


    public static Entity createSentry2(World world, float x, float y) {
        Entity structureSocket = createStructureSocket(world, x, y);
        structureSocket.addToWorld();

        Entity sentry = newPositioned(world, x, y)
                .addComponent(new Bounds(32,32))
                .addComponent(new Attached(structureSocket))
                .addComponent(new Anim("sentry2-frame", Anim.Layer.DIRECTLY_BEHIND_PLAYER));

        Entity head = EntityFactory.createSentryHead2(world, 0, 0, sentry);

        Inventory inventory = new Inventory();
        sentry.addComponent(inventory);
        inventory.weapon = head;

        head.addToWorld();

        return sentry;
    }



    public static Entity createShotgun(World world, float x, float y, Entity player) {
        Weapon weapon = new Weapon();
        weapon.fireCooldown = 0.25f;
        weapon.minBullets = 8;
        weapon.maxBullets = 10;
        weapon.bulletPayload.maxLifetime = 0.5f;
        weapon.spread = 20;
        weapon.bulletSpeed *= 0.9f;
        weapon.fireSfxId = "ns2d_sfx_shotgun_fire";
        weapon.bulletAnimId = "slug";
        weapon.shellParticle = "shellcasing";
        return newPositioned(world, x, y)
                .addComponent(new Anim("shotgun", Anim.Layer.PLAYER_ARM, WEAPON_ROT_ORIGIN_X, WEAPON_ROT_ORIGIN_Y))
                .addComponent(new Attached(player, PLAYER_WEAPON_MOUNT_X - WEAPON_ROT_ORIGIN_X, PLAYER_WEAPON_MOUNT_Y - WEAPON_ROT_ORIGIN_Y))
                .addComponent(weapon)
                .addComponent(new Bounds(G.CELL_SIZE, G.CELL_SIZE));
    }

    public static Entity createRifle(World world, float x, float y, Entity player) {
        Weapon weapon = new Weapon();
        weapon.shellParticle = "bulletcasing";
        weapon.recoil = 2;
        weapon.fireCooldown = 0.1f;
        weapon.fireSfxId = "ns2d_sfx_lmg_fire2";

        weapon.bulletPayload.maxLifetime = 1.5f;

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
        weapon.bulletPayload.maxLifetime = 4f;
        weapon.spread = 5;
        weapon.recoil *= 10;
        weapon.bulletSpeed *= 0.5f;
        weapon.bulletAnimId = "grenade";
        weapon.fireSfxId = "ns2d_sfx_gl_fire";
        weapon.bulletFriction = 0.01f;
        weapon.bulletBounce = 0.8f;
        weapon.bulletPayload.radius = 50;
        weapon.bulletPayload.minDamage = weapon.bulletPayload.maxDamage = 5;
        weapon.bulletGravityFactor = 2;
        weapon.bulletPayload.explodeSfxId = "ns2d_sfx_gl_explode";
        return newPositioned(world, x, y)
                .addComponent(new Anim("grenadelauncher", Anim.Layer.PLAYER_ARM, WEAPON_ROT_ORIGIN_X, WEAPON_ROT_ORIGIN_Y))
                .addComponent(new Attached(player, PLAYER_WEAPON_MOUNT_X - WEAPON_ROT_ORIGIN_X, PLAYER_WEAPON_MOUNT_Y - WEAPON_ROT_ORIGIN_Y))
                .addComponent(weapon)
                .addComponent(new Bounds(G.CELL_SIZE, G.CELL_SIZE));
    }

    public static Entity createFlamethrower(World world, float x, float y, Entity player) {
        return createGrenadeLauncher(world,x,y, player);
    }
}
