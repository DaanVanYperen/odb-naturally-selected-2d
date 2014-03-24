package net.mostlyoriginal.ns2d.system.active;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.managers.GroupManager;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.math.MathUtils;
import net.mostlyoriginal.ns2d.component.Buildable;
import net.mostlyoriginal.ns2d.component.Harvester;
import net.mostlyoriginal.ns2d.component.Wallet;
import net.mostlyoriginal.ns2d.system.render.DialogRenderSystem;

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
    private int builtCount;
    private int unbuiltCount;
    private float notBuiltCooldown = 8;
    private DialogRenderSystem dialogRenderSystem;
    private GroupManager groupManager;


    public HarvesterSystem() {
        super(Aspect.getAspectForAll(Harvester.class, Buildable.class));
    }

    @Override
    protected void begin() {
        builtCount = 0;
        unbuiltCount = 0;
    }

    @Override
    protected void end() {
        notBuiltCooldown -= world.delta;
        // check if more than 50% of harvesters is built.
        if (notBuiltCooldown <= 0) {
            notBuiltCooldown = MathUtils.random(20,40);
            if (builtCount < 3 && (builtCount == 0 || (builtCount < unbuiltCount*0.5f))) {
                dialogRenderSystem.randomSay(DialogRenderSystem.BUILD_MORE_HARVESTERS);
            }
        }
    }

    @Override
    protected void process(Entity e) {
        final Harvester harvester = hm.get(e);
        final Buildable buildable = bm.get(e);

        if (buildable.built) {
            builtCount++;
        } else {
            unbuiltCount++;
        }
    }
}
