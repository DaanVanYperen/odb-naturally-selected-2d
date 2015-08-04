package net.mostlyoriginal.ns2d.component;

import com.artemis.Component;

/**
 * @author Daan van Yperen
 */
public final class Health extends Component {
    public int health;
    public int damage;

    public String[] deathSfxId;
    public String[] damageSfxId;
    public String woundParticle;

    public Health(int health) {
        this.health = health;
    }
}
