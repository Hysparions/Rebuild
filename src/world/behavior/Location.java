package world.behavior;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Location extends Behavior{
	
	//worldPosition
	private Vector3f position;
	//worldRotation
	private float rotation;
	private Vector3f rotationAxe;
	
	public Matrix4f matrix;
	
	public Location(Vector3f position, float rotation, boolean active) {
		super(BehaviorType.LOCATION, active);
		
		this.position = position;
		this.rotation = rotation;
		this.rotationAxe = new Vector3f(0.0f, 1.0f, 0.0f);
		this.matrix = new Matrix4f();
	}
	
	public void update() {
		if(isActive) {
			matrix.identity();
			matrix.translate(position);
			matrix.rotate(rotation, rotationAxe);
			this.Disactivate();
		}
	}
	
	public Vector3f getPosition() {return position;}
}
