package net.mostlyoriginal.ns2d.component;

import com.artemis.Component;

/**
 * Produces coins
 *
 * @author Daan van Yperen
 */
public final class Harvester extends Component {
    public int count = 1;
    public float cooldown;
    public float interval = 3;
}
