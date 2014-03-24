package net.mostlyoriginal.ns2d.system.active;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.managers.GroupManager;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import net.mostlyoriginal.ns2d.component.*;
import net.mostlyoriginal.ns2d.util.EntityFactory;

/**
 * @author Daan van Yperen
 */
@Wire
public class WeaponSystem extends EntityProcessingSystem {

    private ComponentMapper<Weapon> wm;
    private ComponentMapper<Pos> pm;
    private ComponentMapper<Bounds> om;
    private ComponentMapper<Attached> atm;
    private ComponentMapper<Anim> am;
    private ComponentMapper<Physics> ym;
    private ComponentMapper<Gravity> gm;
    private GroupManager groupManager;
    private PhysicsSystem physicsSystems;
    private AfterPhysicsSystem afterPhysicsSystem;
    private AttachmentSystem attachmentSystem;
    private ParticleSystem particleSystem;

    public WeaponSystem() {
        super(Aspect.getAspectForAll(Weapon.class, Pos.class, Bounds.class, Anim.class));
    }

    private Vector2 vTmp = new Vector2();

    @Override
    protected void process(Entity gun) {

        final Weapon weapon = wm.get(gun);

        weapon.cooldown -= world.delta;
        if (weapon.firing) {
            weapon.firing = false;
            if (weapon.cooldown <= 0) {
                weapon.cooldown = weapon.fireCooldown;

                final Pos pos = pm.get(gun);
                final Bounds bounds = om.get(gun);
                final Anim anim = am.get(gun);

                float aimRotation = anim.rotation + weapon.aimRotation;
                if ( weapon.muzzleFlare ) {
                    // determine muzzle location.
                    vTmp.set(28,0).rotate(aimRotation).add(pos.x-8, pos.y+2).add(bounds.cx(), bounds.cy());
                    particleSystem.setRotation(aimRotation);
                    particleSystem.spawnParticle((int)vTmp.x, (int)vTmp.y, "muzzle-flare");
                    particleSystem.setRotation(0);
                }

                if ( weapon.shellParticle != null )
                {
                    vTmp.set(18,0).rotate(aimRotation).add(pos.x-8, pos.y+2).add(bounds.cx(), bounds.cy());
                    particleSystem.spawnParticle((int)vTmp.x, (int)vTmp.y, weapon.shellParticle);
                }

                // repeated bullets.
                for (int c = 0, s = MathUtils.random(weapon.minBullets, weapon.maxBullets); c < s; c++) {

                    Entity bullet = EntityFactory.createBullet(world, pos.x + bounds.cx(), pos.y + bounds.cy());

                    // rotate bullet to player rotation
                    float rotation = aimRotation + MathUtils.random(-weapon.spread, weapon.spread);
                    Anim bulletAnim = am.get(bullet);
                    bulletAnim.rotation = rotation;
                    bulletAnim.id = weapon.bulletAnimId;

                    // push back the user.
                    if (atm.has(gun)) {
                        Attached attachedTo = atm.get(gun);
                        if (attachedTo.parent != null && attachedTo.parent.isActive()) {
                            physicsSystems.push(attachedTo.parent, rotation - 180, weapon.recoil);
                        }
                    }



                    Physics physics = ym.get(bullet);
                    physics.friction = weapon.bulletFriction;
                    physics.bounce = weapon.bulletBounce;

                    Payload payload = weapon.bulletPayload.clone();
                    payload.triggerGroup = weapon.enemyGroup;
                    bullet.addComponent(payload);

                    Gravity gravity = gm.get(bullet);
                    gravity.y *= weapon.bulletGravityFactor;
                    gravity.x *= weapon.bulletGravityFactor;

                    attachmentSystem.push(gun, rotation - 180, weapon.recoil / s);
                    physicsSystems.push(bullet, rotation, weapon.bulletSpeed);

                    bullet.addToWorld();
                }

                if ( weapon.bulletPayload.type == Payload.DamageType.WEAPON_PICKUP )
                {
                    cycleWeaponPickup(weapon);
                }

            }
        }

    }

    private void cycleWeaponPickup(Weapon weapon) {

        switch (weapon.bulletAnimId)
        {
            case "rifle": weapon.bulletAnimId="shotgun"; break;
            case "shotgun": weapon.bulletAnimId="grenadelauncher"; break;
            case "grenadelauncher": weapon.bulletAnimId="flamethrower"; break;
            case "flamethrower": weapon.bulletAnimId="rifle"; break;
        }


    }
}
