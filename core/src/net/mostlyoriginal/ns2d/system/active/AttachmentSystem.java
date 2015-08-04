package net.mostlyoriginal.ns2d.system.active;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.math.Vector2;
import net.mostlyoriginal.ns2d.component.Attached;
import net.mostlyoriginal.ns2d.component.Pos;

/**
 * @author Daan van Yperen
 */
@Wire
public final class AttachmentSystem extends EntityProcessingSystem {
    private ComponentMapper<Pos> pm;
    private ComponentMapper<Attached> am;

    public AttachmentSystem() {
        super(Aspect.getAspectForAll(Pos.class, Attached.class));
    }

    Vector2 vTmp = new Vector2();

    @Override
    protected void process(Entity e) {
        final Attached attached = am.get(e);
        if (attached.parent != null) {
            if (attached.parent.isActive()) {
                Pos pos = pm.get(e);
                Pos parPos = pm.get(attached.parent);

                pos.x = parPos.x + attached.xo + attached.slackX;
                pos.y = parPos.y + attached.yo + attached.slackY;

                updateSlack(attached);
            } else {
                // parent gone? we gone!
                e.deleteFromWorld();
            }
        }
    }

    public void push(final Entity entity, float rotation, float force) {
        if (am.has(entity)) {
            push(am.get(entity), rotation, force);
        }
    }

    /**
     * Stack a force upon this target.
     *
     * @param attached
     * @param rotation
     * @param force
     */
    public void push(final Attached attached, float rotation, float force) {
        vTmp.set(force, 0).rotate(rotation).add(attached.slackX, attached.slackY).clamp(0f, attached.maxSlack);
        attached.slackX = vTmp.x;
        attached.slackY = vTmp.y;
    }

    /**
     * Slack, like weapon pushing back on the mountpoint.
     *
     * @param attached
     */
    private void updateSlack(final Attached attached) {

        float len = vTmp.set(attached.slackX, attached.slackY).len() - world.delta * attached.spring;
        if (len > 0) {
            vTmp.nor().scl(len);
        } else {
            vTmp.set(0, 0);
        }

        attached.slackX = vTmp.x;
        attached.slackY = vTmp.y;
    }
}
