package net.mostlyoriginal.ns2d.system.active;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.managers.TagManager;
import com.artemis.systems.EntityProcessingSystem;
import net.mostlyoriginal.ns2d.component.Buildable;
import net.mostlyoriginal.ns2d.component.Harvester;
import net.mostlyoriginal.ns2d.component.Wallet;

/**
 * Logic for Coinage producing harvesters.
 *
 * @author Daan van Yperen
 */
@Wire
public class HarvesterSystem extends EntityProcessingSystem {

    private ComponentMapper<Harvester> hm;
    private ComponentMapper<Buildable> bm;
    private ComponentMapper<Wallet> wm;

    private TagManager tagManager;

    public HarvesterSystem() {
        super(Aspect.getAspectForAll(Harvester.class, Buildable.class));
        setEnabled(false);
    }

    @Override
    protected void process(Entity e) {
        final Harvester harvester = hm.get(e);
        final Buildable buildable = bm.get(e);

        if (buildable.built) {
            harvester.cooldown -= world.delta;
            if (harvester.cooldown <= 0) {
                harvester.cooldown = harvester.interval;
                Entity player = tagManager.getEntity("player");
                wm.get(player).resources += harvester.count;
            }
        }
    }
}
