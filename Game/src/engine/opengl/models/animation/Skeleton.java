package engine.opengl.models.animation;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Skeleton {

	/** Name of the skeleton */
	private String name;
	/** First bone of the skeleton */
	private Bone head;
	/** Matrix4f transform */
	private Matrix4f matrix;
	
	/**
	 * Constructor of the Skeleton
	 */
	public Skeleton(String name, Bone head, Matrix4f matrix) {
		this.name = name;
		this.head = head;
		this.matrix = matrix;
		this.matrix.mul(new Matrix4f().rotate((float) Math.toRadians(-90), new Vector3f(1, 0, 0)));
	}
	
	/**
	 * Get the first bone of the skeleton hierarchy
	 * @return heading bone
	 */
	public Bone head() {
		return this.head;
	}
	
	/**
	 * Get the name of the Skeleton
	 * @return Skeleton name
	 */
	public String name() {
		return this.name;
	}
	
	/**
	 * Get the matrix transform of the skeleton
	 * @return Skeleton name
	 */
	public Matrix4f matrix() {
		return matrix;
	}
	
	/**
	 * Display the Skeleton data
	 */
	public void display() {
		System.out.println("Skeleton " + name + " :");
		System.out.println(this.matrix);
		head.display();
	}
}
