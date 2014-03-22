package net.mostlyoriginal.ns2d.component;

import com.artemis.Component;

/**
 * @author Daan van Yperen
 */
public class Terminal extends Component {
    public float lifetime;

    public Terminal(float lifetime) {
        this.lifetime = lifetime;
    }
}
