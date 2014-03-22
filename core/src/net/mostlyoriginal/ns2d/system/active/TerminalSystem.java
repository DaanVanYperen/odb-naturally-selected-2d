package net.mostlyoriginal.ns2d.system.active;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import net.mostlyoriginal.ns2d.component.Terminal;

/**
 * @author Daan van Yperen
 */
@Wire
public class TerminalSystem extends EntityProcessingSystem {

    ComponentMapper<Terminal> tm;

    public TerminalSystem() {
        super(Aspect.getAspectForAll(Terminal.class));
    }

    @Override
    protected void process(Entity e) {
        final Terminal terminal = tm.get(e);
        terminal.lifetime -= world.delta;
        if ( terminal.lifetime <= 0 )
            e.deleteFromWorld();
    }
}
