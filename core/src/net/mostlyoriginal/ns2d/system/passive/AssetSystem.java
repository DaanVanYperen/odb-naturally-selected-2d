package net.mostlyoriginal.ns2d.system.passive;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.HashMap;

/**
 * @author Daan van Yperen
 */
public class AssetSystem extends PassiveSystem {

    public static final int TILE_SIZE = 32;
    public final BitmapFont font;

    public Texture tileset;
    public HashMap<String, Animation> sprites = new HashMap<String, Animation>();
    public HashMap<String, Sound> sounds = new HashMap<String, Sound>();

    public Animation get(final String identifier) {
        return sprites.get(identifier);
    }

    public Sound getSfx(final String identifier) {
        return sounds.get(identifier);
    }

    public Animation add(final String identifier, int x1, int y1, int w, int h, int repeatX) {
        return add(identifier, x1, y1, w, h, repeatX, 1, tileset);
    }

    public Animation add(final String identifier, int x1, int y1, int w, int h, int repeatX, int repeatY) {
        return add(identifier, x1, y1, w, h, repeatX, repeatY, tileset);
    }

    public Animation add(final String identifier, int x1, int y1, int w, int h, int repeatX, int repeatY, Texture texture) {

        TextureRegion[] regions = new TextureRegion[repeatX*repeatY];

        int count = 0;
        for (int y = 0; y < repeatY; y++) {
            for (int x = 0; x < repeatX; x++) {
                regions[count++] = new TextureRegion(texture, x1 + w * x, y1 + h * y, w, h);
            }
        }

        return sprites.put(identifier, new Animation(0.5f, regions));
    }

    public AssetSystem() {

        font = new BitmapFont(Gdx.files.internal("font/tahoma-10.fnt"), false);
        font.setColor(0, 0, 0, 0.9f);

        tileset = new Texture("ns2d_tileset.png");

        add("player", 0, 0, TILE_SIZE, TILE_SIZE, 1);
        add("duct", 32, 224, TILE_SIZE, TILE_SIZE, 1);
        add("spawner", 0, 448, TILE_SIZE, TILE_SIZE, 1);
        add("skulk", 0, 64, TILE_SIZE, TILE_SIZE, 1);
        add("debug-marker", 44, 51, 3, 3, 1);
    }


    private void loadSounds(String[] soundnames) {
        for (String identifier : soundnames) {
            sounds.put(identifier, Gdx.audio.newSound(Gdx.files.internal("sfx/" + identifier + ".mp3")));
        }
    }

    public void dispose() {
        sprites.clear();
        tileset.dispose();
        tileset = null;
    }

}
