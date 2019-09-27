package engine.gui;

import java.util.LinkedList;

public abstract class UILayout {

	/** Box containing the dimension of the component containing this Layout */
	protected UIBox box;
	/** Linked List of all the children UIComponent of the panel containing this Layout */
	protected LinkedList<UIComponent> children;
	/** Margin */
	protected UIMargin margin;
	/** FitChildren boolean */
	boolean fitChildren;
	
	
	/** 
	 * Default constructor for the UILayout component 
	 * Set all padding and space to zero
	 * */
	public UILayout() {
		this(0.0f, 0.0f, 0.0f, 0.0f);	
	}
	
	/**
	 * Default constructor for the UILayout component 
	 * Set all padding and space to zero
	 * @param top padding in pixels
	 * @param bottom padding in pixels
	 * @param left padding in pixels
	 * @param right padding in pixels
	 */
	public UILayout(float top, float bottom, float left, float right) {
		this.box = null;
		this.children = null;
		this.margin = new UIMargin(top, bottom, left, right);
	}
	
	
	/**
	 * This function calculates the Minimal, Optimal and Maximal
	 * Dimensions of the component using the data of the children
	 * components of the layout.
	 * If fitChildren is false, then the function will only verify
	 * that the panel size is bigger than the minimal size of the
	 * children
	 * @param fitChildren boolean
	 * @return true if a modification occurred on the component
	 */
	public abstract boolean findDimension();
	
	/**
	 * This Function computes the size and position of all children 
	 */
	public abstract void buildChildren();
	
	/**
	 * Used by the component holding the layout to give its box to the Layout
	 * @param box of the component holding the layout
	 */
	public void setOutputBox(UIBox box) {
		this.box = box;
	}
	
	public void setList(LinkedList<UIComponent> children) {
		this.children = children;
	}
	
	/**
	 * UIMargin Setter
	 * @param top in pixels
	 * @param bottom in pixels
	 * @param left in pixels
	 * @param right in pixels
	 */
	public void margin(float top, float bottom, float left, float right) {
		this.margin.set(top, bottom, left, right);
		this.findDimension();
	}
	
	/**
	 * Setter of the fitChildren boolean
	 * @param fitChildren
	 */
	public void fitChildren(boolean fitChildren) {
		this.fitChildren = fitChildren;
	}
}
