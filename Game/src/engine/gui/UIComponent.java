package engine.gui;

public abstract class UIComponent {

	/** UIBox containing size and position of the component */
	protected UIBox box;
	/** Parent component */
	protected UIComponent parent;
	/** Boolean used to know if the panel should adapt its size to its children */
	protected boolean fitChildren;
	
	/**
	 * Default constructor for the UIcomponent class 
	 * Both box size and position are set zero
	 * Both dimensions hints are set to zero
	 * Parent UIComponent is set to null
	 */
	public UIComponent() {
		this(null);
	}
	
	/** 
	 * Overloaded constructor for UI Box sharing between a component and another
	 * @param box
	 */
	public UIComponent(UIBox box) {
		if(this.box == null) {
			this.box = new UIBox();
		}else {
			this.box = box;
		}
		this.parent = null;
		this.fitChildren = false;
	}
	
	/**
	 * Parent setter usable only in package
	 * @param parent of the component
	 */
	final void parent(UIComponent parent) {
		this.parent = parent;
	}
	
	/**
	 * Getter for the component's parent
	 * @return the component's parent
	 */
	public final UIComponent parent() {
		return this.parent;
	}
	
	/**
	 * Returns the box of the component
	 * @return
	 */
	UIBox box() {
		return this.box;
	}
	
	/**
	 * Builds the component's true size and position
	 * @param x position of the component
	 * @param y position of the component
	 * @param width of the component
	 * @param height of the component
	 */
	public abstract void build(float x, float y, float width, float height);
	
	/**
	 * Destroy this component if necessary and all children components
	 */
	public abstract void destroy();
	
	@Override
	public String toString() {
		return "Component with dimensions " + this.box; 
	}
	
}
