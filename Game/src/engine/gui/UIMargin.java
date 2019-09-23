package engine.gui;

/** 
 * Margin data
 * @author louis
 *
 */
public final class UIMargin {

	/** Padding Top property in pixels*/
	private float top;
	/** Padding Bottom property in pixels*/
	private float bottom;
	/** Padding Left property in pixels*/
	private float left;
	/** Padding Right property in pixels*/
	private float right;
	
	/**
	 * UIMargin Constructor
	 * @param top
	 * @param bottom
	 * @param left
	 * @param right
	 */
	public UIMargin(float top, float bottom, float left, float right) {
		this.top = top;
		this.top = top>0.0f?top:0.0f;
		this.bottom =bottom;
		this.bottom = bottom>0.0f?bottom:0.0f;
		this.left = left;
		this.left = left>0.0f?left:0.0f;
		this.right = right;
		this.right = right>0.0f?right:0.0f;
	}
	
	/**
	 * Default Constructor setting every padding to zero;
	 */
	public UIMargin() {
		this(0.0f, 0.0f, 0.0f, 0.0f);
	}
	
	/**
	 * UIMargin Setter
	 * @param top
	 * @param bottom
	 * @param left
	 * @param right
	 */
	public void set(float top, float bottom, float left, float right) {
		this.top = top;
		this.top = top>0.0f?top:0.0f;
		this.bottom =bottom;
		this.bottom = bottom>0.0f?bottom:0.0f;
		this.left = left;
		this.left = left>0.0f?left:0.0f;
		this.right = right;
		this.right = right>0.0f?right:0.0f;
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
		this.top = top>0.0f?top:0.0f;
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
		this.bottom =bottom;
		this.bottom = bottom>0.0f?bottom:0.0f;
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
		this.left = left>0.0f?left:0.0f;
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
		this.right = right>0.0f?right:0.0f;
	}
}
