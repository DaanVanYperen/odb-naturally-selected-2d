package net.mostlyoriginal.ns2d.system.render;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import net.mostlyoriginal.api.system.delegate.DeferredEntityProcessingSystem;
import net.mostlyoriginal.api.system.delegate.EntityProcessPrincipal;
import net.mostlyoriginal.ns2d.component.Anim;
import net.mostlyoriginal.ns2d.component.Pos;
import net.mostlyoriginal.ns2d.system.passive.AssetSystem;
import net.mostlyoriginal.ns2d.system.passive.CameraSystem;

/**
 * @author Daan van Yperen
 */
@Wire
public class AnimRenderSystem extends DeferredEntityProcessingSystem {

    private ComponentMapper<Pos> pm;
    private ComponentMapper<Anim> sm;
    private CameraSystem cameraSystem;
    private AssetSystem assetSystem;

    private SpriteBatch batch;

    private float age;
    public final ShaderProgram shimmerProgram;

    public AnimRenderSystem(EntityProcessPrincipal principal) {
        super(Aspect.getAspectForAll(Pos.class, Anim.class), principal);

        shimmerProgram = new ShaderProgram(Gdx.files.internal("shader/shimmer.vertex"), Gdx.files.internal("shader/shimmer.fragment"));
        if ( !shimmerProgram.isCompiled() ) throw new RuntimeException("Compilation failed." + shimmerProgram.getLog());
        batch  = new SpriteBatch(2000, shimmerProgram);
    }

    @Override
    protected void begin() {

        age += world.delta;

        batch.setProjectionMatrix(cameraSystem.camera.combined);
        batch.begin();
        shimmerProgram.setUniformf("iGlobalTime", age);
        batch.setColor(1f, 1f, 1f, 1f);
    }

    @Override
    protected void end() {
        batch.end();
    }

    protected void process(final Entity entity) {

        final Anim anim = sm.get(entity);
        final Pos pos = pm.get(entity);

        anim.age += world.delta * anim.speed;

        batch.setColor( anim.color );
        drawAnimation(anim, pos, anim.id);
    }

    private void drawAnimation(final Anim animation, final Pos position, String id) {

        final com.badlogic.gdx.graphics.g2d.Animation gdxanim = assetSystem.get(id);
        if ( gdxanim == null) return;

        final TextureRegion frame = gdxanim.getKeyFrame(animation.age, true);

        if ( animation.flippedX)
        {
            batch.draw(frame.getTexture(),
                    (int)position.x,
                    (int)position.y,
                    animation.ox == Anim.ORIGIN_AUTO ? frame.getRegionWidth() * animation.scale * 0.5f : animation.ox,
                    animation.oy == Anim.ORIGIN_AUTO ? frame.getRegionHeight() * animation.scale * 0.5f : animation.oy,
                    frame.getRegionWidth() * animation.scale,
                    frame.getRegionHeight() * animation.scale,
                    1f,
                    1f,
                    animation.rotation,
                    frame.getRegionX(),
                    frame.getRegionY(),
                    frame.getRegionWidth(),
                    frame.getRegionHeight(),
                    true,
                    false);

        } else
        if ( animation.rotation != 0 )
        {
            batch.draw(frame,
                    (int)position.x,
                    (int)position.y,
                    animation.ox == Anim.ORIGIN_AUTO ? frame.getRegionWidth() * animation.scale * 0.5f : animation.ox,
                    animation.oy == Anim.ORIGIN_AUTO ? frame.getRegionHeight() * animation.scale * 0.5f : animation.oy,
                    frame.getRegionWidth() * animation.scale,
                    frame.getRegionHeight() * animation.scale, 1, 1,
                    animation.rotation);
        } else {
            batch.draw(frame,
                    (int)position.x,
                    (int)position.y,
                    frame.getRegionWidth() * animation.scale,
                    frame.getRegionHeight() * animation.scale);
        }
    }

    @Override
    protected boolean checkProcessing() {
        return true;
    }
}
