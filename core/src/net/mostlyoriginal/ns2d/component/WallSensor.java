package net.mostlyoriginal.ns2d.component;

import com.artemis.Component;

/**
 * @author Daan van Yperen
 */
public class WallSensor extends Component {
    public boolean onFloor = false;
    public boolean onHorizontalSurface = false;
    public boolean onVerticalSurface = false;

    public boolean onAnySurface() {
        return onHorizontalSurface || onVerticalSurface;
    }
}
