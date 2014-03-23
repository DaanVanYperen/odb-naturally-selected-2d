package net.mostlyoriginal.ns2d.step;

import com.artemis.Component;
import com.artemis.Entity;

/**
 * @author Daan van Yperen
 */
public class RemoveStep extends Step {

    public Class<? extends Component> componentClass;

    @Override
    public boolean act(float delta, Entity e) {
        e.removeComponent(componentClass);
        return true;
    }

    @Override
    public void reset() {
        componentClass = null;
    }
}
