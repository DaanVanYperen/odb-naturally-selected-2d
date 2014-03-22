package net.mostlyoriginal.ns2d.system.active;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.math.MathUtils;
import net.mostlyoriginal.ns2d.component.Physics;
import net.mostlyoriginal.ns2d.component.SkulkControlled;

/**
 * @author Daan van Yperen
 */
@Wire
public class SkulkControlSystem extends EntityProcessingSystem {

    private ComponentMapper<Physics> pm;

    public SkulkControlSystem()
    {
        super(Aspect.getAspectForAll(SkulkControlled.class, Physics.class));
    }

    @Override
    protected void process(Entity e) {
        Physics physics = pm.get(e);

        float dx = 0;
        float dy = 0;

        if ( MathUtils.random(100) < 10f ) dx = -1;
        if ( MathUtils.random(100) < 10f ) dx = 1;
        if ( physics.onFloor && MathUtils.random(100) < 10f ) dy = 1;

        if ( dx != 0 ) physics.vx = dx * 200;
        if ( dy != 0 ) physics.vy = dy * 500;
    }
}
