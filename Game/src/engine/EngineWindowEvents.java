package engine;

import org.joml.Vector2i;
import org.lwjgl.glfw.GLFWVidMode;
import static org.lwjgl.glfw.GLFW.*;

import engine.gui.EngineGUI;
import engine.scene.EngineScene;

/**
 * Holds all reference to Window callbacks and notify every Engine component of incoming user events
 * @author louis
 *
 */
class EngineWindowEvents {

	/** GUI reference */
	private EngineGUI engineGUI;
	/** 3D scene Reference */
	private EngineScene engineScene;
	/** boolean fullscreen */
	private boolean fullscreen;
	
	/**
	 * Engine Event Constructor only visible by the Engine package classes
	 */
	public EngineWindowEvents() {
		this.engineGUI = null;
		this.engineScene = null;
		this.fullscreen = false;
	}

	/**
	 * Constructor of the Engine Event
	 * @param engineScene the scene to dipatch events
	 * @param engineGUI the gui to dipatch events
	 */
	public EngineWindowEvents(EngineScene engineScene, EngineGUI engineGUI, Vector2i engineSize) {
		this.engineGUI = engineGUI;
		this.engineScene = engineScene;
		this.fullscreen = false;
	}
	
	/**
	 * Dispatch the Key events to all listeners of the GUI and Scene
	 * @param window The pointer to the window
	 * @param key The key that raised an event
	 * @param scancode of the key
	 * @param action Action Pressed Repeated released
	 * @param mods of the key
	 */
	public void dispatchKey(long window, int key, int scancode, int action, int mods) {
		if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
			glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
		}else if(key == GLFW_KEY_F1 && action == GLFW_RELEASE) {
			if(fullscreen) {
				fullscreen = false;
				long monitor = glfwGetPrimaryMonitor();
				GLFWVidMode vidMode = glfwGetVideoMode(monitor);
				glfwSetWindowMonitor(window, 0 , 0, 0, 1600, 900, GLFW_DONT_CARE);
				glfwSetWindowPos(window, vidMode.width()/2-800, vidMode.height()/2-450);
				glfwSwapInterval(1);
			}else {
				fullscreen = true;
				long monitor = glfwGetPrimaryMonitor();
				GLFWVidMode vidMode = glfwGetVideoMode(monitor);
				glfwSetWindowMonitor(window, monitor, 0, 0, vidMode.width(), vidMode.height(), vidMode.refreshRate());
				glfwSwapInterval(1);
			}
			
		}else {
			if(this.engineScene != null) {
				this.engineScene.handleKeyEvent(window, key, scancode, action, mods);
			}
			if(this.engineGUI != null) {
				this.engineGUI.handleKeyEvent(window, key, scancode, action, mods);
			}
		}
	}
	
	
	/**
	 * Dispatch the Resize event to all listeners of the GUI and Scene
	 * @param window The pointer to the Engine Window
	 * @param width The new width of the window
	 * @param height The new height of the Window
	 */
	public void dispatchSize(long window, int width, int height) {
		if(this.engineScene != null) {
			this.engineScene.handleSizeEvent(window, width, height);
		}
		if(this.engineGUI != null) {
			this.engineGUI.handleSizeEvent(window, width, height);
		}
		
	}
	
	/**
	 * Dispatch the window Mouse position Event to the scene and the GUI
	 * @param window The Engine window
	 * @param x The mouse horizontal position
	 * @param y The mouse vertical position
	 */
	public void dispatchCursorPos(long window, float x, float y) {
		if(this.engineScene != null) {
			this.engineScene.handleMousePositionEvent(window, x, y);
		}
		if(this.engineGUI != null) {
			this.engineGUI.handleMousePositionEvent(window, x, y);
		}
	}
}
