package net.mostlyoriginal.ns2d.api;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.managers.GroupManager;
import com.artemis.utils.Bag;
import com.artemis.utils.ImmutableBag;

/**
 * @author Daan van Yperen
 */
public abstract class DualEntityProcessSystem extends EntitySystem {

    private final String groupA;
    private final String groupB;

    private ImmutableBag<Entity> groupEntitiesA;
    private ImmutableBag<Entity> groupEntitiesB;

    protected Bag<Entity> deleteLater = new Bag<>();

    public DualEntityProcessSystem(String groupA, String groupB) {
        super(Aspect.getEmpty());
        this.groupA = groupA;
        this.groupB = groupB;
    }

    @Override
    protected void processEntities(ImmutableBag<Entity> entities) {
        for (int a = groupEntitiesA.size()-1; a >= 0; a--) {
            for (int b = groupEntitiesB.size()-1; b >= 0; b--) {
                final Entity entityA = groupEntitiesA.get(a);
                final Entity entityB = groupEntitiesB.get(b);
                if ( entityA != null && entityB != null ) {
                    processEntity(entityA, entityB);
                }
            }
        }
    }

    @Override
    protected boolean checkProcessing() {
        return true;
    }

    @Override
    protected void initialize() {
        groupEntitiesA = world.getManager(GroupManager.class).getEntities(groupA);
        groupEntitiesB = world.getManager(GroupManager.class).getEntities(groupB);
    }

    protected abstract void processEntity(Entity entityA, Entity entityB);

}
