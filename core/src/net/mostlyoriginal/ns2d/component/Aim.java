package net.mostlyoriginal.ns2d.component;

import com.artemis.Component;
import com.artemis.Entity;

/**
 * Rotate aim towards target.
 *
 * @author Daan van Yperen
 */
public final class Aim extends Component {
    public Entity at;

    public Aim() {
        this(null);
    }

    public Aim(Entity at) {
        this.at = at;
    }
}
