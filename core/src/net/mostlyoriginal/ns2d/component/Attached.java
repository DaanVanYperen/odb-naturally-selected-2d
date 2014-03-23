package net.mostlyoriginal.ns2d.component;

import com.artemis.Component;
import com.artemis.Entity;

/**
 * @author Daan van Yperen
 */
public class Attached extends Component{

    public Entity parent;
    public int xo;
    public int yo;


    // slack, like recoil on a weapon.
    public float maxSlack = 10; // max length of the slack vector. like weapon recoil.
    public float slackX;   // offset X
    public float slackY;   // offset Y
    public float spring=30;   // Tension on the spring to return to its original state. 1= really slow.

    public Attached(Entity parent) {
        this.parent = parent;
    }

    public Attached(Entity parent, int xo, int yo) {
        this.parent = parent;
        this.xo = xo;
        this.yo = yo;
    }
}
