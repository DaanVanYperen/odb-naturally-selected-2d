package net.mostlyoriginal.ns2d.component;

import com.artemis.Component;
import com.artemis.Entity;

/**
 * @author Daan van Yperen
 */
public class Homing extends Component {
    public Entity target;
    public float speedFactor = 5f;
    public float maxVelocity = 5000f;
    public float maxDistance = 999999f;

    public Homing(Entity target) {
        this.target = target;
    }
}
