package engine.gui;

import org.joml.Vector2f;

/**
 * This class is the parent class of each GUI Component including Visuals, Windows, Panels and so on...
 * @author louis
 */
public abstract class GUIComponent {
	
	/** Relative Position */
	protected Vector2f position;
	/** Relative Surface */
	protected Vector2f size;

	/** Minimal Size */
	protected Vector2f minimal;
	/** Optimal Size */
	protected Vector2f optimal;
	/** Maximal Size */
	protected Vector2f maximal;
	
	/** Hide option when not visible */
	protected boolean visible;


	/** Parent Container Component */
	protected GUIComponent parent;
	/** Built is used to know if the component and all its children is built */
	protected boolean built;

	/**
	 * Default Constructor of the GUI Component class
	 */
	public GUIComponent() {
		this.position = new Vector2f(0.0f);
		this.size = new Vector2f(0.0f);

		this.minimal = new Vector2f(0.0f);
		this.optimal = new Vector2f(0.0f);
		this.maximal = new Vector2f(0.0f);

		visible = true;
		
		this.parent = null;
		this.built = false;
	}

	/**
	 * Constructor of the GUI Component class specifying the following sizing behaviors
	 * @param optimalX	Optimal Horizontal size of the component
	 * @param optimalY	Optimal Vertical size of the component
	 */
	public GUIComponent(int optimalX, int optimalY) {
		this.position = new Vector2f(0.0f);
		this.size = new Vector2f(0.0f);

		this.minimal = new Vector2f(optimalX, optimalY);
		this.optimal = new Vector2f(optimalX, optimalY);
		this.maximal = new Vector2f(optimalX, optimalY);

		this.parent = null;
		this.built = false;
	}

	/**
	 * Constructor of the GUI Component class specifying the following sizing behaviors
	 * @param minimalX	Minimal Horizontal size of the component
	 * @param minimalY	Minimal Vertical size of the component
	 * @param optimalX	Optimal Horizontal size of the component
	 * @param optimalY	Optimal Vertical size of the component
	 * @param maximalX	Maximal Horizontal size of the component
	 * @param maximalY  Maximal Vertical size of the component
	 */
	public GUIComponent(int minimalX, int minimalY, int optimalX, int optimalY, int maximalX, int maximalY) {
		this.position = new Vector2f(0.0f);
		this.size = new Vector2f(0.0f);

		this.minimal = new Vector2f(minimalX, minimalY);
		this.optimal = new Vector2f(optimalX, optimalY);
		this.maximal = new Vector2f(maximalX, maximalY);

		this.parent = null;
		this.built = false;
	}
	
	/**
	 * Method calculating the minimal optimal and maximal size of a container using children values
	 * @param x position
	 * @param y position 
	 * @param w width 
	 * @param h height 
	 */
	public abstract void build(float x, float y, float w, float h);
	
	/**
	 * Render the component if necessary
	 */
	public abstract void render();


	/** Method Returning the position of the component 
	 * @return position vector
	 */
	public Vector2f position() {
		return this.position;
	}

	/** Method Returning the size of the component  
	 * @return size vector
	 */
	public Vector2f size() {
		return this.size;
	}

	/** Method Returning the minimal size of the component  
	 * @return minimal size vector
	 */
	public Vector2f minimal() {
		return this.minimal;
	}

	/** Method Returning the optimal size of the component  
	 * @return optimal size vector
	 */
	public Vector2f optimal() {
		return this.optimal;
	}

	/** Method Returning the maximal size of the component  
	 * @return maximal size vector
	 */
	public Vector2f maximal() {
		return this.maximal;
	}

	
	/** Method Returning the parent container of the component  
	 * @return parent
	 */
	public GUIComponent parent() {
		return this.parent;
	}
	
	/** Method Returning the parent container of the component 
	 * @param parent the new Parent panel
	 */
	public void setParent(GUIComponent parent) {
		this.parent = parent;
	}
	
	/** Method Validating the component 
	 * @return True if the component is validated
	 */
	public boolean isBuilt() {
		return this.built;
	}
	
	/**
	 * method setting built boolean
	 * @param built
	 */
	public void setBuild(boolean built) {
		this.built = built;
	}
	
	/** Method UnValidating the Window */
	public void unBuildWindow() {
		GUIComponent component = this;
		component.setBuild(false);
		while(component.parent() != null) {
			component = component.parent();
			component.setBuild(false);
		}
	}
	
	public void hide() {
		this.visible =false;
	}
	
	public void show() {
		this.visible = true;
	}
	
	public boolean isVisible() {
		return this.visible;
	}

	/** Method Printing Size : minimal, optimal, maximal data*/
	public void printSizeInfo() {
		System.out.print	("Position : "+ position.x()+ " " + position.y()+ "  |  ");
		System.out.print	("Size : "    + size.x()    + " " + size.y()    + "  |  ");
		System.out.print	("Minimal : " + minimal.x() + " " + minimal.y() + "  |  ");
		System.out.print	("Optimal : " + optimal.x() + " " + optimal.y() + "  |  ");
		System.out.println	("Maximal : " + maximal.x() + " " + maximal.y());
	}
}
