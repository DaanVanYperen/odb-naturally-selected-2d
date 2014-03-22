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

/**
 * @author Daan van Yperen
 */
@Wire
public class PlayerControlSystem extends EntityProcessingSystem {

    private ComponentMapper<Physics> pm;

    public PlayerControlSystem()
    {
        super(Aspect.getAspectForAll(PlayerControlled.class, Physics.class));
    }

    @Override
    protected void process(Entity e) {
        Physics physics = pm.get(e);

        float dx = 0;
        float dy = 0;

        if ( Gdx.input.isKeyPressed(Input.Keys.A)) dx = -1;
        if ( Gdx.input.isKeyPressed(Input.Keys.D)) dx = 1;
        if ( Gdx.input.isKeyPressed(Input.Keys.S)) dy = -1;
        if ( Gdx.input.isKeyPressed(Input.Keys.W)) dy = 1;

        if ( dx != 0 ) physics.vx = dx * 5;
        if ( dy != 0 ) physics.vy = dy * 5;
    }
}
