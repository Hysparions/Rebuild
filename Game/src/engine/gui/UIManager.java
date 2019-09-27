package engine.gui;

import java.util.LinkedList;

import org.joml.Vector2i;
import static org.lwjgl.opengl.GL30.*;

import engine.ShaderManager;
import engine.opengl.Shader;
import engine.utils.ShaderException;

/**
 * This class controls all the UI Windows used by the engine
 * The UIManager handles window by order, which is important for
 * rendering and UI listeners, has the only lstener that will be 
 * current is the one of the closest window
 * @author louis
 *
 */
public final class UIManager {

	/** Size of the Engine */
	private final Vector2i engineSize;
	/** Shader Manager containing UI Shaders */
	private final ShaderManager shaders;
	/** Linked List containing all the UIWindow registered to rendering */
	private final LinkedList<UIWindow> windows;
	
	public UIManager(Vector2i engineSize) {
		
		// Get the engine size
		this.engineSize = engineSize;
		
		//Create Shader manager
		this.shaders = new ShaderManager();
		
		// Create Wrappers HashMap
		this.windows = new LinkedList<UIWindow>();
		
	}
	
	/**
	 * This function proceed successively all this operations :
	 * - Running animations that may change Windows position or size
	 * as well as updating the size hints of one or more component
	 * - For each visible windows, Do :
	 * - Build size and position of all the components that have been updated
	 * - Process Hover input
	 * - Find the last Hovered component with a Hovered Listener
	 * - If the Hover listener has animation => proceed animation
	 * - Process scroll input
	 * - If the Hovered panel is a scroll one => scroll
	 * - Process cursor click
	 * - If the Hovered component has a selectable Listener => Make it selected
	 * - Process Keyboard Input
	 * - If the selected component contains a Text listener => Give it keyboard input
	 * - End of the function
	 */
	public final void process() {
		// Build all window
		for(UIWindow window : windows) {
			// Line needed to compute overlay of the content panel
			window.panel.build(window.x(), window.y(), window.width(), window.height());
			// Proceed every children
			buildPanel(window.panel);
		}
	}
	
	/**
	 * Resize and rebuild the entire UI
	 * @param size of the screen
	 */
	public final void resize(Vector2i size) {
		// Build all window
		for(UIWindow window : windows) {
			// Update window pos
			window.updateBox(size);
			// Line needed to compute overlay of the content panel
			window.panel.build(window.x(), window.y(), window.width(), window.height());
			// Proceed every children
			buildPanel(window.panel);
		}
	}
	
	/**
	 * Renders the window list by order
	 * the window order is important as windows rendered first
	 * may be overlapped by windows rendered after
	 */
	public final void render() {
		
		glDisable(GL_DEPTH_TEST);
		for(UIWindow window : this.windows) {
			if(window.isVisible()) {
				window.render(shaders);
			}
		}
		glEnable(GL_DEPTH_TEST);
	}
	
	/**
	 * Register this UI Wrapper so it is now able to be rendered to the screen
	 * using the render function of the UIManager
	 * @param window to register
	 * @return true if the operation succeed, false instead 
	 */
	public final boolean register(UIWindow window) {
		if(window != null) {
			for(UIWindow elem : windows) {
				if(elem == window || elem.name().equals(window.name())) {
					return false;
				}
			}
			this.windows.add(window);
		}
		return false;
	}
	
	/**
	 * Register this UI Wrapper so it is now able to be rendered to the screen
	 * using the render function of the UIManager
	 * @param name of the wrapper to unregister
	 * @param destroy to true if the Wrapper should be destroyed
	 * @return the wrapper if destroy was false, null if the wrapper doesn't exist or was destroyed
	 */
	public final UIWindow unregister(String name, boolean destroy) {
		UIWindow wrapper = null;
		wrapper = this.get(name);
		if(destroy) {
			wrapper.destroy();
			return null;
		}
		return wrapper;
	}

	/**
	 * This function calls the resize function of the Wrapper
	 * associated to the name given in parameters
	 * @param name of the window
	 * @return true if the operation succeed, false if the dimensions are higher than max or lower than min values
	 */
	public final boolean resize(String name, float width, float height) {
		UIWindow window = this.get(name);
		if(window != null) {
			window.resize(width, height);
			return true;
		}
		return false;
	}
	
	/**
	 * This function calls the resize function of the Wrapper
	 * associated to the name given in parameters
	 * @param name of the window
	 * @return true if the operation succeed, false if the dimensions are higher than max or lower than min values
	 */
	public final boolean reposition(String name, float x, float y) {
		UIWindow window = this.get(name);
		if(window != null) {
			window.reposition(x, y);
			return true;
		}
		return false;
	}
	
	private final void buildPanel(UIPanel panel) {
		// Build children of the panel
		panel.buildChildren();
		
		// If the panel have child panel, repeat the function
		for(UIComponent component : panel.children) {
			if(component instanceof UIPanel) {
				buildPanel((UIPanel) component);
			}
		}
	}
	
	/**
	 * Getter for the UI Shader Manager
	 * @return the UI Shader manager
	 */
	public ShaderManager shaders() {
		return shaders;
	}

	/**
	 * Creates the gui shaders
	 */
	public void createShaders() {
		try {
			this.shaders.addFromSource("UIShape");
			this.shaders.addFromSource("UISprite");
			this.shaders.addFromSource("UIText");
			this.setShadersProjections();
		} catch (ShaderException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Recalculates the shaders projection
	 */
	public void setShadersProjections() {
		try {
			Shader shader = this.shaders.get("UIShape");
			if(shader != null) { 
				shader.setOrthoProjection(engineSize.x, engineSize.y);
			}
			shader = this.shaders.get("UISprite");
			if(shader != null) { 
				shader.setOrthoProjection(engineSize.x, engineSize.y);
			}
			shader = this.shaders.get("UIText");
			if(shader != null) { 
				shader.setOrthoProjection(engineSize.x, engineSize.y);
			}
		}catch(ShaderException e) {
			e.printStackTrace();
		}
	}
	
	/** 
	 * Get the Window associated to the given name if it exists 
	 * @param name of the Window
	 * @return the Window associated to this name
	 */
	public UIWindow get(String name) {
		for(UIWindow window : windows) {
			if(window.name().equals(name)) {
				return window;
			}
		}
		return null;
	}

	/**
	 * put the window associated to the specified name in front of all the others
	 * @param name of the window to push in front of the scene
	 */
	public void pushFront(String name) {
		UIWindow window = this.get(name);
		if(window !=null) {
			this.windows.remove(window);
			this.windows.addLast(window);
		}
	}
	
	/**
	 * Destroys all the windows and clear the UIWindow List
	 * Destroys the shaders used by the UI
	 */
	public void destroy() {
		for(UIWindow window : windows) {
			window.destroy();
		}
		windows.clear();
		shaders.destroy();
	}

	
	
	
}
