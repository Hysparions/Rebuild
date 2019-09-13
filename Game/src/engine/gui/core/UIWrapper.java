package engine.gui.core;

import org.joml.Vector2f;

/**
 * This class is the head of the UI Hierarchy
 * it provides function for building the window and render it to the screen
 * @author louis
 *
 */
public class UIWrapper {
	
	/** Name and identifier of the Window Wrapper */
	private String name;

	/** Head component */
	private UIComponent head;
	
	/** Position offset of the Wrapper window */
	private final Vector2f position;
	/** Size of the Wrapper window */
	private final Vector2f size;
	
	
	/**
	 * Constructor for the UIWrapper Object.
	 * @param name of the Wrapper window
	 */
	public UIWrapper(String name) {
		if(name != null) {
			this.name = name;
		}else {
			this.name = "A window with no name";
		}
		this.head = new UIComponent();
		this.position =  new Vector2f(0.0f);
		this.size = new Vector2f(0.0f);
	}
	
	/**
	 * Getter for the name and identifier of the Wrapper Window 
	 * You can use this name to render the window using the EngineUI functions
	 * If the String is null, the window is called "A window with no name"
	 * @return the name identifier
	 */
	public String name() {
		return this.name;
	}
	
	/**
	 * Renames the window with the given string
	 * If the String is null, the window is called "A window with no name"
	 * @param name to give to the window
	 */
	public void rename(String name) {
		if(name != null) {
			this.name = name;
		}else {
			this.name = "A window with no name";
		}
	}
	
	/**
	 * Set the Head Component of the UI Hierarchy of this UIWrapper
	 * @param component
	 */
	public void head(UIComponent component) {
		if(component != null) {
			this.head = component;
		}
	}
	
	/**
	 * Getter for the head component of the Wrapper
	 * @return
	 */
	public UIComponent head() {
		return this.head;
	}
	
	/**
	 * Overriding this let the user decide the behavior of the window when the screen is resized
	 * @param width of the screen
	 * @param height of the screen
	 */
	public void resizeAndPosition(float width, float height) {
		
	}
	
	/**
	 * Set the width and the height of the wrapper
	 * but also the one of the head component and
	 * build the wrapper if the size match the 
	 * adequate format
	 * @param width in pixels
	 * @param height in pixels
	 * @return true if the sizing is successful
	 */
	public boolean setWrapperSize(float width, float height) {
		if(this.head.setRealSize(width, height)) {
			this.size.set(width, height);
			this.head.build();
			return true;
		}
		return false;
	}
	
	/**
	 * Basic getter for the size of the Wrapper
	 * @return the wrapper size
	 */
	public Vector2f size() {
		return size;
	}
	
	/**
	 * Set the position offset of the window
	 * Set also the position of the head component to 0.0f
	 */
	public void setWrapperPosition(float x, float y) {
		this.position.set(x, y);
		this.head.setRealPosition(0.0f, 0.0f);
	}

	/**
	 * Basic getter for the position of the Wrapper
	 * @return the wrapper position
	 */
	public Vector2f position() {
		return position;
	}
	
	
	/**
	 * Destroy all the components
	 */
	public void destroy() {
		this.head.destroy();
	}
	
	
}
