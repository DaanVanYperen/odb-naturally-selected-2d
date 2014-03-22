package net.mostlyoriginal.ns2d.system.active;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import net.mostlyoriginal.ns2d.api.DualEntityProcessSystem;
import net.mostlyoriginal.ns2d.component.Bounds;
import net.mostlyoriginal.ns2d.component.Pos;

/**
 * @author Daan van Yperen
 */
@Wire
public class BulletCollisionSystem extends DualEntityProcessSystem {

    private ComponentMapper<Bounds> bm;
    private ComponentMapper<Pos> pm;


    public BulletCollisionSystem() {
        super("bullet", "enemy");
    }

    public final boolean collides(Bounds b1, Pos p1, Bounds b2, Pos p2 ) {

        if ( b1==null || p1 ==null && b2==null && p2==null)
            return false;

        final float minx = p1.x + b1.x1;
        final float miny = p1.y + b1.y1;
        final float maxx = p1.x + b1.x2;
        final float maxy = p1.y + b1.y2;

        final float bminx = p2.x + b2.x1;
        final float bminy = p2.y + b2.y1;
        final float bmaxx = p2.x + b2.x2;
        final float bmaxy = p2.y + b2.y2;

        return
                !(minx > bmaxx || maxx < bminx ||
                  miny > bmaxy || maxy < bminy );
    }

    @Override
    protected void processEntity(Entity entityA, Entity entityB) {
        if ( collides(
                bm.getSafe(entityA),
                pm.getSafe(entityA),
                bm.getSafe(entityB),
                pm.getSafe(entityB)))
        {
            entityA.deleteFromWorld();
            entityB.deleteFromWorld();
        }
    }
}
