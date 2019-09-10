package engine.gui;

import java.util.HashMap;

import org.joml.Vector2i;

import engine.ShaderManager;
import engine.gui.panel.ContentPanel;
import engine.gui.panel.DialogBox;
import engine.gui.panel.VerticalPanel;


public final class EngineGUI {

	private ContentPanel panel;
	private final Vector2i engineSize;
	private final HashMap<String, DialogBox> dialogs;
	private final ShaderManager shaders;
	
	public EngineGUI(Vector2i engineSize, ShaderManager shaders) {
		this.shaders = shaders;
		this.engineSize = engineSize;
		this.panel = new VerticalPanel();
		this.dialogs = new HashMap<String, DialogBox>();
	}
	
	/**
	 * Handle the Key events to all listeners of the GUI
	 * @param window The pointer to the window
	 * @param key The key that raised an event
	 * @param scancode of the key
	 * @param action Action Pressed Repeated released
	 * @param mods of the key
	 */
	public void handleKeyEvent(long window, int key, int scancode, int action, int mods) {
		
	}
	
	/**
	 * Handle the Mouse position event and dispatch the message to the GUI Windows
	 * @param window Pointer to the window of the engine
	 * @param x the new cursor horizontal position
	 * @param y the new cursor vertical position
	 */
	public void handleMousePositionEvent(long window, float x, float y) {
		
	}

	/**
	 * Handle the Resizing event and dispatch it to all resizing listener of the scene
	 * @param window The pointer to the window
	 * @param width The new Horizontal size of the window
	 * @param height The new Vertical size of the window
	 */
	public void handleSizeEvent(long window, int width, int height) {
		
	}
	
	/** Try to add a window to the map
	 * return false if a window with this name already exists
	 * @param window to add to the map
	 * @return true if the operation succeed
	 */
	public boolean addWindow(DialogBox dialog) {
		if(!dialogs.containsKey(dialog.name())) {
			this.dialogs.put(dialog.name(), dialog);
			return true;
		}
		return false;
	}
	
	public void build() {
		panel.build(0, 0, engineSize.x, engineSize.y);
	}
	
	/**
	 * Set the panel of the engine GUI
	 * Panel can be a Horizontal Panel or a Vertical Panel
	 * @param panel
	 */
	public void setContentPane(ContentPanel panel) {
		this.panel = panel;
	}
	
	/**
	 * Get the content panel of the engine GUI to add or remove component
	 * @return
	 */
	public ContentPanel panel() {
		return panel;
	}

	public void render() {
	}
	
	public ShaderManager shaders() {
		return shaders;
	}
	
}
