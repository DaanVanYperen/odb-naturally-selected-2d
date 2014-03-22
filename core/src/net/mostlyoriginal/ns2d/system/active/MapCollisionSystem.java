package net.mostlyoriginal.ns2d.system.active;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import net.mostlyoriginal.ns2d.component.Pos;
import net.mostlyoriginal.ns2d.system.passive.MapSystem;
import net.mostlyoriginal.ns2d.util.MapMask;

/**
 * Constrain movement to map collision.
 *
 * @author Daan van Yperen
 */
@Wire
public class MapCollisionSystem extends EntityProcessingSystem {

    private MapSystem mapSystem;

    boolean initialized;
    public MapMask solidMask;

    public MapCollisionSystem() {
        super(Aspect.getAspectForAll(Pos.class));
    }

    @Override
    protected void begin() {
        if ( !initialized )
        {
            initialized = true;
            solidMask = mapSystem.getMask("solid");
        }
    }

    @Override
    protected void process(Entity e) {


    }
}
