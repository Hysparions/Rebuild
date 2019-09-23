package engine.gui;

import engine.gui.alignment.UIAlignmentVertical;

/**
 * This class defines the Horizontal Layout
 * Vertical Layout stores the children component Vertically,
 * with padding between them, you can specify the
 * @author louis
 *
 */
public class UILayoutVertical extends UILayout{
	
	/** Alignment Vertical */
	protected UIAlignmentVertical vertical;
	
	/**
	 * Default constructor for the horizontal Layout
	 */
	public UILayoutVertical() {
		this(0.0f, 0.0f, 0.0f, 0.0f);
		this.vertical = UIAlignmentVertical.CENTER;
	}
	
	
	/**
	 * Overloaded constructor for the UI Vertical Layout
	 * @param top padding in pixel
	 * @param bottom padding in pixel
	 * @param left padding in pixel
	 * @param right padding in pixel
	 */
	public UILayoutVertical(float top, float bottom, float left, float right) {
		super(top, bottom, left, right);
		this.vertical = UIAlignmentVertical.CENTER;
	}

	@Override
	public boolean findDimension(boolean fitChildren) {
		boolean modified = false;
		float minX =0.0f, minY = 0.0f;
		
		// Calculates all parameters
		if(fitChildren) {
			float optX = 0.0f, optY = 0.0f, maxX = 0.0f, maxY = 0.0f;
			for(UIComponent component : this.children){
				// Calculating minY
				minY += component.box().minimal().y();
				// Calculating minX
				if(component.box().minimal().x() > minX) { minX = component.box().minimal().x(); }
				// Calculating optY
				optY += component.box().optimal().y();
				// Calculating optX
				if(component.box().optimal().x() > optX) { optX = component.box().optimal().x(); }
				// Calculating MaxY
				maxY += component.box().maximal().y();
				//Calculating MaxX
				if(component.box().maximal().x() > maxX) { maxX = component.box().maximal().x(); }
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
				// Calculating minY
				minY += component.box().minimal().y();
				// Calculating minX
				if(component.box().minimal().x() > minX) { minX = component.box().minimal().x(); }
			}

			minX += this.margin.right()+this.margin.left();
			minY += this.margin.bottom()+this.margin.top();
			minX = minX>this.box.minimal().x()?minX:this.box.minimal().x();
			minY = minY>this.box.minimal().y()?minY:this.box.minimal().y();
			
			if(	minX != box.minimal().x() || minY != box.minimal().y()) {
				modified = true;
			}
			this.box.minimal(minX, minY);
			return modified;
		}
	}

	@Override
	public void buildChildren() {
		
		float x = this.box.position().x();
		float y = this.box.position().y();
		float w = this.box.size().x();
		float h = this.box.size().y();
		
		if(this.box.minimal().y() >= h && this.box.maximal().y() <= h){

			// Compute interpolation ratio between min and max
			float ratio;
			if(this.box.maximal().y() - this.box.minimal().y() == 0.0f) { 
				ratio = 0.0f;
			}else {
				ratio = (h-this.box.minimal().y()) / (this.box.maximal().y() - this.box.minimal().y());
			}
			// Keep offset
			float offset = this.box.position().y();
			for(UIComponent component : children) {
				component.build(x, offset, w , box.minimal().y() +(box.maximal().y()-box.minimal().y()) * ratio );
				offset += box.minimal().y() +(box.maximal().y()-box.minimal().y()) * ratio;
			}
		}
		// Case over maximal
		else {
			float offset = y;
			// Compute offset position with the alignment
			if(this.vertical == UIAlignmentVertical.TOP) {
				offset = y;
			}else if(this.vertical == UIAlignmentVertical.CENTER) {
				offset = y + ((h-this.box.maximal().y())/2.0f);
			}else if(this.vertical == UIAlignmentVertical.BOTTOM) {
				offset = y + h-this.box.maximal().y();
			}
			for(UIComponent component : children) {
				component.build(x, offset, w , box.maximal().y());
				offset += box.maximal().y();
			}
		}
	}


	/**
	 * Set the vertical Alignment of the panel
	 * @param vertical alignment
	 */
	public void alignVertical(UIAlignmentVertical vertical) {
		this.vertical = vertical;
	}
	
	
	/**
	 * Get the vertical alignment of the panel
	 * @return the vertical alignment
	 */
	public UIAlignmentVertical verticalAlignment() {
		return this.vertical;
	}

}
