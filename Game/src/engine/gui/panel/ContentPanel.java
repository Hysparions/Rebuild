package engine.gui.panel;

import java.util.ArrayList;
import java.util.List;

import engine.gui.GUIComponent;
import engine.gui.shape.GLRectangle;

public abstract class ContentPanel extends GUIComponent {

	/** List containing others GUI components */
	protected List<GUIComponent> children;
	/** Boolean to true if the panel should set is appropriate size to it's children size */
	protected boolean adapt;
	/** Background */
	protected GLRectangle background;

	/**
	 * Basic constructor of the GUI Panel class
	 */
	public ContentPanel() {
		super();
		this.adapt = true;
		this.children = new ArrayList<GUIComponent>();
		this.background = null;
	}

	/**
	 * Constructor of the GUI Panel class specifying the following sizing behaviors
	 * @param optimalX	Optimal Horizontal size of the component
	 * @param optimalY	Optimal Vertical size of the component
	 */
	public ContentPanel(int optimalX, int optimalY) {
		super(optimalX, optimalY);
		this.adapt = false;
		this.children = new ArrayList<GUIComponent>();
		this.background = null;
	}

	/**
	 * Constructor of the GUI Primitive class specifying the following sizing behaviors
	 * @param minimalX	Minimal Horizontal size of the component
	 * @param minimalY	Minimal Vertical size of the component
	 * @param optimalX	Optimal Horizontal size of the component
	 * @param optimalY	Optimal Vertical size of the component
	 * @param maximalX	Maximal Horizontal size of the component
	 * @param maximalY  Maximal Vertical size of the component
	 */
	public ContentPanel(int minimalX, int minimalY, int optimalX, int optimalY, int maximalX, int maximalY) {
		super(minimalX, minimalY, optimalX, optimalY, maximalX, maximalY);
		this.adapt = false;
		this.children = new ArrayList<GUIComponent>();
	}
	
	/**
	 * Method adding a component to the container
	 * @param component the component to add to the container
	 */
	public void add(GUIComponent component){
		if(component != null) {
			// Adding the component to the children list
			this.children.add(component);
			// Set the parent of the component to the panel holding it (this)
			component.setParent(this);
			// Invalidate component
			component.unBuildWindow();
			// If the component behavior is to adapt its size to its children
			if(this.adapt) {
				// Compute the Panel Size
				this.findDimension();			
				// As we change the size of the panel we also have to change the size of all the Parents
				// That have the adaptive behavior
				ContentPanel comp = (ContentPanel)this.parent();
				while(comp != null) {
					if(comp.adapt) {
						comp.findDimension();
						comp = (ContentPanel)comp.parent();
					}else {
						break;
					}
				}
			}
		}
	}
	

	
	/**
	 * Method adding a component to the container
	 * @param component the component to remove from the container
	 */
	public void remove(GUIComponent component) {
		
		if(component != null){
			if(this.children.remove(component)) {
				component.setParent(null);
				// If the component behavior is to adapt its size to its children
				if(this.adapt) {
					// Compute the Panel Size
					this.findDimension();			
					// As we change the size of the panel we also have to change the size of all the Parents
					// That have the Adaptative behavior
					ContentPanel comp = (ContentPanel)this.parent();
					while(comp != null) {
						if(comp.adapt) {
							comp.findDimension();
							comp = (ContentPanel)comp.parent();
						}else {
							break;
						}
					}
				}
			}
		}

	}
	
	/**
	 * Method removing all the components from the container
	 */
	public void clear() {
		
		for(GUIComponent component : children) {
			component.setParent(null);
		}
		children.clear();
		// If the component behavior is to adapt its size to its children
		if(this.adapt) {
			// Compute the Panel Size
			this.findDimension();			
			// As we change the size of the panel we also have to change the size of all the Parents
			// That have the Adaptive behavior
			ContentPanel comp = (ContentPanel)this.parent();
			while(comp != null) {
				if(comp.adapt) {
					comp.findDimension();
					comp = (ContentPanel)comp.parent();
				}else {
					break;
				}
			}
		}
	}

	/**
	 * Method calculating the minimal optimal and maximal size of a container using children values
	 */
	protected abstract void findDimension();
	
	@Override
	public void render() {
		
		if(this.background != null) {
			background.render();
		}
		for(GUIComponent component : children) {
			component.render();
		}
	}
	

	/**
	 * Set the Background color of the Panel
	 * if the container doesn't have any background attached yet, it creates one
	 * @param red    color component
	 * @param green color component
	 * @param blue  color component
	 * @param alpha color component
	 */
	public void setBackground(int red, int green, int blue, int alpha) {
		if(background == null) {
			background = new GLRectangle(red, green, blue, alpha);
		}else {
			this.background.color().set(red, green, blue, alpha);
		}
	}

	/**
	 * Get all the components contained in this Panel
	 * 
	 * @return GUIComponent List of all direct children of this Panel
	 */
	public List<GUIComponent> getChildren() {
		return this.children;
	}
}
