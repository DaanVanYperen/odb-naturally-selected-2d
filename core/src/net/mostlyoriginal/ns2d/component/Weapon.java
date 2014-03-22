package net.mostlyoriginal.ns2d.component;

import com.artemis.Component;

/**
 * @author Daan van Yperen
 */
public class Weapon extends Component {
    boolean firing;

    public float cooldown = 0;

    public float bulletSpeed = 500;
    public float fireCooldown = 1f/15f; // cooldown per bullet.
    public float spread = 10f; // spread in degrees
}
