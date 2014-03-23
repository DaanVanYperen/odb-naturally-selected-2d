package net.mostlyoriginal.ns2d.system.render;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.managers.TagManager;
import com.artemis.systems.VoidEntitySystem;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import net.mostlyoriginal.ns2d.component.Wallet;
import net.mostlyoriginal.ns2d.system.passive.AssetSystem;
import net.mostlyoriginal.ns2d.system.passive.CameraSystem;

/**
 * @author Daan van Yperen
 */
@Wire
public class UIRenderSystem extends VoidEntitySystem {

    private CameraSystem cameraSystem;
    private AssetSystem assetSystem;
    private SpriteBatch batch = new SpriteBatch();

    private TagManager tagManager;

    private ComponentMapper<Wallet> wm;

    @Override
    protected void processSystem() {
        batch.setProjectionMatrix(cameraSystem.guiCamera.combined);
        batch.begin();
        batch.setColor(1f, 1f, 1f, 1f);

        Entity player = tagManager.getEntity("player");
        assetSystem.font.setColor(1f,1f,1f,1f);
        assetSystem.font.draw(batch, "resources: " + wm.get(player).resources, 50, 50);

        batch.end();
    }
}
