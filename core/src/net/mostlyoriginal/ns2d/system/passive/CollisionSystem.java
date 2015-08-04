package net.mostlyoriginal.ns2d.system.passive;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;

import net.mostlyoriginal.api.system.core.PassiveSystem;
import net.mostlyoriginal.ns2d.component.Bounds;
import net.mostlyoriginal.ns2d.component.Pos;

/**
 * @author Daan van Yperen
 */
@Wire
public final class CollisionSystem extends PassiveSystem {
    private ComponentMapper<Bounds> bm;
    private ComponentMapper<Pos> pm;

    public final boolean overlaps(final Entity a, final Entity b) {
        final Bounds b1 = bm.getSafe(a);
        final Pos p1 = pm.getSafe(a);
        final Bounds b2 = bm.getSafe(b);
        final Pos p2 = pm.getSafe(b);

        if (b1 == null || p1 == null || b2 == null || p2 == null)
            return false;

        final float minx = p1.x + b1.x1;
        final float miny = p1.y + b1.y1;
        final float maxx = p1.x + b1.x2;
        final float maxy = p1.y + b1.y2;

        final float bminx = p2.x + b2.x1;
        final float bminy = p2.y + b2.y1;
        final float bmaxx = p2.x + b2.x2;
        final float bmaxy = p2.y + b2.y2;

        return !(minx > bmaxx || maxx < bminx ||
                miny > bmaxy || maxy < bminy);
    }
}
