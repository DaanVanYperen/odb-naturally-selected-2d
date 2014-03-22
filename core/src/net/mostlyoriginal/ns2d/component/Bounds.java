package net.mostlyoriginal.ns2d.component;

import com.artemis.Component;

/**
 * @author Daan van Yperen
 */
public class Bounds extends Component {
    public int x1;
    public int y1;
    public int x2;
    public int y2;

    public Bounds(final int width, final int height) {
        this.x1=this.y1=0;
        this.x2=width;
        this.y2=height;
    }

    public Bounds(final int x1, final int y1, final int x2, final int y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }
}
