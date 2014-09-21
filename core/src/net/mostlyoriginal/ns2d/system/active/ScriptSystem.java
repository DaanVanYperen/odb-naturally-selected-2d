package net.mostlyoriginal.ns2d.system.active;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.utils.Array;
import net.mostlyoriginal.ns2d.component.Script;
import net.mostlyoriginal.ns2d.step.Step;

/**
 * @author Daan van Yperen
 */
@Wire
public class ScriptSystem extends EntityProcessingSystem {

    private ComponentMapper<Script> sm;

    public ScriptSystem() {
        super(Aspect.getAspectForAll(Script.class));
    }

    @Override
    protected void process(Entity e) {

        Script script = sm.get(e);
        script.age += world.delta;

        final Array<Step> steps = script.steps;
        for (int i = 0; i < steps.size; i++) {
            final Step step = steps.get(i);
            if (script.age >= step.atAge && step.act(world.delta, e) && i < steps.size) {
                steps.removeIndex(i);
                step.release();
                i--;
            }
        }

        if (script.steps.size == 0) {
            e.edit().remove(Script.class);
        }
    }
}
