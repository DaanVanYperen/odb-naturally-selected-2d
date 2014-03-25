package net.mostlyoriginal.ns2d.system.render;

/**
 * @author Daan van Yperen
 */

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.managers.TagManager;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import net.mostlyoriginal.ns2d.component.*;
import net.mostlyoriginal.ns2d.system.passive.AssetSystem;
import net.mostlyoriginal.ns2d.system.passive.CameraSystem;

@Wire
public class UIAlertBuildableUnderAttack extends EntityProcessingSystem {

    public Entity player;
    public TextureRegion radarImage;
    public TextureRegion arrowImage;
    private DialogRenderSystem dialogSystem;

    public UIAlertBuildableUnderAttack() {
        super(Aspect.getAspectForAll(Buildable.class, Health.class, Pos.class, Bounds.class));
    }

    private ComponentMapper<Pos> pm;
    private ComponentMapper<Bounds> bm;
    private ComponentMapper<Critical> cm;
    private ComponentMapper<Buildable> bum;

    private CameraSystem cameraSystem;
    private AssetSystem assetSystem;
    private SpriteBatch batch = new SpriteBatch();

    private TagManager tagManager;

    @Override
    protected void begin() {
        batch.setProjectionMatrix(cameraSystem.camera.combined);
        batch.begin();
        batch.setColor(1f, 1f, 1f, 1f);

        player = tagManager.getEntity("player");
        arrowImage = assetSystem.get("alert-arrow").getKeyFrame(0);
        radarImage = assetSystem.get("alert-damage").getKeyFrame(0);
    }

    @Override
    protected void end() {
        batch.end();
    }

    Vector2 vTmp = new Vector2();
    float age;
    float lastAlertCooldown = 0;

    @Override
    protected void process(Entity spawner) {

        lastAlertCooldown -= world.delta;
        Buildable critical = bum.get(spawner);
        critical.damageAge += world.delta;
        age += world.delta;
        if ( critical.damageAge > 1 || (age % 2 < 1f) )
            return;

        if ( lastAlertCooldown < 0 )
        {
            lastAlertCooldown = 40;
            dialogSystem.randomSay(DialogRenderSystem.NON_CRITICAL_ALERT_MESSAGES);
        }

        final Pos pPos = pm.get(player);
        final Bounds pBounds = bm.get(player);
        final Pos sPos = pm.get(spawner);
        final Bounds sBounds = bm.get(spawner);

        final float cx = sPos.x + sBounds.cx();
        final float cy = sPos.y + sBounds.cy();

        final float pcx = pPos.x + pBounds.cx();
        final float pcy = pPos.y + pBounds.cy();

        vTmp.set(cx, cy).sub(pcx, pcy);
        float distance = vTmp.len();
        vTmp.nor().scl(Gdx.graphics.getHeight() * 0.2f);
        float angle = vTmp.angle() - 90;
        vTmp.add(pcx, pcy);

        TextureRegion frame = arrowImage;

        batch.setColor(1f,1f,1f,MathUtils.clamp((distance-50)/100f,0f,1f));
        batch.draw(
                frame,
                vTmp.x - frame.getRegionWidth() / 2,
                vTmp.y - frame.getRegionHeight() / 2,
                frame.getRegionWidth() / 2,
                frame.getRegionHeight() / 2,
                frame.getRegionWidth(), frame.getRegionHeight(),
                1, 1, angle);

        vTmp.set(cx, cy).sub(pcx, pcy).nor().scl(Gdx.graphics.getHeight() * 0.18f).add(pcx, pcy);
        frame = radarImage;
        batch.draw(frame, vTmp.x - frame.getRegionWidth() / 2, vTmp.y - frame.getRegionHeight() / 2);
        //cameraSystem.camera;
    }
}
