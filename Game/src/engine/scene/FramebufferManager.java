package engine.scene;

import java.util.HashMap;

import engine.opengl.framebuffers.EngineFramebuffer;

/**
 * This class is the main container for all framebuffer objects
 * @author louis
 *
 */
public class FramebufferManager {

	/**
	 * HashMap containing the frame buffers of the engine
	 */
	private HashMap<String, EngineFramebuffer> framebufferMap;
	
	/**
	 * Creates the framebuffer manager
	 */
	public FramebufferManager() {
		this.framebufferMap = new HashMap<>();
	}
	
	/**
	 * Add a new framebuffer to the frame buffer manager
	 * It can be accessed by the name of the framebuffer
	 * @param framebuffer name
	 */
	public void add(EngineFramebuffer framebuffer) {
		this.framebufferMap.put(framebuffer.name(), framebuffer);
	}
	
	/**
	 * Try to find a frame buffer in the manager using its name
	 * @param framebuffer name
	 * @return true if the manager holds a frame buffer associated to the name given
	 */
	public boolean contains(String name) {
		return this.framebufferMap.containsKey(name);
	}
	
	/**
	 * Get a frame buffer by its name if it exists
	 * @param name of the frame buffer
	 * @return the frame buffer or null if it doesn't exists
	 */
	public EngineFramebuffer get(String name) {
		return this.framebufferMap.get(name);
	}
	
	/**
	 * Remove the frame buffer of the manager using its name if it exists
	 * @param name of the frame buffer
	 * @return the frame buffer removed
	 */
	public EngineFramebuffer remove(String name) {
		return this.framebufferMap.remove(name);
	}
	
	/**
	 * Destroy all OpenGL stuff in the frame buffer and clears the map just after it
	 */
	public void destroy() {
		framebufferMap.forEach((name, framebuffer) -> {
			framebuffer.destroy();
		});
		framebufferMap.clear();
	}
}
