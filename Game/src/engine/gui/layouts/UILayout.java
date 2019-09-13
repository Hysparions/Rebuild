package engine.gui.layouts;

import java.util.LinkedList;

import engine.gui.core.UIBox;

public abstract class UILayout {

	/** Box containing the dimension of the component containing this Layout */
	protected UIBox box;
	/** Linked List of all the UIBoxes of the children of the component containing this Layout */
	protected LinkedList<UIBox> children;
	
	/** Padding Top property in pixels*/
	protected float top;
	/** Padding Bottom property in pixels*/
	protected float bottom;
	/** Padding Left property in pixels*/
	protected float left;
	/** Padding Right property in pixels*/
	protected float right;
	
	
	/** 
	 * Default constructor for the UILayout component 
	 * Set all padding and space to zero
	 * */
	public UILayout() {
		this.box = null;
		this.children = new LinkedList<UIBox>();
		this.top = 0.0f;
		this.bottom = 0.0f;
		this.left = 0.0f;
		this.right = 0.0f;
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
		this.children = new LinkedList<UIBox>();
		this.top = top;
		this.bottom = bottom;
		this.left = left;
		this.right = right;
	}
	
	
	/**
	 * This functions computes the minimal desired size
	 * @return true if the box Size has been modified
	 */
	public abstract boolean findDesiredSize();
	
	/**
	 * This Function computes the size and position of all children 
	 */
	public abstract void findChildrenSizes();
	
	/**
	 * Used by the component holding the layout to give its box to the Layout
	 * @param box of the component holding the layout
	 */
	public void setBox(UIBox box) {
		this.box = box;
	}
	
	/**
	 * Add a Box to the children list
	 * @param Box to add to children
	 */
	public void addChild(UIBox box) {
		this.children.add(box);
	}
	
	/**
	 * Remove a Box from the children List if it's inside
	 * @param Box to remove of children list
	 */
	public void removeChild() {
		this.children.remove(box);
	}

	/**
	 * Getter for the top padding value
	 * @return top value in pixels
	 */
	public float top() {
		return top;
	}

	/**
	 * Setter for the top padding value
	 * @param top value in pixels
	 */
	public void top(float top) {
		this.top = top;
	}

	/**
	 * Getter for the bottom padding value
	 * @return bottom value in pixels
	 */
	public float bottom() {
		return bottom;
	}

	/**
	 * Setter for the bottom padding value
	 * @param bottom value in pixels
	 */
	public void bottom(float bottom) {
		this.bottom = bottom;
	}
	
	/**
	 * Getter for the left padding value
	 * @return left value in pixels
	 */
	public float left() {
		return left;
	}

	/**
	 * Setter for the left padding value
	 * @param left value in pixels
	 */
	public void left(float left) {
		this.left = left;
	}

	/**
	 * Getter for the right padding value
	 * @return right value in pixels
	 */
	public float right() {
		return right;
	}

	/**
	 * Setter for the right padding value
	 * @param right value in pixels
	 */
	public void right(float right) {
		this.right = right;
	}
	
	
}
