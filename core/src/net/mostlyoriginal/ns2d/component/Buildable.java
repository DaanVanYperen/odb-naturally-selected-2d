package net.mostlyoriginal.ns2d.component;

import com.artemis.Component;

/**
 * @author Daan van Yperen
 */
public final class Buildable extends Component {
    public boolean built;
    public String builtAnimId;
    public String unbuiltAnimId;
    public final int resourceCost;
    public boolean weaponUseCausesDamage;
    public float weaponUseDamageCooldown;
    public int defaultHealth = 30;
    public float damageAge = 999;

    public Buildable(String builtAnimId, String unbuiltAnimId, int resourceCost) {
        this.builtAnimId = builtAnimId;
        this.unbuiltAnimId = unbuiltAnimId;
        this.resourceCost = resourceCost;
    }
}
