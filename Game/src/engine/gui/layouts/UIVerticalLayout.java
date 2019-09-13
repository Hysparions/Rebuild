package engine.gui.layouts;

import engine.gui.core.UIBox;

/**
 * This class defines the Horizontal Layout
 * Vertical Layout stores the children component Vertically,
 * with padding between them, you can specify the
 * @author louis
 *
 */
public class UIVerticalLayout extends UILayout{
	
	/**
	 * Default constructor for the horizontal Layout
	 */
	public UIVerticalLayout() {
		super();
	}
	
	
	/**
	 * Overloaded constructor for the UI Vertical Layout
	 * @param top padding in pixel
	 * @param bottom padding in pixel
	 * @param left padding in pixel
	 * @param right padding in pixel
	 */
	public UIVerticalLayout(float top, float bottom, float left, float right) {
		super(top, bottom, left, right);
	}

	@Override
	public boolean findDesiredSize() {
		boolean modified = false;
		float minX =0.0f, minY = 0.0f, optX = 0.0f, optY = 0.0f, maxX = 0.0f, maxY = 0.0f;
		for(UIBox component : this.children){
			// Calculating minY
			minY += component.minimal().y();
			// Calculating minX
			if(component.minimal().x() > minX) { minX = component.minimal().x(); }
			// Calculating optY
			optY += component.optimal().y();
			// Calculating optX
			if(component.optimal().x() > optX) { optX = component.optimal().x(); }
			// Calculating MaxY
			maxY += component.maximal().y();
			//Calculating MaxX
			if(component.maximal().x() > maxX) { maxX = component.maximal().x(); }
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
		// TODO Auto-generated method stub
		
	}

}
