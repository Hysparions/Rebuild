package openGL.camera;

import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Camera {
	
	//Default values for the camera
	private static final float YAW = -90.f;
	private static final float PITCH = 0.0f;
	private static final float SPEED = 40.0f;
	private static final float SENSITIVITY = 0.1f;
	private static final float ZOOM = 60.0f;
	
	//Camera vectors attributes
	private Vector3f position;
	private Vector3f front;
	private Vector3f up;
	private Vector3f right;
	private Vector3f worldUp;
	private Vector3f center;
	private Vector2f buffer;
	
	//Camera View Matrix
	private Matrix4f view;
	
	//Camera attributes
	private float yaw;
	private float pitch;
	private float speed;
	private float sensitivity;
	public float zoom;
	
	//Constructor
	public Camera(float posX, float posY, float posZ) {
		
		//Filling Vectors attributes
		position = new Vector3f(posX, posY, posZ);
		front = new Vector3f(0.0f, 0.0f, -1.0f);
		up = new Vector3f(0.0f, 1.0f, 0.0f);
		right = new Vector3f(1.0f, 0.0f, 0.0f);
		worldUp = new Vector3f(0.0f, 1.0f, 0.0f);
		center = new Vector3f(0.0f, 0.0f, 0.0f);
		buffer = new Vector2f(0.0f, 0.0f);
		
		//Filling Attributes with Default values
		yaw = YAW;
		pitch = PITCH;
		speed = SPEED;
		sensitivity = SENSITIVITY;
		zoom = ZOOM;
		
		//Creating a new matrix
		view = new Matrix4f();
		
		update();
	}
	
	//Update camera vectors and View Matrix
	public void update() {
		
		// Calculate a new Front vector
		front.x = (float) (Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)));
		front.y = (float) Math.sin(Math.toRadians(pitch));
		front.z = (float) (Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)));
		front.normalize();
		// Calculate The Right vector
		front.cross(worldUp, right);
		right.normalize();
		// Calculate The Up vector
		right.cross(front, up);
		up.normalize();
		//Calculating the center of view position
		position.add(front, center);

	}
	
	public Matrix4f getView() {
		//Calculate new View Matrix
		view.identity();
		view.lookAt(position,center, up);
		return view;
	}
	
	public Vector3f getPosition() {
		return position;
	}
	
	public void invertPitch() {
		pitch = -pitch;
	}
	
	public void processMouse(float x, float y) {
		
		//Apply Mouse sensitivity factor
		x*=sensitivity;
		y*=sensitivity;
		
		yaw += x;
		pitch += y;
		
		//Constrain pitch
		if(pitch > 89.0f) {
			pitch = 89.0f;
		}else if(pitch < -89.0f) {
			pitch = -89.0f;
		}	

	}
	
	public void processKeyBoard(Direction direction, float deltaTime) {
		float velocity = speed * deltaTime;
		//Have to normalize X and Z data so when front vector is pointing up/down the camera keep always the same speed
		buffer.x = front.x;
		buffer.y = front.z;
		buffer.normalize();
		if (direction == Direction.FORWARD)
			position.add(buffer.x * velocity, 0.0f, buffer.y * velocity);
		if (direction == Direction.BACKWARD)
			position.add(-(buffer.x * velocity), 0.0f, -(buffer.y * velocity));
		buffer.x = right.x;
		buffer.y = right.z;
		buffer.normalize();
		if (direction == Direction.LEFT)
			position.add(-(buffer.x * velocity), 0.0f, -(buffer.y * velocity));
		if (direction == Direction.RIGHT)
			position.add(buffer.x * velocity, 0.0f, buffer.y * velocity);
		if (direction == Direction.UP)
			position.add(0.0f, worldUp.y * velocity, 0.0f);
		if (direction == Direction.DOWN)
			position.add(0.0f, -(worldUp.y * velocity), 0.0f);
	}

	public float getPitch() {
		
		return pitch;
	}
	
	
}
