package net.mostlyoriginal.ns2d.component;

import com.artemis.Component;

/**
 * @author Daan van Yperen
 */
public final class SkulkControlled extends Component {
    public float leapCooldown;
    public float closestEnemyApproach = 0;
    public boolean canLeap = true;
}
