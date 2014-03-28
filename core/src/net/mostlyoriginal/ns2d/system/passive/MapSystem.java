package net.mostlyoriginal.ns2d.system.passive;

import com.artemis.annotations.Wire;
import com.artemis.systems.VoidEntitySystem;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.utils.Array;
import net.mostlyoriginal.ns2d.G;
import net.mostlyoriginal.ns2d.system.active.EntitySpawnerSystem;
import net.mostlyoriginal.ns2d.util.MapMask;

/**
 * Handles map loading.
 *
 * @author Daan van Yperen
 */
@Wire
public class MapSystem extends VoidEntitySystem {

    public TiledMap map;
    private int width;
    private int height;
    private Array<TiledMapTileLayer> layers;
    private boolean isSetup;

    private EntitySpawnerSystem entitySpawnerSystem;

    @Override
    protected void initialize() {
        map = new TmxMapLoader().load("map1.tmx");

        layers = new Array<TiledMapTileLayer>();
        for ( MapLayer rawLayer : map.getLayers() )
        {
            layers.add((TiledMapTileLayer) rawLayer);
        }
        width = layers.get(0).getWidth();
        height = layers.get(0).getHeight();
    }

    public MapMask getMask( String property )
    {
        return new MapMask(height, width, layers, property);
    }

    /**
     * Spawn map entities.
     */
    protected void setup() {
        for (TiledMapTileLayer layer : layers) {

//            private HashMap<String, TiledMapTileLayer> layerIndex = new HashMap<String, TiledMapTileLayer>();
//            layerIndex.put(layer.getName(), layer);

            for (int ty = 0; ty < height; ty++) {
                for (int tx = 0; tx < width; tx++) {
                    final TiledMapTileLayer.Cell cell = layer.getCell(tx, ty);
                    if (cell != null) {
                        final MapProperties properties = cell.getTile().getProperties();

                        if ( properties.containsKey("entity")) {
                            entitySpawnerSystem.spawnEntity(tx * G.CELL_SIZE, ty * G.CELL_SIZE, properties);
                            layer.setCell(tx, ty, null);
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void processSystem() {

        if ( !isSetup )
        {
            isSetup = true;
            setup();
        }
    }

}
