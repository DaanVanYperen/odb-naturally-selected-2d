package net.mostlyoriginal.ns2d.system.active;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import net.mostlyoriginal.ns2d.component.Anim;
import net.mostlyoriginal.ns2d.component.Terminal;

/**
 * @author Daan van Yperen
 */
@Wire
public final class TerminalSystem extends EntityProcessingSystem {
    private ComponentMapper<Terminal> tm;
    private ComponentMapper<Anim> am;

    public TerminalSystem() {
        super(Aspect.all(Terminal.class));
    }

    @Override
    protected void process(Entity e) {
        final Terminal terminal = tm.get(e);
        terminal.lifetime -= world.delta;
        if (terminal.alphaFadeout > 0 && terminal.lifetime <= terminal.alphaFadeout && am.has(e)) {
            am.get(e).color.a = (terminal.lifetime / terminal.alphaFadeout);
        }
        if (terminal.lifetime <= 0)
            e.deleteFromWorld();
    }
}
