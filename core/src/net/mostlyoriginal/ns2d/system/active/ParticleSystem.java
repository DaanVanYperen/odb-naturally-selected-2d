package net.mostlyoriginal.ns2d.system.active;

import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import net.mostlyoriginal.ns2d.api.PassiveSystem;
import net.mostlyoriginal.ns2d.component.Anim;
import net.mostlyoriginal.ns2d.component.Pos;
import net.mostlyoriginal.ns2d.component.Terminal;
import net.mostlyoriginal.ns2d.system.passive.AssetSystem;

/**
 * @author Daan van Yperen
 */
@Wire
public class ParticleSystem extends PassiveSystem {

    public static final float EXPLOSION_FRAME_DURATION = 1/15f;

    AssetSystem assetSystem;

    public void spawnParticle(int x, int y, String particle) {

        switch ( particle )
        {
            case "explosion":
                createExplosion(x,y, 1f);
                break;
            case "tiny-explosion":
                createExplosion(x, y, 0.5f);
                break;
        }
    }

    private void createExplosion(int x, int y, float scale) {
        float speed = MathUtils.random(0.8f,1f);
        basicParticle(x, y, "particle-explosion", scale, speed)
                .addComponent(new Terminal(EXPLOSION_FRAME_DURATION * 5 * (1/speed)))
                .addToWorld();

    }

    /**
     * Spawns a particle, animation centered on x,y.
     *
     * @param x
     * @param y
     * @param animId
     * @return
     */
    private Entity basicParticle(int x, int y, String animId, float scale, float speed) {
        Anim anim = new Anim(animId);
        anim.scale=scale;
        anim.speed=speed;
        anim.color.a= 0.9f;

        TextureRegion frame = assetSystem.get(animId).getKeyFrame(0);

        return world.createEntity()
                .addComponent(new Pos(x - ((frame.getRegionWidth() * anim.scale) / 2), y - (frame.getRegionHeight() * anim.scale) / 2))
                .addComponent(anim);
    }
}
