package engine.gui;

import org.joml.Vector2f;

import engine.ShaderManager;

/**
 * This class is the head of the UI Hierarchy
 * it provides function for building the window and render it to the screen
 * @author louis
 *
 */
public class UIWindow {
	
	/** Name and identifier of the Window Wrapper */
	private String name;

	/** Master panel of the Wrapper */
	private UIPanel head;
	/** Position offset of the Wrapper window */
	private final Vector2f position;
	/** Size of the Wrapper window */
	private final Vector2f size;
	/** Visibility of the window */
	private boolean visible;
	
	
	/**
	 * Constructor for the UIWrapper Object.
	 * @param name of the Wrapper window
	 */
	public UIWindow(String name) {
		if(name != null) {
			this.name = name;
		}else {
			this.name = "A window with no name";
		}
		this.position =  new Vector2f(0.0f);
		this.size = new Vector2f(0.0f);
		this.head = new UIPanel();
		this.visible = true;
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
	 * Set the Head panel of the UI Hierarchy of this UIWrapper
	 * @param component
	 */
	public void head(UIPanel panel) {
		if(panel != null) {
			this.head = panel;
		}
	}
	
	/**
	 * Getter for the head component of the Wrapper
	 * @return
	 */
	public UIPanel head() {
		return this.head;
	}
	
	/**
	 * Overriding this let the user decide the behavior of the window when the screen is resized
	 * @param width of the screen
	 * @param height of the screen
	 */
	void resize(float width, float height) {
		width = width<this.head.box.minimal().x()?this.head.box.minimal().x():(width>this.head.box.maximal().x()?this.head.box.maximal().x():width);
		height = height<this.head.box.minimal().y()?this.head.box.minimal().y():(height>this.head.box.maximal().y()?this.head.box.maximal().y():height);
		this.size.set(width, height);
	}
	
	/**
	 * Overriding this let the user decide of the behavior of the window when the screen is resized
	 * @param x
	 * @param y
	 */
	public void reposition(float x, float y) {
		// TODO Auto-generated method stub
		
	}
	
	/** Render method */
	public void render(ShaderManager shaders) {
		//this.head().render(shaders, this.position);
	}
	
	/**
	 * Set the width and the height of the wrapper
	 * but also the one of the head component and
	 * adequate format
	 * @param width in pixels
	 * @param height in pixels
	 */
	public void setWrapperSize(float width, float height) {
		this.head.build(0.0f, 0.0f, width, height);
		this.size.set(width, height);

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

	/**
	 * Getter for the visible member of UIWindow class
	 * @return true if the UIWindow is visible on the screen
	 */
	public boolean isVisible() {
		return this.visible;
	}
	
	/**
	 * Setter used to set the visibility of the window to true
	 */
	public void show() {
		this.visible = true;
	}
	
	/**
	 * Setter used to set the visibility of the window to false
	 */
	public void hide() {
		this.visible = false;
	}
	
	/**
	 * Getter for the x position of the window
	 * @return the x Axis position of the window
	 */
	public float x() {
		return this.position.x();
	}
	
	/**
	 * Getter for the y position of the window
	 * @return the y Axis position of the window
	 */
	public float y() {
		return this.position.y();
	}

	/**
	 * Getter for the width of the window
	 * @return the width of the window
	 */
	public float width() {
		return this.size.x();
	}
	
	/**
	 * Getter for the height of the window
	 * @return the height of the window
	 */
	public float height() {
		return this.size.y();
	}
	
	
}
