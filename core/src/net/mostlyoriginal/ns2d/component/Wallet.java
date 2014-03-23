package net.mostlyoriginal.ns2d.component;

import com.artemis.Component;

/**
 * @author Daan van Yperen
 */
public class Wallet extends Component {
    public int resources;

    public Wallet(int resources) {
        this.resources = resources;
    }
}
