package net.mostlyoriginal.ns2d.system.passive;

import com.artemis.utils.Bag;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import net.mostlyoriginal.ns2d.api.PassiveSystem;

/**
 * @author Daan van Yperen
 */
public class FramebufferManager extends PassiveSystem {

	Bag<FrameBuffer> frameBuffers = new Bag<>();

	/** Fetch a canvas size framebuffer. */
	public FrameBuffer getFrameBuffer( int index )
	{
		FrameBuffer result = frameBuffers.get(index);
		if ( result == null )
		{
			result = new FrameBuffer(Pixmap.Format.RGBA8888, (int)(Gdx.graphics.getWidth() * CameraSystem.ZOOM), (int)(Gdx.graphics.getHeight() *  CameraSystem.ZOOM), false);
			frameBuffers.set(index, result);
		}
		return result;
	}
}
