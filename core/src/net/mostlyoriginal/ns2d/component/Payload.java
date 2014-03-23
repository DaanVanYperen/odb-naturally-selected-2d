package net.mostlyoriginal.ns2d.component;

import com.artemis.Component;

/**
 * @author Daan van Yperen
 */
public class Payload extends Component {

    public static enum DamageType {
        EXPLOSIVE
    }

    public DamageType type =  DamageType.EXPLOSIVE;
    public float radius = 0;
    public int minDamage = 1;
    public int maxDamage = 1;
    public String triggerGroup;

    public Payload clone()
    {
        final Payload p = new Payload();
        p.type = type;
        p.radius = radius;
        p.minDamage = minDamage;
        p.maxDamage = maxDamage;
        p.triggerGroup = triggerGroup;
        return p;
    }
}
