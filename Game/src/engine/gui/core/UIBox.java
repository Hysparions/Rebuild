package engine.gui.core;

import org.joml.Vector2f;

/**
 * This class contains the data about the position and the size hints of a UIComponent
 * @author louis
 *
 */
public class UIBox {

	/** Position of the Box */
	private Vector2f position;
	/** Real Size of the Box */
	private Vector2f size;
	
	/** minimal dimensions of a box */
	private Vector2f minimal;
	/** optimal dimensions of a box */
	private Vector2f optimal;
	/** maximal dimensions of a box */
	private Vector2f maximal;
	
	/** Boolean used to know if the box should adapt its Dimension to the Content of the component */
	private boolean fitContent;
	/** Boolean used to know if the box keep Aspect Ratio Between optimal width and height */
	private boolean keepAspect;
	
	/**
	 * Default constructor for the UIBox Class
	 * Creates a UIBox with the following parameters
	 * - Position set to 0 0
	 * - RealSize set to 0 0
	 * - minimal set to 0 0
	 * - optimal set to 0 0
	 * - maximal set to 0 0
	 * - fitContent to True
	 * - keepsAspect to False
	 */
	public UIBox() {
		this.position = new Vector2f(0.0f);
		this.size = new Vector2f(0.0f);
		this.minimal = new Vector2f(0.0f);
		this.optimal = new Vector2f(0.0f);
		this.maximal = new Vector2f(0.0f);
		this.fitContent = true;
		this.keepAspect = false;
	}
	
	/**
	 * Overloaded Constructor of the UIBox Class
	 * Creates a UIBox with the following parameters
	 * - Position set to 0 0
	 * - RealSize set to 0 0
	 * - minimal set to width height
	 * - optimal set to width height
	 * - maximal set to width height
	 * - fitContent to False
	 * - keepsAspect to True
	 * @param width in pixels
	 * @param height in pixels
	 */
	public UIBox(float width, float height) {
		this.position = new Vector2f(0.0f);
		this.size = new Vector2f(0, 0);
		width = width<0.0f?0.0f:width;
		height = height<0.0f?0.0f:height;
		this.minimal = new Vector2f(width, height);
		this.optimal = new Vector2f(width, height);
		this.maximal = new Vector2f(width, height);
		this.fitContent = false;
		this.keepAspect = true;
	}
	
	
	/**
	 * Overloaded Constructor of the UIBox Class
	 * Creates a UIBox with the following parameters
	 * - Position set to 0 0
	 * - RealSize set to 0 0
	 * - minimal set to minWidth minHeight
	 * - optimal set to optWidth optHeight
	 * - maximal set to maxWidth maxHeight
	 * - fitChildren to False
	 * - keepsAspect to False
	 * @param minWidth in pixels
	 * @param minHeight in pixels
	 * @param optWidth in pixels
	 * @param optHeight in pixels
	 * @param maxWidth in pixels
	 * @param maxHeight in pixels
	 */
	public UIBox(float minWidth, float minHeight, float optWidth, float optHeight, float maxWidth, float maxHeight) {
		this.position = new Vector2f(0.0f);
		this.size = new Vector2f(0.0f);
		this.minimal = new Vector2f(minWidth, minHeight);
		this.optimal = new Vector2f(optWidth, optHeight);
		this.maximal = new Vector2f(maxWidth, maxHeight);
		this.optimal.x = this.optimal.x<this.minimal.x?this.minimal.x:this.optimal.x;
		this.optimal.y = this.optimal.y<this.minimal.y?this.minimal.y:this.optimal.y;
		this.maximal.x = this.maximal.x<this.optimal.x?this.optimal.x:this.maximal.x;
		this.maximal.y = this.maximal.y<this.optimal.y?this.optimal.y:this.maximal.y;
		this.fitContent = false;
		this.keepAspect = false;
	}
	
	
	/**
	 * Update the position and force values, may be dangerous
	 * Used normally only by animations and layouts
	 * @param x value in pixels
	 * @param y value in pixels
	 */
	public void position(float x, float y) {
		this.position.set(x, y);
	}
	
	/**
	 * Getter for the position of the UIBox
	 * @return a vector2f containing x and y position of the component
	 */
	public Vector2f position() {
		return this.position;
	}
	
	/**
	 * Update the position and force values, may be dangerous
	 * Used normally only by animations and layouts
	 * @param width in pixels
	 * @param height in pixels
	 */
	public void size(float width, float height) {
		width = width<0.0f?0.0f:width;
		height = height<0.0f?0.0f:height;
		this.size.set(width, height);
	}
	
	/**
	 * Getter for the size of the UIBox
	 * @return a vector2f containing width and height of the component
	 */
	public Vector2f size() {
		return this.size();
	}
	
	/**
	 * Setter for the minimal value of the UIBox
	 * @param width in pixels
	 * @param height in pixels
	 */
	public void minimal(float width, float height) {
		width = width<0.0f?0.0f:width;
		height = height<0.0f?0.0f:height;
		this.minimal.set(width, height);
		this.optimal.x = this.optimal.x<this.minimal.x?this.minimal.x:this.optimal.x;
		this.optimal.y = this.optimal.y<this.minimal.y?this.minimal.y:this.optimal.y;
		this.maximal.x = this.maximal.x<this.optimal.x?this.optimal.x:this.maximal.x;
		this.maximal.y = this.maximal.y<this.optimal.y?this.optimal.y:this.maximal.y;
		if(keepAspect) {
			correctAspect();
		}
	}
	
	/**
	 * Getter for the minimal dimension of the UIBox
	 * @return a Vector2f containing the width and height optimal dimensions of the component
	 */
	public Vector2f minimal() {
		return this.minimal;
	}
	
