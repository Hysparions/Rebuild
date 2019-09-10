package engine.opengl.models.stanford;

import org.joml.Vector3f;

import engine.utils.Color;

public class PolyVertex {

	public static final int SIZE = 10;

	public Vector3f position;
	public Vector3f normal;
	public Color color;

	// Static Vertex constructor
	public PolyVertex() {
		position = new Vector3f();
		normal = new Vector3f();
		color = new Color();
	}

	// Static Vertex overloaded constructor
	public PolyVertex(Vector3f position, Vector3f normal, Color color) {
		this.position = position;
		this.normal = normal;
		this.color = color;
	}

	public void display() {
		System.out.println(
				"Position" + position.x + " " + position.y + " " + position.z + " | Normal " + normal.x + " " + normal.y
						+ " " + normal.z + color.toString());
	}

}
