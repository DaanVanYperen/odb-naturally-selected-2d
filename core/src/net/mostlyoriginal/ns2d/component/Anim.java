package net.mostlyoriginal.ns2d.component;

import com.artemis.Component;
import com.badlogic.gdx.graphics.Color;

/**
 * @author Daan van Yperen
 */
public class Anim extends Component {

    public static final int ORIGIN_AUTO = -999999;

    public static enum Layer
    {
        ON_WALL,
        BULLETS,
        ENEMIES,
        DIRECTLY_BEHIND_PLAYER,
        PLAYER_ARM,
        PLAYER,
        DEFAULT_LAYER
    };

    public String id;
    public Layer layer = Layer.DEFAULT_LAYER;

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
    public Anim(String id, Layer layer ) {
        this.id = id;
        this.layer = layer;
    }
    public Anim(String id, Layer layer, int ox, int oy ) {
        this.id = id;
        this.layer = layer;
        this.ox = ox;
        this.oy = oy;
    }

}
