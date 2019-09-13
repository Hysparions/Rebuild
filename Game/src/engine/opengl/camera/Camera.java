package engine.opengl.camera;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT_SHIFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.glfwGetKey;

import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;
/**
 * Camera class provide an event responsive view matrix to visualize the scene from a point
 * @author louis
 *
 */
public class Camera {

	// Default values for the camera
	private static final float YAW = 0.f;
	private static final float PITCH = 0.0f;
	private static final float SPEED = 40.0f;
	private static final float SENSITIVITY = 0.1f;
	private static final float ZOOM = 60.0f;

	// Camera vectors attributes
	private Vector3f position;
	private Vector3f front;
	private Vector3f up;
	private Vector3f right;
	private Vector3f worldUp;
	private Vector3f center;
	private Vector3f eye;
	private Vector2f buffer;

	// Camera View Matrix
	private Matrix4f view;
	
	/** Clip Space factor for near plane */
	protected final float near = 0.1f;
	/** Clip Space factor for far plane */
	protected final float far = 500.f;
	/** Engine Screen Size*/
	private final Vector2i engineSize;
	/** Clip Space Matrix */
	private Matrix4f projection;
	
	/** Matrix used as buffer to hold the result of the projection*view matrix operation */
	private Matrix4f projectionView;
	
	// Camera attributes
	private float yaw;
	private float pitch;
	private float speed;
	private float sensitivity;
	public float zoom;
	
	// Camera locker
	private boolean active;
	private boolean firstMouse;
	
	// Camera Mouse utils
	private float lastX;
	private float lastY;
	
	// Camera control
	private int forwardControl = GLFW_KEY_W;
	private int backwardControl = GLFW_KEY_S;
	private int leftControl = GLFW_KEY_A;
	private int rightControl = GLFW_KEY_D;
	private int topControl = GLFW_KEY_SPACE;
	private int bottomControl = GLFW_KEY_LEFT_SHIFT;

	/**
	 * Construct a new camera at the specified 3D position
	 * @param posX
	 * @param posY
	 * @param posZ
	 */
	public Camera(float posX, float posY, float posZ, Vector2i engineSize) {

		// Filling Vectors attributes
		position = new Vector3f(posX, posY, posZ);
		front = new Vector3f(1.0f, 0.0f, 0.0f);
		up = new Vector3f(0.0f, 1.0f, 0.0f);
		right = new Vector3f(0.0f, 0.0f, 1.0f);
		worldUp = new Vector3f(0.0f, 1.0f, 0.0f);
		center = new Vector3f(0.0f, 0.0f, 0.0f);
		eye = new Vector3f(0.0f);
		buffer = new Vector2f(0.0f, 0.0f);

		// Filling Attributes with Default values
		yaw = YAW;
		pitch = PITCH;
		speed = SPEED;
		sensitivity = SENSITIVITY;
		zoom = ZOOM;
		
		// Mouse and Keyboard utils
		active = true;
		firstMouse = true;
		lastX = 0.0f;
		lastY = 0.0f;

		// Creating a new View matrix
		this.view = new Matrix4f();
		// Creating a new Projection Matrix
		this.projection = new Matrix4f();
		// Creating a new Projection Matrix
		this.projectionView = new Matrix4f();
		// Set the vector of the engine size
		this.engineSize = engineSize;
		
		// Update the view Matrix
		updateView();
		// Update the projection Matrix
		updateProjection();
	}

	// Update camera vectors and View Matrix and the projectionView by the way
	public void updateView() {

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
		// Calculating the center of view position
		eye.add(front, center);

		// Calculate new View Matrix
		view.identity();
		view.lookAt(eye, center, up);
		this.projection.mul(this.view, this.projectionView);

	}
	
	/**
	 * This function update the camera Projection matrix and the projectionView by the way
	 */
	public void updateProjection() {
		this.projection.identity();
		this.projection.perspective((float) Math.toRadians(zoom), ((float) engineSize.x) / ((float) engineSize.y), near, far);
		this.projection.mul(this.view, this.projectionView);
	}

