package engine.gui;

import engine.gui.alignment.UIAlignmentHorizontal;

/**
 * This class defines the Horizontal Layout
 * Horizontal Layout stores the children component horizontally,
 * with padding between them, you can specify the
 * @author louis
 *
 */
public class UILayoutHorizontal extends UILayout{
	
	/** Alignment Horizontal*/
	protected UIAlignmentHorizontal horizontal;
	
	/**
	 * Default constructor for the horizontal Layout
	 */
	public UILayoutHorizontal() {
		this(0.0f, 0.0f, 0.0f, 0.0f);
	}
	
	
	/**
	 * Overloaded constructor for the UI Horizontal Layout
	 * @param top padding in pixel
	 * @param bottom padding in pixel
	 * @param left padding in pixel
	 * @param right padding in pixel
	 */
	public UILayoutHorizontal(float top, float bottom, float left, float right) {
		super(top, bottom, left, right);
		this.horizontal = UIAlignmentHorizontal.CENTER;
	}
	
	
	@Override
	public boolean findDimension(boolean fitChildren) {

		boolean modified = false;
		float minX =0.0f, minY = 0.0f;
		
		// Calculates all parameters
		if(fitChildren) {
			float optX = 0.0f, optY = 0.0f, maxX = 0.0f, maxY = 0.0f;
			for(UIComponent component : this.children){
				// Calculating minX
				minX += component.box().minimal().x();
				// Calculating minY
				if(component.box().minimal().y() > minY) { minY = component.box().minimal().y(); }
				// Calculating optX
				optX += component.box().optimal().x();
				// Calculating optY
				if(component.box().optimal().y() > optY) { optY = component.box().optimal().y(); }
				// Calculating MaxX
				maxX += component.box().maximal().x();
				//Calculating MaxY
				if(component.box().maximal().y() > maxY) { maxY = component.box().maximal().y(); }
				
			}

			minX += this.margin.right()+this.margin.left();
			optX += this.margin.right()+this.margin.left();
			maxX += this.margin.right()+this.margin.left();
			
			minY += this.margin.bottom()+this.margin.top();
			optY += this.margin.bottom()+this.margin.top();
			maxY += this.margin.bottom()+this.margin.top();
			
			if(		minX != box.minimal().x() || minY != box.minimal().y() 
				||	optX != box.optimal().x() || optY != box.optimal().y()
				||	maxX != box.maximal().x() || maxY != box.maximal().y()) {
				modified = true;
			}
			this.box.minimal(minX, minY);
			this.box.optimal(optX, optY);
			this.box.maximal(maxX, maxY);
			return modified;
		}
		// Only modify minimal values if necessary
		else {
			for(UIComponent component : this.children){
				// Calculating minX
				minX += component.box().minimal().x();
				// Calculating minY
				if(component.box().minimal().y() > minY) { minY = component.box().minimal().y(); }
				
			}

			minX += this.margin.right()+this.margin.left();
			minY += this.margin.bottom()+this.margin.top();
			minX = minX>this.box.minimal().x()?minX:this.box.minimal().x();
			minY = minY>this.box.minimal().y()?minY:this.box.minimal().y();
			
			if(		minX != box.minimal().x() || minY != box.minimal().y()) {
				modified = true;
			}
			this.box.minimal(minX, minY);
			return modified;
		}
		
	}

	@Override
	public void buildChildren() {
		
	}


	/**
	 * Set the horizontal Alignment of the panel
	 * @param horizontal alignment
	 */
	public void alignHorizontal(UIAlignmentHorizontal horizontal) {
		this.horizontal = horizontal;
	}
	
	
	/**
	 * Get the horizontal alignment of the panel
	 * @return the horizontal alignment
	 */
	public UIAlignmentHorizontal horizontalAlignment() {
		return this.horizontal;
	}

	
}
