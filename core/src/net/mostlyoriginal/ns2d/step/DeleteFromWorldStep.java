package net.mostlyoriginal.ns2d.step;

import com.artemis.Entity;

/**
 * @author Daan van Yperen
 */
public class DeleteFromWorldStep extends Step {

    public DeleteFromWorldStep() {}

    @Override
    public boolean act(float delta, Entity e) {
        e.deleteFromWorld();
        return true;
    }

    @Override
    public void reset() {
    }
}
