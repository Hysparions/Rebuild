package engine;

import static org.lwjgl.glfw.GLFW.*;

/** 
 * This class holds the Global time and delta time between Frames
 * You can use the time properties to build animations
 * @author louis
 *
 */
public final class EngineTime {
	
	/** time since the window creation */
	private double time;
	/** time between the two last frames */
	private float deltaTime;
	
	/**
	 * Contsructor of the EngineTime class setting time to zero and default delta to 0.05f
	 */
	public EngineTime() {
		time = 0.0f;
		deltaTime = 0.05f;
	}
	
	/**
	 * This function is called by the engine in the render loop and update currentTime
	 * as well as the delta Time between last frame and this frame
	 * Visibility is package
	 */
	final void update() {
		deltaTime = (float)(glfwGetTime()-time);
		time += deltaTime;
	}
	
	/**
	 * Get the time since the Engine creation
	 * @return time since Engine creation
	 */
	public final double getTime() {
		return time;
	}
	
	/**
	 * Get the time Between the two last frames
	 * @return time Between the two last frames
	 */
	public final float getDeltaTime() {
		return deltaTime;
	}
}
