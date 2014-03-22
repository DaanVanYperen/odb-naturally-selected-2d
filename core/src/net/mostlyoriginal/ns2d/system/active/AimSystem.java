package net.mostlyoriginal.ns2d.system.active;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.math.Vector2;
import net.mostlyoriginal.ns2d.component.Aim;
import net.mostlyoriginal.ns2d.component.Anim;
import net.mostlyoriginal.ns2d.component.Pos;

/**
 * Aim (by rotation) at a target.
 *
 * @author Daan van Yperen
 */
@Wire
public class AimSystem extends EntityProcessingSystem {

    private ComponentMapper<Aim> am;
    private ComponentMapper<Pos> pm;
    private ComponentMapper<Anim> nm;

    public AimSystem() {
        super(Aspect.getAspectForAll(Aim.class, Pos.class, Anim.class));
    }

    Vector2 vTmp = new Vector2();

    @Override
    protected void process(Entity e) {
        final Pos origin = pm.get(e);
        final Aim aim = am.get(e);

        if ( aim.at != null )
        {
            if ( aim.at.isActive() )
            {
                Pos target = pm.get(aim.at);

                nm.get(e).rotation = vTmp.set(target.x, target.y).sub(origin.x, origin.y).angle();
            } else {
                aim.at = null;
            }
        }
    }
}
