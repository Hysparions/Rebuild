package engine.gui.core;

import java.util.LinkedList;

import org.joml.Vector2f;

import engine.gui.layouts.UIHorizontalLayout;
import engine.gui.layouts.UILayout;
import engine.gui.renderable.sprite.UISprite;
import engine.gui.renderable.text.UIText;
import engine.gui.renderable.shape.UIShape;

public class UIComponent {

	/** Box defining the position, the size and the size hints of a UIComponent */
	protected final UIBox box;
	/** Layout of the UIComponent */
	protected UILayout layout;
	/** UIShape Renderable */
	protected UIShape shape;
	/** UISprite Renderable */
	protected UISprite sprite;
	/** UIText Renderable */
	protected UIText text;
	/** parent component */
	protected UIComponent parent;
	/** Linked list containing all the children of the component */
	protected final LinkedList<UIComponent> children;

	/**
	 * Default constructor for the UIComponent class
	 * Create an empty Component with the following parameters
	 * - UIBox is created with default constructor (size = minimal = optimal = maximal = 0.0f)
	 * - No children by default
	 * - No Layout by default
	 */
	public UIComponent() {
		this.box = new UIBox();		
		this.layout = null;
		this.parent = null;
		this.children = new LinkedList<UIComponent>();

	}
	
	/**
	 * Overloaded Constructor for the UIComponentClass
	 * Create a component with the given parameters
	 * @param box
	 */
	public UIComponent(UIBox box) {
		this.box = box;		
		this.layout = null;
		this.parent = null;
		this.children = new LinkedList<UIComponent>();

	}
	
	/**
	 * Overloaded Constructor for the UIComponentClass
	 * Create a component with the given parameters
	 * @param box
	 */
	public UIComponent(UIBox box, UILayout layout) {
		this.box = box;		
		this.layout = layout;
		this.parent = null;
		this.children = new LinkedList<UIComponent>();
	}
	
	/**
	 * Destroy the Renderable and do a recursive clean of all
	 * the children of this component
	 */
	public void destroy() {
		if(shape != null) {shape.destroy();}
		if(sprite != null) {sprite.destroy();}
		if(text != null) {text.destroy();}
		for(UIComponent comp : children) {
			comp.destroy();
		}
	}	
	
	/**
	 * Build the component's children True sizes  
 	 * using the Layout
	 * Build the component's renderable stuff
	 * with the actual true size of the component
	 */
	public void build() {
		if(hasChildren() && this.layout != null) {
			layout.findChildrenSizes();
		}
		//this.shape.build();
		//this.sprite.build();
		//this.text.build();
	}
	

	/**
	 * Add an UIComponent to the list of children
	 * @param component
	 */
	public void add(UIComponent component) {
		//First add the component as a child of this
		this.children.add(component);
		// Parent the component 
		component.parent = this;
		// Create a default Layout (Horizontal), if no one exists
		if(this.layout == null) {
			this.layout(new UIHorizontalLayout());
		}else {
			this.layout.addChild(component.box);
		}

		this.correctLayout();
	}
	
	public void remove(UIComponent component, boolean destroy) {
		// TODO stuff
	}
	
	
	/**
	 * Set the current layout for the component
	 * the layout is a powerful tool that is able to modify automatically size and position of component
	 * using some data provided in argument, as well as the type of the layout itself
	 * @param layout
	 */
	public void layout(UILayout layout) {
		if(layout != null) {
			this.layout = layout;
			this.layout.setBox(this.box);
			for(UIComponent child : children) {
				this.layout.addChild(child.box);
				correctLayout();
			}
		}
	}
	
	/**
	 * Correct layout when a component is added
	 */
	private boolean correctLayout() {

		boolean modified = false;
		if(layout != null) {
			if(this.box.fitContent()) {
				modified = this.layout.findDesiredSize();
			}else {
				modified = this.correctMinimal();
			}

			
			UIComponent parent = this.parent;
			while(modified && parent != null) {
				
				if(parent.box.fitContent()) {
					modified = parent.layout.findDesiredSize();
				}else {
					modified = parent.correctMinimal();
				}
				parent = parent.parent();
			}
		}
		return modified;
	}

