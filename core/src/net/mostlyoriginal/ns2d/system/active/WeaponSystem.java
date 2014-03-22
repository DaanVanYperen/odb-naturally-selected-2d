package net.mostlyoriginal.ns2d.system.active;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.managers.GroupManager;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
    private ComponentMapper<Anim> am;
    private ComponentMapper<Physics> ym;
    private GroupManager groupManager;
    private AfterPhysicsSystem afterPhysicsSystem;

    public WeaponSystem() {
        super(Aspect.getAspectForAll(Weapon.class, Pos.class, Bounds.class, Anim.class));
    }

    Vector2 vTmp = new Vector2();

    @Override
    protected void process(Entity e) {

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE) || Gdx.input.isButtonPressed(0)) {

            final Weapon weapon = wm.get(e);

            weapon.cooldown -= world.delta;
            if (weapon.cooldown <= 0) {
                weapon.cooldown = weapon.fireCooldown;

                final Pos pos = pm.get(e);
                final Bounds bounds = om.get(e);
                final Anim anim = am.get(e);
                Entity bullet = EntityFactory.createBullet(world, pos.x + bounds.cy(), pos.y + bounds.cy());
                groupManager.add(bullet, "bullet");

                // rotate bullet to player rotation
                float rotation = anim.rotation + MathUtils.random(-weapon.spread, weapon.spread);

                am.get(bullet).rotation = rotation;

                vTmp.set(weapon.bulletSpeed, 0).setAngle(rotation);
                ym.get(bullet).vx = vTmp.x;
                ym.get(bullet).vy = vTmp.y;

                bullet.addToWorld();
            }
        }

    }
}
