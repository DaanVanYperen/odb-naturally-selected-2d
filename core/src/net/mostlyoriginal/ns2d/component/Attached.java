package net.mostlyoriginal.ns2d.component;

import com.artemis.Component;
import com.artemis.Entity;

/**
 * @author Daan van Yperen
 */
public class Attached  extends Component{
    public Entity parent;

    public Attached(Entity parent) {
        this.parent = parent;
    }
}
