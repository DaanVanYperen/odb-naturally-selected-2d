package net.mostlyoriginal.ns2d.system.passive;

import java.util.HashMap;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.BitmapFontData;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

import net.mostlyoriginal.api.system.core.PassiveSystem;
import net.mostlyoriginal.ns2d.component.Pos;
import net.mostlyoriginal.ns2d.system.active.ParticleSystem;
import net.mostlyoriginal.ns2d.util.EntityUtil;

/**
 * @author Daan van Yperen
 */
@Wire
public class AssetSystem extends PassiveSystem {

    public static final int TILE_SIZE = 32;
    public final BitmapFont font;
    public final BitmapFont fontLarge;

	public Texture tileset;
	public Texture tilesetNormal;

    public HashMap<String, Animation> sprites = new HashMap<String, Animation>();
    public HashMap<String, Sound> sounds = new HashMap<String, Sound>();
    private TagManager tagManager;

    ComponentMapper<Pos> pm;

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

        return add(identifier, x1, y1, w, h, repeatX, repeatY, tileset, 0.5f);
    }

    public Animation add(final String identifier, int x1, int y1, int w, int h, int repeatX, int repeatY, Texture texture, float frameDuration) {

        TextureRegion[] regions = new TextureRegion[repeatX*repeatY];

        int count = 0;
        for (int y = 0; y < repeatY; y++) {
            for (int x = 0; x < repeatX; x++) {
                regions[count++] = new TextureRegion(texture, x1 + w * x, y1 + h * y, w, h);
            }
        }

        return sprites.put(identifier, new Animation(frameDuration, regions));
    }

    public AssetSystem() {
        font = new BitmapFont(Gdx.files.internal("font/tahoma-10.fnt"), false);
        font.setColor(0, 0, 0, 0.9f);
        BitmapFontData fontLargeData = new BitmapFontData(Gdx.files.internal("font/tahoma-10.fnt"), false);
        fontLargeData.setScale(3f);
        fontLarge = new BitmapFont(fontLargeData, (TextureRegion) null, true);
        fontLarge.setColor(0, 0, 0, 0.9f);

	    tileset = new Texture("ns2d_tileset.png");
	    tilesetNormal = new Texture("ns2d_tileset_normal.png");

        add("player-idle", 0, 0, TILE_SIZE, TILE_SIZE, 2);
        add("player-jetpack", TILE_SIZE*2, 0, TILE_SIZE, TILE_SIZE, 1);
        add("player-walk", TILE_SIZE*3, 0, TILE_SIZE, TILE_SIZE, 4,1,tileset,0.2f);
        add("player-respawning", TILE_SIZE*7, 0, TILE_SIZE, TILE_SIZE, 1);


        add("spawner", 0, 464, 16,16, 1);
        add("resourcetower", 0, 336, 16*3, 16*3, 1);
        add("armory", 96, 336, 16*3, 16*3, 1);

        add("spawner-unbuilt", 16, 464, 16,16, 1);
        add("resourcetower-unbuilt", 16*3, 336, 16*3, 16*3, 1);
        add("armory-unbuilt", 96+16*3, 336, 16*3, 16*3, 1);

        add("techpoint", 0, 384, TILE_SIZE*2, TILE_SIZE*2, 1);

        add("duct", 192, 384, 48, 48, 1);
        add("duct-hot", 192+48, 384, 48, 48, 4, 1, tileset, 1/3f);

        add("skulk", TILE_SIZE, 592, TILE_SIZE, 16, 2);
        add("skulk-head", 105, 590, 15, 10, 1);
        add("gorge", 32, 621, 32, 19, 2);
        add("gorge-head", 106,623, 12,12, 1);
        add("gorge-spit",171,619,10,10,1);
        add("bile-droplet",200,620,13,8,1);
        add("babbler", 0, 654, 32, 18, 2);

        add("debug-marker", 44, 51, 3, 3, 1);

        add("alert-arrow", 64, 512, 32, 31, 1);
        add("alert-skulk", 103, 524, 21, 11, 1);
        add("alert-radar", 135, 519, 18, 18, 1);
        add("alert-damage", 167, 519, 18, 18, 1);

        add("particle-alienblood", 256, 144, 16, 16, 18);

        add("techpoint-alert", 65, 401, 62, 47, 1);

        add("resource", 43, 523, 10, 10, 1);

        add("bullet", 43, 45, 9, 6, 1);
        add("slug", 44, 76, 8, 8, 1);
        add("grenade", 42, 108, 12, 8, 1);
        add("flames", 32, 128, 16, 16, 6,2);

        add("health-tick", 14, 526, 4, 5, 1);

        add("particle-explosion", 128, 32, 64,64, 5, 1, tileset, ParticleSystem.EXPLOSION_FRAME_DURATION);
        add("particle-muzzleflare", 99, 38, 26,19, 1, 1);
        add("particle-bulletcasing", 75, 45, 10,5, 1, 1);
        add("particle-shellcasing", 72, 77, 15,7, 1, 1);
        add("particle-debris", 256, 128, 16, 16, 10);
        add("particle-jetpack", 128, 144, 16, 16, 4, 1, tileset, 0.1f);
//        add("particle-jetpack", 128, 128, 16, 16, 4, 1, tileset, 0.1f);
        add("particle-puff", 192, 144, 16, 16, 4, 1);

        add("speech-bubble-left", 0, 544, 16, 38, 1);
        add("speech-bubble-middle", 16, 544, 16, 38, 1);
        add("speech-bubble-right", 32, 544, 16, 38, 1);

        add("sentry-frame",64, 448, 32, 32, 2);
        add("sentry-frame-unbuilt",128, 448, 32, 32, 1);
        add("sentry",256, 448, 32, 32, 2);
        add("sentry-unbuilt",256+32, 448, 32, 32, 1);

        add("sentry2-frame",64+256, 448, 32, 32, 2);
        add("sentry2-frame-unbuilt",128+256, 448, 32, 32, 1);
        add("sentry2",256+256, 448, 32, 32, 2);
        add("sentry2-unbuilt",256+32+256, 448, 32, 32, 1);

        add("rifle", 0, 32, TILE_SIZE, TILE_SIZE, 1);
        add("shotgun", 0, 64, TILE_SIZE, TILE_SIZE, 1);
        add("grenadelauncher", 0, 96, TILE_SIZE, TILE_SIZE, 1);
        add("flamethrower", 0, 128, TILE_SIZE, TILE_SIZE, 1);
        
        loadSounds(new String[] {
                "ns2d_sfx_bullet_casing1",
                "ns2d_sfx_bullet_casing2",
                "ns2d_sfx_bullet_casing3",
                "ns2d_sfx_gl_explode",
                "ns2d_sfx_gl_fire",
                "ns2d_sfx_lmg_fire2",
                "ns2d_sfx_sentry_fire",
                "ns2d_sfx_shotgun_fire",
                "ns2d_sfx_skulk_die1",
                "ns2d_sfx_skulk_die2",
                "ns2d_sfx_skulk_die3",
                "ns2d_sfx_structure_damage1",
                "ns2d_sfx_structure_damage2",
                "ns2d_sfx_structure_damage3",
                "ns2d_sfx_gorge_die1",
                "ns2d_sfx_gorge_die2",
                "ns2d_sfx_gorge_die3",
                "ns2d_sfx_jetpack_loop",
                "ns2d_sfx_jetpack_start",
                "ns2d_sfx_gorge_bile",
                "ns2d_sfx_construct",
                "ns2d_sfx_pickup",
        });
    }


    private void loadSounds(String[] soundnames) {
        for (String identifier : soundnames) {
            sounds.put(identifier, Gdx.audio.newSound(Gdx.files.internal("sfx/" + identifier + ".mp3")));
        }
    }

    private float sfxVolume = 0.2f;
    public void playSfx(String name) {
        if (sfxVolume > 0 )
        {
            Sound sfx = getSfx(name);
            sfx.stop();
            sfx.play(sfxVolume, MathUtils.random(1f, 1.04f), 0);
        }
    }

    public void playSfx(String name, Entity origin) {
        if (sfxVolume > 0 )
        {
            Entity player = tagManager.getEntity("player");
            float distance = EntityUtil.distance(origin, player);

            float volume = sfxVolume - (distance / 2000f);
            if ( volume > 0.01f )
            {
                float balanceX = pm.has(origin) && pm.has(player) ? MathUtils.clamp((pm.get(origin).x - pm.get(player).x)/100f, -1f,1f) : 0;
                Sound sfx = getSfx(name);
                sfx.stop();
                sfx.play(volume, MathUtils.random(1f, 1.04f), balanceX);
            }
        }
    }

    public void dispose() {
        sprites.clear();
        tileset.dispose();
        tileset = null;
    }

}
