 package engine.gui.panel;

import org.joml.Vector2f;

import engine.ShaderManager;
import engine.gui.GUIComponent;
import engine.gui.shape.GLShape;
import engine.utils.UnexistingShaderException;

public class DialogBox{	

	/** Name of the window used as identifier */
	private String name;
	/** Position of the Window */
	private Vector2f position;
	/** Size of the Window */
	private Vector2f size;
	
	/** Dialog content pane */
	private ContentPanel panel;
	
	// TO ADD LISTENERS
	
	/**
	 * Constructor of the GUI Dialog class specifying the following sizing behaviors
	 * By default content pane for a dialog is a vertical panel
	 * By default with this constructor, the dialog doesn't adapt its size to its children optimal size
	 * @param name of the dialog box
	 * @param x	The position of the Dialog on X-Axis
	 * @param y	The position of the Dialog on Y-Axis
	 * @param w	The width of the Window
	 * @param h	The height of the Window
	 */
	public DialogBox(String name, int x, int y, int w, int h) {
		this.name = name;
		this.position = new Vector2f(x, y);
		this.size = new Vector2f(w, h);
		this.panel = new VerticalPanel();
	}
	
	/**
	 * Constructor of the GUI Dialog class specifying the following sizing behaviors
	 * By default content pane for a dialog is a vertical panel
	 * By default with this constructor, the dialog doesn't adapt its size to its children optimal size
	 * @param name of the dialog box
	 * @param x	The position of the Dialog on X-Axis
	 * @param y	The position of the Dialog on Y-Axis
	 */
	public DialogBox(String name, int x, int y) {
		this.name = name;
		this.position = new Vector2f(x, y);
		this.panel = new VerticalPanel();
		this.size = this.panel.optimal();
	}
	
	/**
	 * Build the position and size of each components and report every batch in the window handler
	 */
	public void build() {
		if(panel == null) {
			this.panel = new VerticalPanel();
		}
		panel.build(position.x(), position.y(), size.x(), size.y());
	}
	
	/**
	 * Draws the Window but check if it is built before and modify it if necessary
	 */
	public void render(ShaderManager shaders) {
		if(panel == null) {
			this.panel = new VerticalPanel();
		}
		if(!panel.isBuilt()) {
			this.build();
		}	
		try {
			shaders.get("Shape").use();
			renderShapes(panel);
		} catch (UnexistingShaderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void renderShapes(GUIComponent comp) {
		if(comp instanceof ContentPanel || comp instanceof GLShape) {
			comp.render();
		}
	}
	
	/**
	 * @return the name of the Window
	 */
	public String name() {
		return this.name;
	}
	
	/**
	 * Get the panel of the dialog box
	 * @return
	 */
	public ContentPanel panel() {
		if(panel == null) {
			this.panel = new VerticalPanel();
		}
		return this.panel;
	}
	
	/**
	 * Set the size of the window to a fixed value that doesn't fit children
	 * @param width of the window
	 * @param height of the window
	 */
	public void fixSize(int width, int height) {
		this.size = new Vector2f();
	}
	
	/**
	 * Set the size of the window to the optimal children size
	 */
	public void fitChildren() {
		if(panel == null) {
			this.panel = new VerticalPanel();
		}
		this.size = this.panel.optimal();
	}
	
	/**
	 * Panel to set as the main panel
	 * @param panel to set
	 */
	public void setContentPanel(ContentPanel panel) {
		this.panel = panel;
	}	
}
