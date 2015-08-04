package net.mostlyoriginal.ns2d;

import com.artemis.World;

/**
 * @author Daan van Yperen
 */
public final class G {
    private G() {
    }

    public static final int CELL_SIZE = 32;
    public static final int DIFFUSE_FBO = 0;
    public static final int NORMAL_FBO = 1;

    public static MainScreen screen;
    public static World world;
    public static NsGame game;
    public static Settings settings = new Settings();
}
