package engine.gui;

import java.util.LinkedList;

import engine.gui.shape.UIRectangle;


public class UIPanel extends UIComponent{
	

	/** UIComponent Children List */
	protected LinkedList<UIComponent> children;
	/** Layout Manager */
	private UILayout layout;
	/** Overlay */
	protected UIRectangle overlay;

	/** Listeners List */
	//protected LinkedList<UIListener> listeners;
	
	/**
	 *  Default constructor for the panel
	 */
	public UIPanel() {
		super();
		this.children = new LinkedList<UIComponent>();
		this.fitChildren = true;
		this.layout(null);
		this.overlay = null;
	}
	
	/** Overloaded constructor */
	public UIPanel(UILayout layout) {
		this.children = new LinkedList<UIComponent>();
		this.fitChildren = true;
		this.layout(layout);
		this.overlay = null;
	}
	
	/** Overloaded constructor 
	 * This constructor is used to create a panel that doesn't fit their children
	 * @param box defining size of the panel
	 */
	public UIPanel(UIBox box) {
		super(box);
		this.children = new LinkedList<UIComponent>();
		this.fitChildren = false;
		this.layout(null);
		this.overlay = null;
	}
	
	/**
	 * Set the Panel Layout
	 * Set the Layout to horizontal Layout if layout is null
	 * @param layout of the panel
	 */
	public void layout(UILayout layout) {
		// Set the new Layout
		this.layout = layout;
		if(this.layout == null) {
			this.layout = new UILayoutHorizontal();
		}
		// Give the layout the component list
		this.layout.setList(children);
		// give the layout the box of the panel
		this.layout.setOutputBox(this.box);
		// Recompute the dimensions or correct minimal
		this.computeDimension();
	}
	
	/**
	 * @return the layout of the panel
	 */
	public UILayout layout() {
		return this.layout;
	}
	
	/**	 
	 * This function calculates the Minimal, Optimal and Maximal
	 * Dimensions of the component using the data of the children
	 * components of the layout.
	 * If fitChildren is false, then the function will only verify
	 * that the panel size is bigger than the minimal size of the
	 * children
	 */
	public void computeDimension() {
		this.layout.findDimension(fitChildren);
	}
	
	
	@Override
	public void build(float x, float y, float width, float height) {
		this.box.position(x, y);
		this.box.size(width, height);
		if(this.overlay != null) {
			this.overlay.build(x, y, width, height);
		}
	}
	
	/**
	 * Build the children using layout data and functions
	 */
	public void buildChildren() {
		this.layout.buildChildren();
	}
	
	/**
	 * @return true if the panel fits children
	 */
	public boolean fitChildren() {
		return this.fitChildren;
	}
	
	/**
	 * Set the behavior of the panel about fitting its children size or keep its dimensions
	 * @param fitChildren
	 */
	public void fitChildren(boolean fitChildren) {
		this.fitChildren = fitChildren;		
		this.computeDimension();
	}
	
	/**
	 * Set the prefered dimensions of the panel and disactivate the keepAspect and 
	 * @param minimalWidth dimension in pixel
	 * @param minimalHeight dimension in pixel
	 * @param optimalWidth dimension in pixel
	 * @param optimalHeight dimension in pixel
	 * @param maximalWidth dimension in pixel
	 * @param maximalHeight dimension in pixel
	 */
	public void setDimension(float minimalWidth, float minimalHeight, float optimalWidth, float optimalHeight, float maximalWidth, float maximalHeight) {
		this.box.minimal(minimalWidth, minimalHeight);
		this.box.optimal(optimalWidth, optimalHeight);
		this.box.maximal(maximalWidth, optimalHeight);
	}
	
	/**
	 * Add a component to the children list
	 * and it recompute the dimensions
	 * @param component
	 */
	public void add(UIComponent component) {
		if(component != null) {
			component.parent(this);
			this.children.add(component);
			this.computeDimension();
		}
	}
	
	/**
	 * Remove component from the panel
	 * @param component to remove
	 * @param destroy if true
	 */
	public void remove(UIComponent component, boolean destroy) {
		if(component != null) {
			if(this.children.remove(component)) {
				this.computeDimension();
				if(destroy) {
					component.destroy();
				}
			}
		}
	}
	
	/**
	 * Clear all the panels component
	 * @param destroy
	 */
	public void clear(boolean destroy) {
		for(UIComponent component : children) {
			component.destroy();
		}
		this.children.clear();
		this.computeDimension();
	}
	
	

	@Override
	public void destroy() {
		if(this.overlay != null) {
			this.overlay.destroy();
		}
		for(UIComponent comp : children) {
			comp.destroy();
		}
	}
}
