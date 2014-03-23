package net.mostlyoriginal.ns2d.util;

import com.artemis.Entity;
import com.artemis.World;
import net.mostlyoriginal.ns2d.G;
import net.mostlyoriginal.ns2d.component.*;

/**
 * @author Daan van Yperen
 */
public class EntityFactory {

    public static Entity createPlayer(final World world, final float x, final float y) {

        Entity player = newPositioned(world, x, y)
                .addComponent(new Anim("player", Anim.Layer.PLAYER))
                .addComponent(new Physics())
                .addComponent(new Health(10))
                .addComponent(new RespawnOnDeath())
                .addComponent(new Gravity())
                .addComponent(new WallSensor())
                .addComponent(new PlayerControlled())
                .addComponent(new Bounds(G.CELL_SIZE, G.CELL_SIZE));
        return player;
    }



    public static Entity createSkulkHead(World world, float x, float y, Entity skulk) {
        Weapon weapon = new Weapon();
        weapon.bulletLifetime = 0f;
        weapon.fireCooldown = 0.4f;
        weapon.bulletGroup = "enemy-bullet";
        return newPositioned(world, x, y)
                .addComponent(new Anim("player-arm", Anim.Layer.PLAYER_ARM))
                .addComponent(new Attached(skulk))
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
                .addComponent(new Bounds(G.CELL_SIZE, G.CELL_SIZE));

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

    public static Entity createRifle(World world, float x, float y, Entity player) {
        return newPositioned(world, x, y)
                .addComponent(new Anim("player-arm", Anim.Layer.PLAYER_ARM))
                .addComponent(new Attached(player))
                .addComponent(new Weapon())
                .addComponent(new Bounds(G.CELL_SIZE, G.CELL_SIZE));
    }

    public static Entity createMouseCursor(World world, float x, float y) {
        return newPositioned(world, x, y)
                .addComponent(new MouseCursor());
    }

    public static Entity createResourceTower(World world, float x, float y) {
        return newPositioned(world, x, y)
                .addComponent(new Bounds(16 * 3, 16 * 3))
                .addComponent(new Health(100))
                .addComponent(new Buildable("resourcetower", "resourcetower-unbuilt"))
                .addComponent(new Anim("resourcetower-unbuilt", Anim.Layer.DIRECTLY_BEHIND_PLAYER));
    }

    public static Entity createTechpoint(World world, float x, float y) {
        return newPositioned(world, x, y)
                .addComponent(new Health(1000))
                .addComponent(new Anim("techpoint", Anim.Layer.DIRECTLY_BEHIND_PLAYER));
    }

    public static Entity createSpawner(final World world, final float x, final float y) {
        return newPositioned(world, x, y)
                .addComponent(new Bounds(16, 16))
                .addComponent(new Health(100))
                .addComponent(new Buildable("spawner", "spawner-unbuilt"))
                .addComponent(new Anim("spawner", Anim.Layer.DIRECTLY_BEHIND_PLAYER));

    }
}
