package net.mostlyoriginal.ns2d.system.active;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import net.mostlyoriginal.ns2d.component.*;

/**
 * @author Daan van Yperen
 */
@Wire
public class PlayerControlSystem extends EntityProcessingSystem {

    public static final int MOVEMENT_FACTOR = 1000;
    public static final int JUMP_FACTOR = 10000;
    public static final int JETPACK_THRUST = 1500;
    public static final int THRUST_VECTOR = 90;
    public static final float ROTATIONAL_SPEED_JETPACK_ON = 120f;
    public static final float ROTATIONAL_SPEED_JETPACK_OFF = 360f;
    private ComponentMapper<Physics> ym;
    private ComponentMapper<WallSensor> wm;
    private ComponentMapper<Pos> pm;
    private ComponentMapper<Gravity> gm;
    private ComponentMapper<Anim> am;
    private ComponentMapper<Frozen> fm;

    private EntitySpawnerSystem entitySpawnerSystem;
    private PhysicsSystem physicsSystem;

    public PlayerControlSystem()
    {
        super(Aspect.getAspectForAll(PlayerControlled.class, Physics.class, WallSensor.class, Anim.class));
    }

    Vector2 tmp = new Vector2();

    @Override
    protected void process(Entity player) {

        final Physics physics = ym.get(player);

        // frozen player does not act.
        if ( fm.has(player))
        {
            physics.vr = physics.vx = physics.vy = 0;
            return;
        }

        final WallSensor wallSensor = wm.get(player);
        final Pos pos = pm.get(player);
        final Anim anim = am.get(player);
        final Gravity gravity = gm.get(player);

        gravity.enabled = true;
        if ( wallSensor.onFloor )
        {
            anim.id = "player";

            // handle player walking. move left and right, rotation always 0.
            anim.rotation = 0;
            physics.vr = 0;

            float dx = 0;
            float dy = 0;

            if ( Gdx.input.isKeyPressed(Input.Keys.A)) dx = -MOVEMENT_FACTOR;
            if ( Gdx.input.isKeyPressed(Input.Keys.D)) dx = MOVEMENT_FACTOR;
            if ( Gdx.input.isKeyPressed(Input.Keys.W)) // jump
            {
                dy = JUMP_FACTOR;
            };

            if ( dx != 0 ) physics.vx += dx * world.delta;
            if ( dy != 0 ) physics.vy += dy * world.delta;
        } else {
            anim.id = "player-jetpack";
            // handle player flying! :D

            boolean jetPackActive = Gdx.input.isKeyPressed(Input.Keys.W);

            float turningSpeed = jetPackActive ? ROTATIONAL_SPEED_JETPACK_ON : ROTATIONAL_SPEED_JETPACK_OFF;

                // slow turning.
            int rx = 0;
            if ( Gdx.input.isKeyPressed(Input.Keys.A)) rx += turningSpeed;
            if ( Gdx.input.isKeyPressed(Input.Keys.D)) rx -= turningSpeed;

            if ( rx != 0 ) {
                physics.vr += rx * world.delta * 10f;
            }

            if ( jetPackActive )
            {
                gravity.enabled=false;
                physicsSystem.push(player,anim.rotation  + THRUST_VECTOR, JETPACK_THRUST * world.delta );
                physics.vr = MathUtils.clamp(physics.vr, -ROTATIONAL_SPEED_JETPACK_ON*2, ROTATIONAL_SPEED_JETPACK_ON*2); // clamp our rotation while accelerating.
            } else {
                gravity.enabled=true;
                physics.vr = MathUtils.clamp(physics.vr, -ROTATIONAL_SPEED_JETPACK_OFF * 2, ROTATIONAL_SPEED_JETPACK_OFF * 2);
            }
        }



    }
}
