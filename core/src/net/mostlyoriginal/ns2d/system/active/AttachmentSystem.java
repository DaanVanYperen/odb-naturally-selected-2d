package net.mostlyoriginal.ns2d.system.active;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import net.mostlyoriginal.ns2d.component.Attached;
import net.mostlyoriginal.ns2d.component.Pos;

/**
 * @author Daan van Yperen
 */
@Wire
public class AttachmentSystem extends EntityProcessingSystem {

    private ComponentMapper<Pos> pm;
    private ComponentMapper<Attached> am;

    public AttachmentSystem() {
        super(Aspect.getAspectForAll(Pos.class, Attached.class));
    }

    @Override
    protected void process(Entity e) {
        final Attached attached = am.get(e);
        if (attached.parent != null) {
            if (attached.parent.isActive()) {
                Pos pos = pm.get(e);
                Pos parPos = pm.get(attached.parent);

                pos.x = parPos.x;
                pos.y = parPos.y;
            } else {
                // parent gone? we gone!
                e.deleteFromWorld();
            }
        }
    }
}
