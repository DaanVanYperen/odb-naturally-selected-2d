package net.mostlyoriginal.ns2d.system.active;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.managers.TagManager;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import net.mostlyoriginal.ns2d.component.*;
import net.mostlyoriginal.ns2d.system.passive.CollisionSystem;

/**
 * @author Daan van Yperen
 */
@Wire
public class BuildableSystem extends EntityProcessingSystem {

    ComponentMapper<Buildable> bm;
    ComponentMapper<Pos> pm;
    ComponentMapper<Anim> am;

    CollisionSystem collisionSystem;

    TagManager tagManager;
    public Entity player;

    public BuildableSystem()
    {
        super(Aspect.getAspectForAll(Buildable.class,Pos.class,Bounds.class));
    }

    @Override
    protected void begin() {
        player = tagManager.getEntity("player");
    }

    @Override
    protected void process(Entity e) {
        final Buildable buildable = bm.get(e);
        if ( !buildable.built && collisionSystem.overlaps(player, e))
        {
            if (Gdx.input.isKeyPressed(Input.Keys.SPACE) || Gdx.input.isKeyPressed(Input.Keys.E))
            {
                buildable.built = true;
                Anim anim = am.get(e);
                anim.id = buildable.builtAnimId;
                e.addComponent(new Health(100)).changedInWorld();
            }
        }
    }

    public void destroyBuildable(Entity victim) {
        final Buildable buildable = bm.get(victim);
        if ( buildable.built )
        {
            buildable.built = false;
            Anim anim = am.get(victim);
            anim.id = buildable.unbuiltAnimId;
        }
    }
}
