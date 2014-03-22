package net.mostlyoriginal.ns2d.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import net.mostlyoriginal.ns2d.NsGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

        /* Valid youtube resolutions.

        2160p: 3840x2160
        1440p: 2560x1440
        1080p: 1920x1080
        720p: 1280x720
        480p: 854x480
        360p: 640x360
        240p: 426x240 */

        config.title  = "NS2D";
        config.resizable = false;
        config.width  = 1280;
        config.height = 720;
		new LwjglApplication(new NsGame(), config);
	}
}

