package net.mostlyoriginal.ns2d.component;

import com.artemis.Component;
import com.badlogic.gdx.graphics.Color;

/**
 * @author Daan van Yperen
 */
public class Anim extends Component {

    public static final int ORIGIN_AUTO = -999999;
    public boolean flippedX; // flip animation, should not affect rotation.

    public static class Layer
    {
	    public static final int ON_WALL = 50;
	    public static final int BULLETS = 100;
	    public static final int ENEMIES = 200;
	    public static final int DIRECTLY_BEHIND_BEHIND_PLAYER = 300;
	    public static final int DIRECTLY_BEHIND_PLAYER = 400;
	    public static final int PLAYER_ARM = 500;
        public static final int PLAYER = 600;
	    public static final int DEFAULT_LAYER = 700;
    };

    public String id;

    public float speed = 1;
    public float age = 0;
    public float scale = 1;
    public float rotation = 0;
    public int ox = ORIGIN_AUTO; // rotational origin X
    public int oy = ORIGIN_AUTO; // rotational origin Y
    public final Color color = new Color(1,1,1,1);

    public Anim(String id) {
        this.id = id;
    }
    public Anim(String id, int ox, int oy ) {
        this.id = id;
        this.ox = ox;
        this.oy = oy;
    }

}
