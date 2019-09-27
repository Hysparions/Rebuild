package engine.gui;

import org.joml.Vector2i;

import engine.ShaderManager;

/**
 * This class is the head of the UI Hierarchy
 * it provides function for building the window and render it to the screen
 * @author louis
 *
 */
public class UIWindow{
	
	/** Name and identifier of the Window Wrapper */
	private String name;
	/** Visibility of the window */
	private boolean visible;
	/** Panel content */
	UIPanel panel;
	
	
	/**
	 * Constructor for the UIWrapper Object.
	 * @param name of the Wrapper window
	 */
	public UIWindow(String name) {
		super();
		if(name != null) {
			this.name = name;
		}else {
			this.name = "A window with no name";
		}
		this.panel = new UIPanel();
		this.visible = true;
	}

	/**
	 * Render method	
	 * @param shaders
	 */
	void render(ShaderManager shaders) {
		this.panel.render(shaders);
	}
	
	/**
	 * Overriding this let the user decide the behavior of the window when the screen is resized
	 * @param width of the screen
	 * @param height of the screen
	 */
	public void resize(float width, float height) {
		this.panel.box.size(width, height);
	}
	
	/**
	 * Overriding this let the user decide of the behavior of the window when the screen is resized
	 * @param x in pixels
	 * @param y in pixels
	 */
	public void reposition(float x, float y) {
		this.panel.box.position(x, y);
	}
	
	public void overlay(int red, int green, int blue, int alpha) {
		this.panel.overlay(red, green, blue, alpha);
	}

	/**
	 * Add a component to the window panel
	 * @param component to add
	 */
	public void add(UIComponent component) {
		this.panel.add(component);
		// If the actual size isn't sufficient to satisfy minimal dimension 
		this.panel.box.size().x = this.panel.box.size().x()<this.panel.box().minimal().x()?this.panel.box().minimal().x():this.panel.box.size().x();
		this.panel.box.size().y = this.panel.box.size().y()<this.panel.box().minimal().y()?this.panel.box().minimal().y():this.panel.box.size().y();
	}
	
	public void remove(UIComponent component, boolean destroy) {
		this.panel.remove(component, destroy);
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
		return this.panel.box.position().x();
	}
	
	/**
	 * Getter for the y position of the window
	 * @return the y Axis position of the window
	 */
	public float y() {
		return this.panel.box.position().y();
	}

	/**
	 * Getter for the width of the window
	 * @return the width of the window
	 */
	public float width() {
		return this.panel.box.size().x();
	}
	
	/**
	 * Getter for the height of the window
	 * @return the height of the window
	 */
	public float height() {
		return this.panel.box.size().y();
	}

	/**
	 * UIMargin Setter
	 * @param top in pixels
	 * @param bottom in pixels
	 * @param left in pixels
	 * @param right in pixels
	 */
	public void margin(float top, float bottom, float left, float right) {
		this.panel.margin(top, bottom, left, right);
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
	
	@Override
	public String toString() {
		return "Window " + this.name() + " with panel " + this.panel;
	}
	
	/**
	 * Destroy the window and all its components
	 */
	public void destroy() {
		this.panel.destroy();
	}

	/**
	 * Override this method to resize your window
	 * @param size of the engine
	 */
	public void updateBox(Vector2i size) {
		// TODO Auto-generated method stub
		
	}
	
}
