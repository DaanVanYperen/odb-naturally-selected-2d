package net.mostlyoriginal.ns2d.system.active;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import net.mostlyoriginal.ns2d.component.Physics;
import net.mostlyoriginal.ns2d.component.PlayerControlled;
import net.mostlyoriginal.ns2d.component.Pos;

/**
 * @author Daan van Yperen
 */
@Wire
public class PlayerControlSystem extends EntityProcessingSystem {

    private ComponentMapper<Physics> ym;
    private ComponentMapper<Pos> pm;

    private EntitySpawnerSystem entitySpawnerSystem;

    public PlayerControlSystem()
    {
        super(Aspect.getAspectForAll(PlayerControlled.class, Physics.class));
    }

    @Override
    protected void process(Entity e) {
        Physics physics = ym.get(e);

        float dx = 0;
        float dy = 0;

        if ( Gdx.input.isKeyPressed(Input.Keys.A)) dx = -1;
        if ( Gdx.input.isKeyPressed(Input.Keys.D)) dx = 1;
        if ( physics.onFloor && Gdx.input.isKeyPressed(Input.Keys.W)) dy = 1;

        if ( Gdx.input.isKeyPressed(Input.Keys.SPACE))
        {
            Pos pos = pm.get(e);
            entitySpawnerSystem.spawnEntity(pos.x, pos.y, "bullet");
        }


        if ( dx != 0 ) physics.vx = dx * 200;
        if ( dy != 0 ) physics.vy = dy * 500;
    }
}
