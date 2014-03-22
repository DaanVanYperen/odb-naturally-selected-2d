package net.mostlyoriginal.ns2d.component;

import com.artemis.Component;

/**
 * @author Daan van Yperen
 */
public class Gravity extends Component {
    public static final float DEFAULT_Y_GRAVITY = -9.8f;
    public float x = 0;
    public float y = DEFAULT_Y_GRAVITY;

    public Gravity() {}

    public Gravity(float y) {
        this.y = y;

    }
}
