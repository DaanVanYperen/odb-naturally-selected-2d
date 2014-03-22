package net.mostlyoriginal.ns2d.util;

import com.artemis.Entity;
import com.artemis.World;
import net.mostlyoriginal.ns2d.component.Anim;
import net.mostlyoriginal.ns2d.component.CameraFocus;
import net.mostlyoriginal.ns2d.component.Physics;
import net.mostlyoriginal.ns2d.component.Pos;

/**
 * @author Daan van Yperen
 */
public class EntityFactory {

    public static Entity createPlayer(final World world, final float x, final float y) {

        return newPositioned(world, x, y)
                .addComponent(new Anim("player", Anim.Layer.PLAYER))
                .addComponent(new Physics())
                .addComponent(new CameraFocus());
    }

    public static Entity createSpawner(final World world, final float x, final float y) {
        return newPositioned(world, x, y)
                .addComponent(new Anim("spawner", Anim.Layer.DIRECTLY_BEHIND_PLAYER));

    }

    private static Entity newPositioned(final World world, final float x, final float y) {
        return world.createEntity()
                .addComponent(new Pos(x, y));
    }

    public static Entity createDuct(World world, float x, float y) {
        return world.createEntity()
                .addComponent(new Pos(x, y))
                .addComponent(new Anim("duct", Anim.Layer.ON_WALL));
    }
}
