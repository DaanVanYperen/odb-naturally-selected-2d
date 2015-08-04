package net.mostlyoriginal.ns2d.step;

import com.artemis.Component;
import com.artemis.Entity;

/**
 * @author Daan van Yperen
 */
public final class AddStep extends Step {
    public Component component;

    public AddStep() {
    }

    @Override
    public boolean act(float delta, Entity e) {
        e.edit().add(component);
        return true;
    }

    @Override
    public void reset() {
        component = null;
    }
}
