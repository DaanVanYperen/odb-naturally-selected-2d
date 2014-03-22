package net.mostlyoriginal.ns2d.system.passive;

import com.artemis.systems.VoidEntitySystem;

/**
 * @author Daan van Yperen
 */
public class PassiveSystem extends VoidEntitySystem {

    public PassiveSystem() {
        setPassive(true);
    }

    @Override
    protected void processSystem() {
    }
}
