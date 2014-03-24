package net.mostlyoriginal.ns2d.system.active;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
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
        if (notBuiltCooldown <= 0) {
            notBuiltCooldown = MathUtils.random(10,20);
            if (builtCount == 0) {
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
