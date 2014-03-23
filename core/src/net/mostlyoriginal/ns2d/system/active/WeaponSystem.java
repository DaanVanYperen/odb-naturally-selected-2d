package net.mostlyoriginal.ns2d.system.active;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.managers.GroupManager;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.math.MathUtils;
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
    private GroupManager groupManager;
    private PhysicsSystem physicsSystems;
    private AfterPhysicsSystem afterPhysicsSystem;
    private AttachmentSystem attachmentSystem;

    public WeaponSystem() {
        super(Aspect.getAspectForAll(Weapon.class, Pos.class, Bounds.class, Anim.class));
    }

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

                // repeated bullets.
                for (int c = 0, s = MathUtils.random(weapon.minBullets, weapon.maxBullets); c < s; c++) {

                    Entity bullet = EntityFactory.createBullet(world, pos.x + bounds.cy(), pos.y + bounds.cy());

                    bullet.addComponent(new Terminal(weapon.bulletLifetime));

                    // rotate bullet to player rotation
                    float rotation = anim.rotation + MathUtils.random(-weapon.spread, weapon.spread);
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


                    attachmentSystem.push(gun, rotation - 180, weapon.recoil / s);
                    physicsSystems.push(bullet, rotation, weapon.bulletSpeed);

                    bullet.addToWorld();
                }
            }
        }

    }
}
