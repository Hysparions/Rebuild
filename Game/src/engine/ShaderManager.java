package engine;

import java.util.HashMap;
import java.util.Map;

import engine.opengl.Shader;
import engine.utils.ShaderException;


/**
 * This class holds all the shaders programs used by the Engine, the gui and the scene
 * @author louis
 *
 */
public final class ShaderManager {

	/** Map containing the shader */
	private Map<String, Shader> shadersMap;
	
	/** 
	 * Constructor of the Engine shader class creating a new hashmap of shaders
	 */
	public ShaderManager() {
		shadersMap = new  HashMap<String, Shader>();
		
	}
	
	/**
	 * This method add a new custom shader to the map 
	 * @param name the name/key of the shader to get it later
	 * @param shader the shader object
	 * @return True if the operation succeeds, false otherwise
	 */
	public boolean add(String name, Shader shader) {
		if(name != null && shader != null) {
			if(shader.isBuilt()) {
				shadersMap.put(name, shader);
				return true;
			}
		}		
		return false;
	}
	
	/**
	  * This method add a new custom shader to the map 
	 * @param name the name/key of the shader to get it later
	 * @param shader the shader object
	 * @throws UnexistingShaderException  if the shader doesn't exist in shader resource folder
	 */
	public boolean addFromSource(String name) throws ShaderException{
		if(name != null) {
			Shader shader = new Shader();
			if (shader.create(name) == false) {
				throw new ShaderException();
			}else {
				shadersMap.put(name, shader);
				return true;
			}
		}		
		return false;
	}

	/**
	 * This method remove a shader from the map 
	 * @param name the name/key of the shader
	 * @return True if the operation succeeds, false otherwise
	 */
	public boolean remove(String name) {
		if(name != null) {
			if(shadersMap.containsKey(name)) {
				
				shadersMap.remove(name);
				return true;
			}
		}		
		return false;
	}
	
	/**
	 * Get a Shader associated to a name. If the shader isn't already in the map
	 * the functions try to build it from source and throws an exception in case of failure
	 * @param name The name of the shader to get from the map
	 * @return the shader associated to the name, build it from source if necessary
	 * @throws UnexistingShaderException if the shader doesn't exist
	 */
	public Shader get(String name) throws ShaderException{
		
		if(shadersMap.containsKey(name)){
			return shadersMap.get(name);
		}else {
			Shader shader = new Shader();
			if (shader.create(name) == false) {
				throw new ShaderException();
			}else {
				shadersMap.put(name, shader);
				return shader;
			}
		}
	}	
	
	/**
	 * Method used to know if a shader is already existing
	 * @param shader
	 * @return
	 */
	public boolean contains(String shader) {
		return shadersMap.containsKey(shader);
	}

	/**
	 * Destroy every Shaders in the Map and clear it
	 */
	public void destroy() {
		shadersMap.forEach((name, shader) -> {
			shader.destroy();
		});
		shadersMap.clear();
	}
}


