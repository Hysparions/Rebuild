package world.water;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class WaterVertex {
	
	public Vector3f position;
	public Vector2f point1;
	public Vector2f point2;
	
	public WaterVertex() {
		position = new Vector3f();
		point1 = new Vector2f();
		point2 = new Vector2f();
		
	}
	
	public WaterVertex(Vector2f position, Vector2f point1, Vector2f point2, float height){
		this.position = new Vector3f(position.x, height, position.y);
		this.point1 = new Vector2f(point1.x, point1.y);
		this.point2 = new Vector2f(point2.x, point2.y);
	}
	
	
}
