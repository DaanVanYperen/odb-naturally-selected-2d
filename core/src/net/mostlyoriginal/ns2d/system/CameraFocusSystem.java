package net.mostlyoriginal.ns2d.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import net.mostlyoriginal.ns2d.component.CameraFocus;
import net.mostlyoriginal.ns2d.component.Pos;

/**
 * @author Daan van Yperen
 */
@Wire
public class CameraFocusSystem extends EntityProcessingSystem {

    private ComponentMapper<Pos> pm;
    private CameraSystem cameraSystem;

    public CameraFocusSystem() {
        super(Aspect.getAspectForAll(Pos.class, CameraFocus.class));
    }

    @Override
    protected void process(Entity e) {
        final Pos pos = pm.get(e);
        cameraSystem.camera.position.x = pos.x;
        cameraSystem.camera.position.y = pos.y;
        cameraSystem.camera.update();
    }
}
