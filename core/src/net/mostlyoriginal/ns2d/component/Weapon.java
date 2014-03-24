package net.mostlyoriginal.ns2d.component;

import com.artemis.Component;

/**
 * @author Daan van Yperen
 */
public class Weapon extends Component {

    public float cooldown = 0;

    public float bulletLifetime = 10;
    public float bulletSpeed = 500;
    public int minBullets = 1;
    public int maxBullets = 1;
    public float fireCooldown = 0.04f; // cooldown per bullet.
    public float spread = 10f; // spread in degrees
    public boolean firing = false;

    public String bulletAnimId = "bullet";
    public float recoil = 20;
    public float bulletFriction = 0.01f;
    public float bulletBounce = 0;
    public Payload bulletPayload = new Payload();
    public String enemyGroup = "enemy";

    public boolean muzzleFlare = true;
    public String shellParticle;
    public float bulletGravityFactor = 1;
    public float aimRotation = 0;
}
