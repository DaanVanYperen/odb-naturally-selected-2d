package net.mostlyoriginal.ns2d.step;

import com.artemis.Component;
import com.artemis.Entity;

/**
 * @author Daan van Yperen
 */
public class AddStep extends Step {

    public Component component;

    @Override
    public boolean act(float delta, Entity e) {
        e.addComponent(component);
        return true;
    }

    @Override
    public void reset() {
        component = null;
    }
}
