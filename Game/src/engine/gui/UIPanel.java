package engine.gui;

import java.util.LinkedList;

import engine.ShaderManager;
import engine.gui.shape.UIRectangle;
import engine.utils.Color;


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
		this.layout(null);
		this.fitChildren(true);
		this.overlay = null;
	}
	
	/** Overloaded constructor */
	public UIPanel(UILayout layout) {
		this.children = new LinkedList<UIComponent>();
		this.layout(layout);
		this.fitChildren(true);
		this.overlay = null;
	}
	
	/** Overloaded constructor 
	 * This constructor is used to create a panel that doesn't fit their children
	 * @param box defining size of the panel
	 */
	public UIPanel(UIBox box) {
		super(box);
		this.children = new LinkedList<UIComponent>();
		this.layout(null);
		this.fitChildren(false);
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
		if(layout == null) {
			this.layout = new UILayoutHorizontal();
		}
		// Give the layout the component list
		this.layout.setList(children);
		// give the layout the box of the panel
		this.layout.setOutputBox(this.box);
		// Recompute the dimensions or correct minimal
		this.layout.findDimension();
	}
	
	/**
	 * @return the layout of the panel
	 */
	public UILayout layout() {
		return this.layout;
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
		this.layout.fitChildren(this.fitChildren);
		this.layout.findDimension();
	}
	
	/**
	 * Set the prefered dimensions of the panel and disactivate 
	 * keep Aspect and fitChildren flags
	 * @param minimalWidth dimension in pixel
	 * @param minimalHeight dimension in pixel
	 * @param optimalWidth dimension in pixel
	 * @param optimalHeight dimension in pixel
	 * @param maximalWidth dimension in pixel
	 * @param maximalHeight dimension in pixel
	 */
	public void dimension(float minimalWidth, float minimalHeight, float optimalWidth, float optimalHeight, float maximalWidth, float maximalHeight) {
		this.box.minimal(minimalWidth, minimalHeight);
		this.box.optimal(optimalWidth, optimalHeight);
		this.box.maximal(maximalWidth, optimalHeight);
		this.fitChildren(false);
		this.layout.findDimension();
	}
	
	/**
	 * Render method for panels
	 * @param shaders
	 */
	public void render(ShaderManager shaders) {
		if(this.overlay != null) {
			this.overlay.render(shaders);
		}
		for(UIComponent component : children) {
			component.render(shaders);
		}
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
			this.layout.findDimension();
			UIPanel panel = this;
			while(panel.parent() != null) {
				panel = (UIPanel) panel.parent();
				if(!panel.layout.findDimension()) {
					break;
				}
			}
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
				this.layout.findDimension();
				if(destroy) {
					component.destroy();
				}
			}
		}
	}
	

	public void overlay(int red, int green, int blue, int alpha) {
		if(this.overlay == null) {
			this.overlay = new UIRectangle(this.box, new Color(red, green, blue, alpha));
		}else {
			this.overlay.color(red, green, blue, alpha);
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
		this.layout.findDimension();
	}
	
	/**
	 * UIMargin Setter
	 * @param top in pixels
	 * @param bottom in pixels
	 * @param left in pixels
	 * @param right in pixels
	 */
	public void margin(float top, float bottom, float left, float right) {
		this.layout.margin(top, bottom, left, right);
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
