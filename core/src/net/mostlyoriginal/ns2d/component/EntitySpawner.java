package net.mostlyoriginal.ns2d.component;

import com.artemis.Component;

/**
 * @author Daan van Yperen
 */
public class EntitySpawner extends Component {

    public String entityId;
    public String entityId2;
    public int entityId2Chance = 10;
    public float cooldown = -1;

    public float minInterval = 2;
    public float maxInterval = 10;
    public int  minCount    = 1;
    public int maxCount    = 5;
    public boolean enabled = false;

    public EntitySpawner(String entityId) {
        this.entityId = entityId;
    }

    public EntitySpawner(String entityId, String entityId2) {
        this.entityId = entityId;
        this.entityId2 = entityId2;
    }
}
