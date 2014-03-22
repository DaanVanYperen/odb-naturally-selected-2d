package net.mostlyoriginal.ns2d.util;

import com.artemis.ComponentType;
import com.artemis.Entity;
import com.badlogic.gdx.math.Vector2;
import net.mostlyoriginal.ns2d.component.Pos;

/**
 * @author Daan van Yperen
 */
public class EntityUtil{

    public static final ComponentType posType = ComponentType.getTypeFor(Pos.class);

    private static Vector2 tmp = new Vector2();

    public static float distance2( final Entity a, final Entity b)
    {
        final Pos pa = (Pos)a.getComponent(posType);
        final Pos pb = (Pos)b.getComponent(posType);

        return tmp.set(pa.x, pa.y).dst2(pb.x, pb.y);
    }

    public static float angle( final Entity a, final Entity b)
    {
        final Pos pa = (Pos)a.getComponent(posType);
        final Pos pb = (Pos)b.getComponent(posType);

        return tmp.set(pb.x, pb.y).sub(pa.x, pa.y).angle();

    }
}
