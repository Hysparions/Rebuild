package gui;

import org.joml.Vector2f;

public abstract class GUIComponent {
	
	//private static float MAX_SCALE = 2.0f;
	//private static float MIN_SCALE = 0.25f;
	
	protected GUIType type;
	/** Relative Position */
	protected Vector2f position;
	/** Relative Surface */
	protected Vector2f surface;
	/**Aspect*/
	protected float aspect;
	

	/**
	 * Construct the Interface Component specifying the type of the child class
	 * @param type
	 */
	public GUIComponent(GUIType type, Vector2f position, Vector2f surface) {
		this.type = type;
		this.position = position;
		this.surface = surface;

	}
	
	public abstract void reSize();
	
	/** Method Returning the interface type of the component */
	public GUIType type() { return this.type; }
	/** Method Returning the interface type of the component */
	public Vector2f position() { return this.position; }
	/** Method Returning the interface type of the component */
	public Vector2f surface() { return this.surface; }
	
	
	/** Method Returning the interface type of the component */
	public void setPosition(Vector2f position) { this.position = position; }
	/** Method Returning the interface type of the component */
	public void setSurface(Vector2f surface) { this.surface = surface; }
	
}