	/**
	 * This functions computes the minimal desired size
	 * @return true if the box Size has been modified
	 */
	public boolean correctMinimal() {
		
		// MODIFY THIS TO AVOID SHIT WITH SCISSOR PANELS
		
		boolean modified = false;
		float minX =0.0f, minY = 0.0f;
		for(UIComponent component : this.children){
			// Calculating minX
			minX += component.box.minimal().x();
			// Calculating minY
			if(component.box.minimal().y() > minY) { minY = component.box.minimal().y(); }
		}
		minX += this.layout.right()+this.layout.left();
		minY += this.layout.bottom()+this.layout.top();
		
		minX = minX>box.minimal().x()?minX:box.minimal().x();
		minY = minY>box.minimal().y()?minY:box.minimal().y();
		if(minX != box.minimal().x() || minY != box.minimal().y()) {
			modified = true;
		}
		this.box.minimal(minX, minY);
		return modified;
	}
	
	/** 
	 * Set the final dimension for the component after layout management
	 * @param w
	 * @param h
	 * @return true if size affectation succeed
	 */
	public boolean setRealSize(float w, float h) {
		Vector2f min = this.box.minimal();
		Vector2f max = this.box.maximal();
		if(min.x() <= w && w <= max.x() && min.y() <= h && h <= max.y()) {
			this.box.size(w, h);
			return true;
		}
		return false;
	}
	
	/** 
	 * Set the final position offset of the component relative to the
	 * wrapper origin after layout management
	 * @param w
	 * @param h
	 * @return true if size affectation succeed
	 */
	public boolean setRealPosition(float x, float y) {
		if(this.parent != null) {
			Vector2f pos = this.parent.box.position();
			Vector2f size = this.parent.box.size();
			if(x > pos.x && x < pos.x + size.x && y > pos.y && y < pos.y + size.y) {
				this.box.position(x, y);
				return true;
			}
		}else {
			this.box.position(0.0f, 0.0f);
			return true;
		}
		return false;
	}

	/**
	 * Set new dimensions for the Component Box and deactivate content fitting
	 * @param minWidth in Pixel
	 * @param minHeight in Pixel
	 * @param optWidth in Pixel
	 * @param optHeight in Pixel
	 * @param maxWidth in Pixel
	 * @param maxHeight in Pixel
	 */
	public void setBoxDimension(float minWidth, float minHeight, float optWidth, float optHeight, float maxWidth, float maxHeight) {
		// Deactivate content fitting
		this.box.fitContent(false);
		// Set new Bounds
		this.box.set(minWidth, minHeight, optWidth, optHeight, maxWidth, maxHeight);
		// Resolve potential minimal requirements
		this.correctMinimal();
	}
	
	/**
	 * Check if the UIComponent have some children
	 * @return true if the component has any children
	 */
	public final boolean hasChildren() {
		return !children.isEmpty();
	}
	
	/**
	 * Simple Getter returning the parent of the component
	 * @return parent of this component
	 */
	public UIComponent parent() {
		return this.parent;
	}
	
	@Override
	public String toString() {
		return "UIComponent with Layout : " + this.layout.getClass().getSimpleName() + " and box " + box; 
	}

	/**
	 * Method returning the box minimal size
	 * @return vector containing minimal box size
	 */
	public Vector2f boxMinimal() {
		return new Vector2f(box.minimal());
	}
	
	/**
	 * Method returning the box optimal size
	 * @return vector containing optimal box size
	 */
	public Vector2f boxOptimal() {
		return new Vector2f(box.optimal());
	}
	
	/**
	 * Method returning the box maximal size
	 * @return vector containing maximal box size
	 */
	public Vector2f boxMaximal() {
		return new Vector2f(box.maximal());
	}
	
	
}
