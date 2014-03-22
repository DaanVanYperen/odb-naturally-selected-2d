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
    private TagManager tagManager;
    private AimSystem aimSystem;
    private PhysicsSystem physicsSystem;
    private GroupManager groupManager;

    public Entity player;
    public Pos enemyPos;

    public SkulkControlSystem()
    {
        super(Aspect.getAspectForAll(SkulkControlled.class, WallSensor.class, Anim.class, Physics.class, Focus.class));
    }

    @Override
    protected void begin() {
        super.begin();
    }

    private Entity determineFocus(Entity skulk) {
        Focus focus = fm.get(skulk);

        float closestDistance = -1;

        if ( focus.entity == null || !focus.entity.isActive() )
        {
            focus.entity = tagManager.getEntity("player");
        }

        if ( focus.entity != null )
        {
            closestDistance = EntityUtil.distance2( skulk,focus.entity);
        }

        ImmutableBag<Entity> entities = groupManager.getEntities("player-structure");
        for ( int i=0; entities.size() > i; i++ )
        {
            final Entity b = entities.get(i);
            final float distance = b != null ? EntityUtil.distance2( skulk, b) : -1;
            if ( distance != -1 && (closestDistance == -1 || distance < closestDistance) )
            {
                focus.entity = b;
                closestDistance = distance;
            }
        }

        return focus.entity;
    }

    @Override
    protected void process(Entity skulk) {


        Entity focus = determineFocus(skulk);
        enemyPos = pm.get(focus);

        WallSensor sensor = wm.get(skulk);
        Physics physics = ym.get(skulk);
        Pos skulkPos = pm.get(skulk);

        // 1. crawl along the surface of wherever they are, in the direction of the focus.
        // 2.

        Gravity gravity = gm.get(skulk);
        gravity.enabled = !sensor.onVerticalSurface && !sensor.onHorizontalSurface;

        float enemyDirX =enemyPos.x- skulkPos.x;
        float enemyDirY = enemyPos.y- skulkPos.y;

        float playerDistance = EntityUtil.distance2(skulk, focus);
        if ( playerDistance < 300*300 && playerDistance > 50*50 && sensor.onAnySurface() && Math.abs(physics.vx) < 10 )
        {
            // aim and fire!
            float direction = EntityUtil.angle( skulk, focus );
            physicsSystem.push(skulk, direction, MathUtils.clamp(playerDistance, 40, 600));
           //physicsSystem.push(skulk, 90, 200); // slight upforce each jump
        } else if ( sensor.onAnySurface() ) {

            float dx = 0;
            float dy = 0;

            if ( enemyDirX < -APPROACH_RANGE && sensor.onHorizontalSurface ) dx = -1;
            if ( enemyDirX > APPROACH_RANGE && sensor.onHorizontalSurface ) dx = 1;
            if ( enemyDirY < 0 && sensor.onVerticalSurface ) dy = -1;
            if ( enemyDirY > 0 && sensor.onVerticalSurface ) dy = 1;

            physics.vx = dx * 100;
            if ( dy != 0 ) physics.vy = dy * 100;
        }
//        if ( sensor.onFloor && MathUtils.random(100) < 10f ) dy = 1;


    }

}
