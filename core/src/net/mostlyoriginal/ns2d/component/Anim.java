package net.mostlyoriginal.ns2d.component;

import com.artemis.Component;
import com.badlogic.gdx.graphics.Color;

/**
 * @author Daan van Yperen
 */
public class Anim extends Component {

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

    public float scale = 1;
    public float rotation = 0;
    public final Color color = new Color(1,1,1,1);

    public Anim(String id) {
        this.id = id;
    }
    public Anim(String id, Layer layer ) {
        this.id = id;
        this.layer = layer;
    }

}
