package net.mostlyoriginal.ns2d.system.passive;

import com.badlogic.gdx.maps.MapProperties;
import net.mostlyoriginal.ns2d.util.EntityFactory;

/**
 * @author Daan van Yperen
 */
public class EntitySpawnSystem extends PassiveSystem {

    public void spawnEntity(float x, float y, MapProperties properties) {

        final String entity = (String) properties.get("entity");

        switch ( entity )
        {
            case "player" :
                EntityFactory.createPlayer(world, x, y).addToWorld();
                break;
            case "spawner" :
                EntityFactory.createSpawner(world, x, y).addToWorld();
                break;
            case "duct" :
                EntityFactory.createDuct(world, x, y).addToWorld();
                break;
            default:
                throw new RuntimeException("No idea how to spawn entity of type " + entity);
        }
    }
}
