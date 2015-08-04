package net.mostlyoriginal.ns2d.component;

import com.artemis.Component;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import net.mostlyoriginal.ns2d.step.AddStep;
import net.mostlyoriginal.ns2d.step.DeleteFromWorldStep;
import net.mostlyoriginal.ns2d.step.RemoveStep;
import net.mostlyoriginal.ns2d.step.Step;

/**
 * @author Daan van Yperen
 */
public final class Script extends Component {
    public Array<Step> steps = new Array<Step>(1);

    public float age;
    private float atAge;

    public Script() {
    }

    /**
     * Returns a new or pooled action of the specified type.
     */
    static public <T extends Step> T prepare(Class<T> type, float atAge) {
        Pool<T> pool = Pools.get(type);
        T node = pool.obtain();
        node.setPool(pool);
        node.atAge = atAge;
        return node;
    }

    public Script wait(float delaySeconds) {
        this.atAge += delaySeconds;
        return this;
    }

    public Script deleteFromWorld() {
        steps.add(prepare(DeleteFromWorldStep.class, atAge));
        return this;
    }

    public Script add(final Component component) {
        AddStep step = prepare(AddStep.class, atAge);
        step.component = component;
        steps.add(step);
        return this;
    }

    public Script remove(final Class<? extends Component> component) {
        RemoveStep step = prepare(RemoveStep.class, atAge);
        step.componentClass = component;
        steps.add(step);
        return this;
    }
}
