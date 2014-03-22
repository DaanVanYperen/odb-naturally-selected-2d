package net.mostlyoriginal.ns2d.component;

import com.artemis.Component;
import com.artemis.Entity;

/**
 * Stick between two entities.
 *
 * @author Daan van Yperen
 */
public class Inbetween extends Component {

    public Entity a;
    public Entity b;
    public float tween = 0.5f; // where are we? 0.0 at a, 1.0 at b location

    public Inbetween(Entity a, Entity b) {
        this.a = a;
        this.b = b;
    }
}
