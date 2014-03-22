package net.mostlyoriginal.ns2d.system.active;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import net.mostlyoriginal.ns2d.component.Gravity;
import net.mostlyoriginal.ns2d.component.Physics;
import net.mostlyoriginal.ns2d.component.PlayerControlled;
import net.mostlyoriginal.ns2d.component.Pos;

/**
 * @author Daan van Yperen
 */
@Wire
public class PlayerControlSystem extends EntityProcessingSystem {

    public static final int MOVEMENT_FACTOR = 250;
    public static final int JUMP_FACTOR = 1000;
    private ComponentMapper<Physics> ym;
    private ComponentMapper<Pos> pm;

    private EntitySpawnerSystem entitySpawnerSystem;

    public PlayerControlSystem()
    {
        super(Aspect.getAspectForAll(PlayerControlled.class, Physics.class));
    }

    @Override
    protected void process(Entity e) {
        final Physics physics = ym.get(e);
        final Pos pos = pm.get(e);

        float dx = 0;
        float dy = 0;

        if ( Gdx.input.isKeyPressed(Input.Keys.A)) dx = -MOVEMENT_FACTOR;
        if ( Gdx.input.isKeyPressed(Input.Keys.D)) dx = MOVEMENT_FACTOR;
        if ( Gdx.input.isKeyPressed(Input.Keys.W))
        {
            if ( physics.onFloor )
            {
                // jump.
                dy = JUMP_FACTOR * 10f;
            } else {
                dy = (-Gravity.DEFAULT_Y_GRAVITY + 1.5f) * GravitySystem.GRAVITY_FACTOR;
            }
        };

        if ( Gdx.input.isKeyPressed(Input.Keys.SPACE))
        {
            entitySpawnerSystem.spawnEntity(pos.x, pos.y, "bullet");
        }

        if ( dx != 0 ) physics.vx += dx * world.delta;
        if ( dy != 0 ) physics.vy += dy * world.delta;
    }
}
