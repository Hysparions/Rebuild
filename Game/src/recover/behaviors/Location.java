package recover.behaviors;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import engine.behaviors.BehaviorType;
import engine.behaviors.EngineBehavior;

public class Location extends EngineBehavior{

	// worldPosition
	private Vector3f position;
	// worldRotation
	private float rotation;
	private Vector3f rotationAxe;

	public Matrix4f matrix;

	public Location(Vector3f position, float rotation) {
		super(BehaviorType.LOCATION);
		this.position = position;
		this.rotation = rotation;
		this.rotationAxe = new Vector3f(0.0f, 1.0f, 0.0f);
		this.matrix = new Matrix4f();
		updateMatrix();
	}

	/**
	 * Update the location matrix 
	 */
	public void updateMatrix() {
		if (isActive) {
			matrix.identity();
			matrix.translate(position);
			matrix.rotate(rotation, rotationAxe);
			this.deactivate();
		}
	}

	/**
	 * Getter for the position
	 * @return the position vector of the location
	 */
	public Vector3f position() {
		return position;
	}

	@Override
	public boolean generateSceneEvent() {
		// Don't want event for this
		return false;
	}

	@Override
	public String toString() {
		return "Location : " + Float.toString(this.position.x) + " " + Float.toString(this.position.y) + " " + Float.toString(this.position.z);
	}

}