	public Matrix4f getView() {
		return view;
	}

	public Vector3f position() {
		return position;
	}

	public void invertPitch() {
		pitch = -pitch;
	}

	public void processMouse(float x, float y) {
		if(active) {
			float xoffset = x - lastX;
			float yoffset = lastY -  y; // reversed since y-coordinates go from bottom to top
			lastX = x;
			lastY = y;
			if(firstMouse) {
				xoffset = 0.0f;
				yoffset = 0.0f;
				firstMouse = false;
			}
			// Apply Mouse sensitivity factor
			xoffset *= sensitivity;
			yoffset *= sensitivity;

			yaw += xoffset;
			pitch += yoffset;

			// Constrain pitch
			if (pitch > 89.0f) {
				pitch = 89.0f;
			} else if (pitch < -89.0f) {
				pitch = -89.0f;
			}
		}
	}

	public void processInput(long window, float deltaTime) {
		// Process Camera movments in every direction
		if (glfwGetKey(window, forwardControl) == GLFW_PRESS) {
			this.processKeyBoard(Direction.FORWARD, (float) deltaTime);
		}
		if (glfwGetKey(window, backwardControl) == GLFW_PRESS) {
			this.processKeyBoard(Direction.BACKWARD, (float) deltaTime);
		}
		if (glfwGetKey(window, leftControl) == GLFW_PRESS) {
			this.processKeyBoard(Direction.LEFT, (float) deltaTime);
		}
		if (glfwGetKey(window, rightControl) == GLFW_PRESS) {
			this.processKeyBoard(Direction.RIGHT, (float) deltaTime);
		}
		if (glfwGetKey(window, bottomControl) == GLFW_PRESS) {
			this.processKeyBoard(Direction.DOWN, (float) deltaTime);
		}
		if (glfwGetKey(window, topControl) == GLFW_PRESS) {
			this.processKeyBoard(Direction.UP, (float) deltaTime);
		}
	}
	
	public void processKeyBoard(Direction direction, float deltaTime) {
		
		float velocity = speed * deltaTime;
		// Have to normalize X and Z data so when front vector is pointing up/down the
		// camera keep always the same speed
		
		if (direction == Direction.FORWARD) {
			buffer.x = front.x;
			buffer.y = front.z;
			buffer.normalize();
			position.add(buffer.x * velocity, 0.0f, buffer.y * velocity);
		}
		else if (direction == Direction.BACKWARD) {
			buffer.x = front.x;
			buffer.y = front.z;
			buffer.normalize();
			position.add(-(buffer.x * velocity), 0.0f, -(buffer.y * velocity));
		}
		else if (direction == Direction.LEFT) {
			buffer.x = right.x;
			buffer.y = right.z;
			buffer.normalize();
			position.add(-(buffer.x * velocity), 0.0f, -(buffer.y * velocity));
		}
		else if (direction == Direction.RIGHT) { 
			buffer.x = right.x;
			buffer.y = right.z;
			buffer.normalize();
			position.add(buffer.x * velocity, 0.0f, buffer.y * velocity);
		}
		else if (direction == Direction.UP) { 
			position.add(0.0f, worldUp.y * velocity, 0.0f);
		}
		else if (direction == Direction.DOWN) {
			position.add(0.0f, -(worldUp.y * velocity), 0.0f);
		}
	}

	/** @return the pitch of the camera */
	public float getPitch() { return pitch;}
	
	/** Disactivate the camera movement*/
	public void disactivate() { active = false;	}
	
	/** Activate the camera movement*/
	public void activate() { active = true;	}
	
	/** @return if the camera is active */
	public boolean isActive() { return active;	}
	
	/** @return near value  of the projection matrix*/
	public final float near() { return near; }
	
	/** @return Far value of the projection matrix*/
	public final float far() { return far; }

	/** @return The projection matrix */
	public Matrix4f projection() {return this.projection;}

	public Matrix4f projectionView() {
		return this.projectionView;
	}

	/** Print the camera position */
	public void print() {System.out.println("Position : " + position.x + " " + position.y + " " + position.z);}


}
