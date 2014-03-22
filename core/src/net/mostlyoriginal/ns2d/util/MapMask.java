package net.mostlyoriginal.ns2d.util;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.utils.Array;
import net.mostlyoriginal.ns2d.G;

/**
 * Creates a mask based on tiles with a certain propertyKey.
 *
 * @author Daan van Yperen
 */
public class MapMask {

    public final boolean[][] v;
    public final int height;
    public final int width;

    public MapMask(int height, int width, Array<TiledMapTileLayer> layers, String propertyKey) {
        this.height = height;
        this.width = width;

        v = new boolean[height][width];
        generate(layers, propertyKey);
    }

    /**
     * @param x grid coordinates
     * @param y grid coordinates.
     * @return TRUE when property found at TILE coordinates, FALSE if otherwise or out of bounds.
     */
    public boolean atGrid( final int x, final int y )
    {
        if ( x > width || x < 0 || y < 0 || y > height  ) return false;
        return v[y][x];
    }

    /**
     *
     * @param x
     * @param y
     * @return TRUE when property found at PIXEL coordinates.
     */
    public boolean atScreen( final int x, final int y)
    {
        return atGrid((int)(x / G.CELL_SIZE),(int)(y / G.CELL_SIZE));
    }

    public boolean atScreen( final float x, final float y)
    {
        return atGrid((int)((int)x / G.CELL_SIZE),(int)((int)y / G.CELL_SIZE));
    }

    private void generate(Array<TiledMapTileLayer> layers, String propertyKey) {
        for (TiledMapTileLayer layer : layers) {
            for (int ty = 0; ty < height; ty++) {
                for (int tx = 0; tx < width; tx++) {
                    final TiledMapTileLayer.Cell cell = layer.getCell(tx, ty);
                    if ( cell != null && cell.getTile() != null && cell.getTile().getProperties().containsKey(propertyKey)) {
                        v[ty][tx] = true;
                    }
                }
            }
        }
    }
}

