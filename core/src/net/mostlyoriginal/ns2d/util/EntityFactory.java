package net.mostlyoriginal.ns2d.util;

import com.artemis.Entity;
import com.artemis.World;
import net.mostlyoriginal.ns2d.component.CameraFocus;
import net.mostlyoriginal.ns2d.component.Pos;

/**
 * @author Daan van Yperen
 */
public class EntityFactory {

    public static Entity createPlayer(final World world, final float x, final float y) {

        return newPositioned(world, x, y).addComponent(new CameraFocus());
    }

    public static Entity createSpawner(final World world, final float x, final float y) {
        return newPositioned(world, x, y);
    }

    private static Entity newPositioned(final World world, final float x, final float y) {
        return world.createEntity().addComponent(new Pos(x, y));
    }

    public static Entity createDuct(World world, float x, float y) {
        return world.createEntity().addComponent(new Pos(x, y));
    }
}
