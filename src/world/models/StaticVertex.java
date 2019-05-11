package world.models;

import org.joml.Vector3f;

public class StaticVertex {
	
	public static final int SIZE = 10;
	
	public Vector3f position;
	public Vector3f normal;
	public Vector3f color;
	public float flexibility;
	
	//Static Vertex constructor
	public StaticVertex () {
		position = new Vector3f();
		normal = new Vector3f();
		color = new Vector3f();
		flexibility = 1.0f;
	}
	
	//Static Vertex overloaded constructor
	public StaticVertex (Vector3f Position, Vector3f Normal, Vector3f Color, float Flexibility) {
		this.position = Position;
		this.normal = Normal;
		this.color = Color;
		this.flexibility = Flexibility;
	}
	
	public void display() {
		System.out.println("Position" + position.x + " " + position.y + " " + position.z + " | Normal " + normal.x + " " + normal.y + " " + normal.z + " | Color " + color.x + " " + color.y + " " + color.z + " "+ flexibility);
	}
	
}