	/**
	 * Setter for the optimal value of the UIBox
	 * @param width in pixels
	 * @param height in pixels
	 */
	public void optimal(float width, float height) {
		width = width<0.0f?0.0f:width;
		height = height<0.0f?0.0f:height;
		this.optimal.set(width, height);
		this.minimal.x = this.optimal.x>this.minimal.x?this.minimal.x:this.optimal.x;
		this.minimal.y = this.optimal.y>this.minimal.y?this.minimal.y:this.optimal.y;
		this.maximal.x = this.maximal.x<this.optimal.x?this.optimal.x:this.maximal.x;
		this.maximal.y = this.maximal.y<this.optimal.y?this.optimal.y:this.maximal.y;
		if(keepAspect) {
			correctAspect();
		}
	}
	
	/**
	 * Getter for the optimal dimension of the UIBox
	 * @return a Vector2f containing the width and height optimal dimensions of the component
	 */
	public Vector2f optimal() {
		return this.optimal;
	}
	
	/**
	 * Setter for the maximal value of the UIBox
	 * @param width in pixels
	 * @param height in pixels
	 */
	public void maximal(float width, float height) {
		width = width<0.0f?0.0f:width;
		height = height<0.0f?0.0f:height;
		this.maximal.set(width, height);
		this.minimal.x = this.optimal.x>this.minimal.x?this.minimal.x:this.optimal.x;
		this.minimal.y = this.optimal.y>this.minimal.y?this.minimal.y:this.optimal.y;
		this.optimal.x = this.maximal.x>this.optimal.x?this.optimal.x:this.maximal.x;
		this.optimal.y = this.maximal.y>this.optimal.y?this.optimal.y:this.maximal.y;

		if(keepAspect) {
			correctAspect();
		}
	}
	
	/**
	 * Getter for the maximal dimension of the UIBox
	 * @return a Vector2f containing the width and height maximal dimensions of the component
	 */
	public Vector2f maximal() {
		return this.maximal;
	}
	
	/**
	 * Overloaded Constructor of the UIBox Class
	 * Creates a UIBox with the following parameters
	 * - minimal set to minWidth minHeight
	 * - optimal set to optWidth optHeight
	 * - maximal set to maxWidth maxHeight
	 * @param minWidth in pixels
	 * @param minHeight in pixels
	 * @param optWidth in pixels
	 * @param optHeight in pixels
	 * @param maxWidth in pixels
	 * @param maxHeight in pixels
	 */
	public void set(float minWidth, float minHeight, float optWidth, float optHeight, float maxWidth, float maxHeight) {

		this.minimal = new Vector2f(minWidth, minHeight);
		this.optimal = new Vector2f(optWidth, optHeight);
		this.maximal = new Vector2f(maxWidth, maxHeight);
		this.optimal.x = this.optimal.x<this.minimal.x?this.minimal.x:this.optimal.x;
		this.optimal.y = this.optimal.y<this.minimal.y?this.minimal.y:this.optimal.y;
		this.maximal.x = this.maximal.x<this.optimal.x?this.optimal.x:this.maximal.x;
		this.maximal.y = this.maximal.y<this.optimal.y?this.optimal.y:this.maximal.y;
		
		if(keepAspect) {
			correctAspect();
		}
	}
	
	@Override
	public String toString() {
		return "UIBox with : Position " + position.x() + " " + position.y() +
				" Size " + size.x() + " " + size.y() +  
				" Minimal " + minimal.x() + " " + minimal.y() +  
				" Optimal " + optimal.x() + " " + optimal.y() +  
				" Maximal " + maximal.x() + " " + maximal.y(); 
	}
	
	public boolean fitContent() {
		return this.fitContent;
	}
	
	/**
	 * Set this to true if you want the component to adapt its size to its children
	 * Desactivate AspectRatio
	 * @param fitChildren to true
	 */
	public void fitContent(boolean fitContent) {
		this.fitContent = fitContent;
	}
	
	/**
	 * Getter for the keep Aspect field
	 * @return true if the box should keep its aspect between optimal and minimal size
	 */
	public boolean keepAspect() {
		return this.keepAspect;
	}
	
	/**
	 * Setter for the Aspect fitting option
	 * If the min and max size doesn't fit optimal ratio, 
	 * this function calculates automatically new min and max dimensions
	 * Disactivate 
	 * @param keepAspect boolean to set to true if you want the box to keep its aspect
	 */
	public void keepAspect(boolean keepAspect) {
		this.keepAspect = keepAspect;
		if(this.keepAspect) {
			this.correctAspect();
		}
	}
	
	/**
	 * Function that calculates correct opt and max size based on min aspect ratio
	 */
	private void correctAspect(){
		float minimalAspect = minimal.y>0.0f?(minimal.x/minimal.y):0.0f;
		float optimalAspect = optimal.y>0.0f?(optimal.x/optimal.y):0.0f;
		float maximalAspect = maximal.y>0.0f?(maximal.x/maximal.y):0.0f;
		// If optimal aspect exists
		if(optimalAspect != 0.0f) {
			// Compute minimal aspect
			if(minimalAspect != 0.0f) {
				if(optimalAspect > minimalAspect) {
					minimal.x*=(optimalAspect/minimalAspect);
				}else {
					minimal.y*=(optimalAspect/minimalAspect);
				}
			}else {
				minimal.set(0.0f);
			}
			// Compute maximal Aspect
			if(maximalAspect != 0.0f) {
				if(optimalAspect > maximalAspect) {
					maximal.y/=(maximalAspect/optimalAspect);
				}else {
					maximal.x/=(maximalAspect/optimalAspect);
				}
			}else {
				maximal.set(0.0f);
			}
			
		}else {
			minimal.set(optimal);
			maximal.set(optimal);
		}
		
	}
}
