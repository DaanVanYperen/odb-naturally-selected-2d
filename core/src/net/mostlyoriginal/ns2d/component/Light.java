package net.mostlyoriginal.ns2d.component;

import com.artemis.Component;
import com.badlogic.gdx.graphics.Color;

/**
 * @author Daan van Yperen
 */
public class Light extends Component {

	public Light() {
	}

	public Light( Color color, float z, float radius, float intensity ) {
		this.z = z;
		this.radius = radius;
		this.intensity = intensity;
		this.color.set(color);
	}

	public Color color = new Color();
	public float intensity = 1;
	public float radius = 10;
	public float z = 60;
}
