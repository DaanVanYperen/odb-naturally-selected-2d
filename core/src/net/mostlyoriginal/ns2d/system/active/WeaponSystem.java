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
    private ComponentMapper<Anim> am;
    private GroupManager groupManager;
    private PhysicsSystem physicsSystems;
    private AfterPhysicsSystem afterPhysicsSystem;

    public WeaponSystem() {
        super(Aspect.getAspectForAll(Weapon.class, Pos.class, Bounds.class, Anim.class));
    }

    @Override
    protected void process(Entity e) {

        final Weapon weapon = wm.get(e);
        if (weapon.firing) {
            weapon.firing = false;


            weapon.cooldown -= world.delta;
            if (weapon.cooldown <= 0) {
                weapon.cooldown = weapon.fireCooldown;

                final Pos pos = pm.get(e);
                final Bounds bounds = om.get(e);
                final Anim anim = am.get(e);

                // repeated bullets.
                for (int c = 0, s = MathUtils.random(weapon.minBullets, weapon.maxBullets); c < s; c++) {

                    Entity bullet = EntityFactory.createBullet(world, pos.x + bounds.cy(), pos.y + bounds.cy());
                    groupManager.add(bullet, weapon.bulletGroup);

                    bullet.addComponent(new Terminal(weapon.bulletLifetime));

                    // rotate bullet to player rotation
                    float rotation = anim.rotation + MathUtils.random(-weapon.spread, weapon.spread);
                    Anim bulletAnim = am.get(bullet);
                    bulletAnim.rotation = rotation;
                    bulletAnim.id = weapon.bulletAnimId;


                    physicsSystems.push(bullet, rotation, weapon.bulletSpeed);

                    bullet.addToWorld();
                }
            }
        }

    }
}
