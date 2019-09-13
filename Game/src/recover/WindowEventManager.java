package recover;

import static org.lwjgl.glfw.GLFW.GLFW_DONT_CARE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_F1;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwSetWindowMonitor;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;

import org.joml.Vector2i;
import org.lwjgl.glfw.GLFWVidMode;

import engine.gui.UIManager;
import engine.scene.EngineScene;

/**
 * This class handles all user input for the recover engine
 * @author louis
 *
 */
public class WindowEventManager{

	/** Reference to the recover scene */
	private EngineScene scene;
	/** Reference to the recover gui */
	private UIManager gui;
	/** Vector2i containing screen size */
	private Vector2i size;
	
	/** boolean fullscreen */
	private boolean fullscreen;
	
	/**
	 * Creates an Event handler for the Recover Game
	 * @param scene recover scene
	 * @param gui recover gui
	 * @param size recover screen size
	 */
	public WindowEventManager(EngineScene scene, UIManager gui, Vector2i size) {
		this.scene = scene;
		this.gui = gui;
		this.size = size;
	}

	public void handleSizeEvent(long window, int width, int height) {
		gui.setShadersProjections();
		gui.resize("Main Menu Navigation");
		scene.camera().updateProjection();
	}

	public void handleMousePositionEvent(long window, float x, float y) {
		scene.camera().processMouse(x, y);
	}

	public void handleKeyEvent(long window, int key, int scancode, int action, int mods) {
		if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
			glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
		}else if(key == GLFW_KEY_F1 && action == GLFW_RELEASE) {
			if(fullscreen()) {
				fullscreen(false);
				long monitor = glfwGetPrimaryMonitor();
				GLFWVidMode vidMode = glfwGetVideoMode(monitor);
				glfwSetWindowMonitor(window, 0 , 0, 0, 1600, 900, GLFW_DONT_CARE);
				glfwSetWindowPos(window, vidMode.width()/2-800, vidMode.height()/2-450);
				glfwSwapInterval(1);
			}else {
				fullscreen(true);
				long monitor = glfwGetPrimaryMonitor();
				GLFWVidMode vidMode = glfwGetVideoMode(monitor);
				glfwSetWindowMonitor(window, monitor, 0, 0, vidMode.width(), vidMode.height(), vidMode.refreshRate());
				glfwSwapInterval(1);
			}
			
		}else {
			
		}
	}


	/**
	 * @return true if fullscreen is active
	 */
	protected boolean fullscreen() {
		return this.fullscreen;
	}
	
	/**
	 * Activate or disactivate fullscreen with this command
	 * @param activate boolean
	 */
	protected void fullscreen(boolean activate) {
		this.fullscreen = activate;
	}
}
