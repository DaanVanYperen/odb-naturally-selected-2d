package net.mostlyoriginal.ns2d.system.active;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.math.Vector2;
import net.mostlyoriginal.ns2d.component.Inbetween;
import net.mostlyoriginal.ns2d.component.Pos;

/**
 * @author Daan van Yperen
 */
@Wire
public final class InbetweenSystem extends EntityProcessingSystem {
    private ComponentMapper<Inbetween> dm;
    private ComponentMapper<Pos> pm;

    public InbetweenSystem() {
        super(Aspect.all(Inbetween.class, Pos.class));
    }

    private Vector2 tmp = new Vector2();

    @Override
    protected void process(Entity e) {
        final Inbetween inbetween = dm.get(e);
        Pos pos1 = pm.get(inbetween.a);
        Pos pos2 = pm.get(inbetween.b);

        tmp.set(pos2.x, pos2.y).sub(pos1.x, pos1.y).scl(inbetween.tween).add(pos1.x, pos1.y);

        Pos pos = pm.get(e);
        pos.x = tmp.x;
        pos.y = tmp.y;
    }
}
