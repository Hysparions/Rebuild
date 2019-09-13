package engine.gui;

import java.util.HashMap;

import org.joml.Vector2i;

import engine.ShaderManager;
import engine.gui.core.UIWrapper;
import engine.opengl.Shader;
import engine.utils.UnexistingShaderException;

/**
 * This class controls all the UI Wrappers used by the engine
 * @author louis
 *
 */
public final class UIManager {

	/** Size of the Engine */
	private final Vector2i engineSize;
	/** Shader Manager containing UI Shaders */
	private final ShaderManager shaders;
	/** Map containing all the UI Wrappers registered to rendering */
	private final HashMap<String, UIWrapper> wrappers;
	
	public UIManager(Vector2i engineSize) {
		
		// Get the engine size
		this.engineSize = engineSize;
		
		//Create Shader manager
		this.shaders = new ShaderManager();
		
		// Create Wrappers HashMap
		this.wrappers = new HashMap<String, UIWrapper>();
		
	}
	
	/**
	 * Register this UI Wrapper so it is now able to be rendered to the screen
	 * using the render function of the UIManager
	 * @param wrapper to register
	 * @return true if the operation succeed, false instead 
	 */
	public boolean register(UIWrapper wrapper) {
		if(wrapper != null) {
			if(!wrappers.containsKey(wrapper.name())){
				this.wrappers.put(wrapper.name(), wrapper);
				return true;
			}
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
	public UIWrapper unregister(String name, boolean destroy) {
		UIWrapper wrapper = null;
		wrapper = this.wrappers.get(name);
		if(destroy) {
			wrapper.destroy();
			return null;
		}
		return wrapper;
	}
	
	/**
	 * This function calls the resize function of the Wrapper
	 * associated to the name given in parameters
	 * @param name of the wrapper
	 * @return true if the operation succeed
	 */
	public boolean resize(String name) {
		UIWrapper wrapper = this.get(name);
		if(wrapper != null) {
			wrapper.resizeAndPosition(engineSize.x, engineSize.y);
			return true;
		}
		return false;
	}
	
	/**
	 * Getter for the UI Shader Manager
	 * @return the UI Shader manager
	 */
	public ShaderManager shaders() {
		return shaders;
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
		}catch(UnexistingShaderException e) {
			e.printStackTrace();
		}
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
		} catch (UnexistingShaderException e) {
			e.printStackTrace();
		}
	}
	
	/** 
	 * Get the wrapper associated to the given name if it exists 
	 * @param name of the Wrapper
	 * @return the wrapper associated to this name
	 */
	public UIWrapper get(String name) {
		return this.wrappers.get(name);
	}
}
