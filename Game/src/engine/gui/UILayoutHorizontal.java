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
	public boolean findDimension() {

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

		float x = this.box.position().x() + margin.left();
		float y = this.box.position().y() + margin.top();
		float w = this.box.size().x();
		float h = this.box.size().y() - margin.top()-margin.bottom();

		if(this.box.minimal().x() <= w && this.box.maximal().x() >= w){
			
			// Compute interpolation ratio between min and max
			float ratio;
			if(this.box.maximal().x() - this.box.minimal().x() == 0.0f) { 
				ratio = 0.0f;
			}else {
				ratio = (w-this.box.minimal().x()) / (this.box.maximal().x() - this.box.minimal().x());
			}
			// Keep offset
			float offset = x;
			for(UIComponent component : children) {
				component.build(offset, y, component.box.minimal().x() +(component.box.maximal().x()-component.box.minimal().x()) * ratio, h );
				offset += component.box.minimal().x() +(component.box.maximal().x()-component.box.minimal().x()) * ratio;
			}
		}
		// Case over maximal
		else {
			float offset = x;
			// Compute offset position with the alignment
			if(this.horizontal == UIAlignmentHorizontal.LEFT) {
				offset = x;
			}else if(this.horizontal == UIAlignmentHorizontal.CENTER) {
				offset = x + ((w-this.box.maximal().x())/2.0f);
			}else if(this.horizontal == UIAlignmentHorizontal.RIGHT) {
				offset = x + w-this.box.maximal().x();
			}
			for(UIComponent component : children) {
				component.build(offset, y, box.maximal().x() , h);
				offset += box.maximal().x();
			}
		}
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
