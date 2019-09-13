package engine.gui.layouts;

import engine.gui.core.UIBox;

/**
 * This class defines the Horizontal Layout
 * Horizontal Layout stores the children component horizontally,
 * with padding between them, you can specify the
 * @author louis
 *
 */
public class UIHorizontalLayout extends UILayout{
	
	
	/**
	 * Default constructor for the horizontal Layout
	 */
	public UIHorizontalLayout() {
		super();
	}
	
	
	/**
	 * Overloaded constructor for the UI Horizontal Layout
	 * @param top padding in pixel
	 * @param bottom padding in pixel
	 * @param left padding in pixel
	 * @param right padding in pixel
	 */
	public UIHorizontalLayout(float top, float bottom, float left, float right) {
		super(top, bottom, left, right);
	}
	
	
	@Override
	public boolean findDesiredSize() {
		boolean modified = false;
		float minX =0.0f, minY = 0.0f, optX = 0.0f, optY = 0.0f, maxX = 0.0f, maxY = 0.0f;
		for(UIBox component : this.children){
			// Calculating minX
			minX += component.minimal().x();
			// Calculating minY
			if(component.minimal().y() > minY) { minY = component.minimal().y(); }
			// Calculating optX
			optX += component.optimal().x();
			// Calculating optY
			if(component.optimal().y() > optY) { optY = component.optimal().y(); }
			// Calculating MaxX
			maxX += component.maximal().x();
			//Calculating MaxY
			if(component.maximal().y() > maxY) { maxY = component.maximal().y(); }
			
		}

		minX += this.right+this.left;
		optX += this.right+this.left;
		maxX += this.right+this.left;
		
		minY += this.bottom+this.top;
		optY += this.bottom+this.top;
		maxY += this.bottom+this.top;
		
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

	@Override
	public void findChildrenSizes() {
		
	}


	
}
