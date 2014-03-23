package net.mostlyoriginal.ns2d.system.active;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.managers.GroupManager;
import com.artemis.managers.TagManager;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.math.MathUtils;
import net.mostlyoriginal.ns2d.component.*;
import net.mostlyoriginal.ns2d.util.EntityUtil;

/**
 * @author Daan van Yperen
 */
@Wire
public class SkulkControlSystem extends EntityProcessingSystem {

    public static final int APPROACH_RANGE = 32;
    public static final int MIN_LEAP_DISTANCE = 100;
    private ComponentMapper<WallSensor> wm;
    private ComponentMapper<Gravity> gm;
    private ComponentMapper<Physics> ym;
    private ComponentMapper<Pos> pm;
    private ComponentMapper<Anim> am;
    private ComponentMapper<Focus> fm;
    private ComponentMapper<Weapon> weam;
    private ComponentMapper<Aim> a2m;
    private ComponentMapper<Inventory> im;
    private ComponentMapper<SkulkControlled> com;
    private TagManager tagManager;
    private AimSystem aimSystem;
    private PhysicsSystem physicsSystem;
    private GroupManager groupManager;

    public Entity player;
    public Pos enemyPos;
    private CombatSystem combatSystem;
    private ComponentMapper<Buildable> bm;
    private ComponentMapper<Health> hm;
    public ImmutableBag<Entity> playerFriends;

    public SkulkControlSystem() {
        super(Aspect.getAspectForAll(SkulkControlled.class, WallSensor.class, Anim.class, Physics.class, Focus.class));
    }

    @Override
    protected void initialize() {
        super.initialize();
        playerFriends = groupManager.getEntities("player-friend");
    }

    @Override
    protected void begin() {
        super.begin();
    }

    private Entity determineFocus(Entity skulk) {

        final Focus focus = fm.get(skulk);
        float closestDistance = -1;

        // lose interest in things without health.
        if (focus.entity != null && !hm.has(focus.entity)) {
            focus.entity = null;
        }

        if (focus.entity != null) {
            closestDistance = EntityUtil.distance2(skulk, focus.entity);
        }

        // check all valid victims.
        for ( int i = 0; playerFriends.size() > i; i++)
        {
            final Entity b = playerFriends.get(i);

            // we don't care about targets without health.
            if (b == null || !hm.has(b))
                continue;


            final float distance = EntityUtil.distance2(skulk, b);
            if (closestDistance == -1 || distance < closestDistance) {
                focus.entity = b;
                closestDistance = distance;
            }
        }

        return focus.entity;
    }

    @Override
    protected void process(Entity skulk) {

        SkulkControlled controlled = com.get(skulk);
        controlled.leapCooldown -= world.delta;


        Entity focus = determineFocus(skulk);
        if (focus != null) {
            stalk(skulk, focus);
        } else {
            wander(skulk);
        }


    }

    private void wander(Entity skulk) {

        final WallSensor sensor = wm.get(skulk);
        final Physics physics = ym.get(skulk);
        final Pos skulkPos = pm.get(skulk);

        float tx = skulkPos.x;
        float ty = skulkPos.y;

        aimHeadAtFocus(skulk, null);

        if (sensor.onVerticalSurface) {
            ty += 100;

            // randomly leap
            SkulkControlled controlled = com.get(skulk);
            if (controlled.leapCooldown <= 0) {
                leapTowards(skulk, MathUtils.random(-360, 360), MathUtils.random(100, 600));
            }
        }

        if (sensor.onHorizontalSurface) {
            tx += 100;
        }


        walkTowards(sensor, physics, tx, ty);
    }

    private void stalk(Entity skulk, Entity focus) {
        enemyPos = pm.get(focus);

        aimHeadAtFocus(skulk, focus);

        final WallSensor sensor = wm.get(skulk);
        final Physics physics = ym.get(skulk);
        final Pos skulkPos = pm.get(skulk);

        // 1. crawl along the surface of wherever they are, in the direction of the focus.
        // 2.

        Gravity gravity = gm.get(skulk);
        gravity.enabled = !sensor.onVerticalSurface && !sensor.onHorizontalSurface;

        float enemyDirX = enemyPos.x - skulkPos.x;
        float enemyDirY = enemyPos.y - skulkPos.y;

        float enemyDistance = EntityUtil.distance2(skulk, focus);

        SkulkControlled controlled = com.get(skulk);

        if (sensor.onAnySurface() && controlled.leapCooldown <= 0) {
            float direction = EntityUtil.angle(skulk, focus) + MathUtils.random(-10f,10f);
            leapTowards(skulk, direction, enemyDistance);
        } else if (sensor.onAnySurface()) {
            walkTowards(sensor, physics, enemyDirX, enemyDirY);
        }
    }

    private void leapTowards(Entity skulk, float direction, float distance) {
        // aim and fire!
        SkulkControlled controlled = com.get(skulk);
        controlled.leapCooldown = MathUtils.random(1f, 1.5f);
        physicsSystem.push(skulk, direction, 3*MathUtils.clamp(distance, 100, 250));

        final WallSensor sensor = wm.get(skulk);
        physicsSystem.push(skulk, sensor.wallAngle-180, 100);
    }

    private void walkTowards(WallSensor sensor, Physics physics, float enemyDirX, float enemyDirY) {
        float dx = 0;
        float dy = 0;

        if (enemyDirX < -APPROACH_RANGE && sensor.onHorizontalSurface) dx = -1;
        if (enemyDirX > APPROACH_RANGE && sensor.onHorizontalSurface) dx = 1;
        if (enemyDirY < 0 && sensor.onVerticalSurface) dy = -1;
        if (enemyDirY > 0 && sensor.onVerticalSurface) dy = 1;

        physics.vx = dx * 100;
        if (dy != 0) physics.vy = dy * 100;
    }

    private void aimHeadAtFocus(Entity skulk, Entity focus) {
        Inventory inventory = im.get(skulk);
        if (inventory.weapon != null && inventory.weapon.isActive()) {
            Aim aim = a2m.get(inventory.weapon);
            aim.at = focus;
            weam.get(inventory.weapon).firing = (focus != null);
        }
    }

}
