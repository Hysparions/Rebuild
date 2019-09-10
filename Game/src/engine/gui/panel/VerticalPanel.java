package engine.gui.panel;

import engine.gui.GUIComponent;

/**
 * This Panel stores its component vertically
 * @author louis
 *
 */
public class VerticalPanel extends ContentPanel{
	/**
	 *  Constructor of the GUI Vertical Panel class
	 */
	public VerticalPanel() {
		super();
	}

	/**
	 * Constructor of the GUI Vertical Panel class specifying the following sizing behaviors
	 * @param optimalX	Optimal Horizontal size of the component
	 * @param optimalY	Optimal Vertical size of the component
	 */
	public VerticalPanel( int optimalX, int optimalY) {
		super(optimalX, optimalY);
	}

	/**
	 * Constructor of the GUI Vertical Panel specifying the following sizing behaviors
	 * @param minimalX	Minimal Horizontal size of the component
	 * @param minimalY	Minimal Vertical size of the component
	 * @param optimalX	Optimal Horizontal size of the component
	 * @param optimalY	Optimal Vertical size of the component
	 * @param maximalX	Maximal Horizontal size of the component
	 * @param maximalY  Maximal Vertical size of the component
	 */
	public VerticalPanel(int minimalX, int minimalY, int optimalX, int optimalY, int maximalX, int maximalY) {
		super(minimalX, minimalY, optimalX, optimalY, maximalX, maximalY);
	}

	@Override
	protected void findDimension() {
		float minX =0.0f, minY = 0.0f, optX = 0.0f, optY = 0.0f, maxX = 0.0f, maxY = 0.0f;
		for(GUIComponent component : this.children){
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

		this.minimal().set(minX, minY);
		this.optimal().set(optX, optY);
		this.maximal().set(maxX, maxY);

	}

	@Override
	public void build(float x, float y, float w, float h) {
		// First set position
		this.position().set(x, y);
		// Then set Size
		this.size().set(w, h);

		if(this.background != null) {
			background.build(x, y, w, h);
		}
		
		this.setBuild(true);
		// Case where there's no place to show the content
		if(this.minimal().y > h) {
			for(GUIComponent component : children) {
				component.hide();
			}
		}
		// Case between minimal and maximal
		else if(this.minimal().y >= h && this.maximal().y <= h){

			// Compute interpolation ratio between min and max
			float ratio;
			if(this.maximal().y() - this.minimal().y() == 0.0f) { 
				ratio = 0.0f;
			}else {
				ratio = (h-this.minimal().y()) / (this.maximal().y() - this.minimal().y());
			}
			// Keep offset
			float offset = y;
			for(GUIComponent component : children) {
				component.show();
				component.build(x, offset, w , component.minimal().y() +(component.maximal().y()-component.minimal().y()) * ratio );
				offset += component.minimal().y() +(component.maximal().y()-component.minimal().y()) * ratio;
			}
		}
		// Case over maximal
		else {
			// Compute offset position
			float offset = y + ((h-this.maximal().y())/2.0f);
			for(GUIComponent component : children) {
				component.show();
				component.build(x, offset, w , component.maximal().y());
				offset += component.maximal().y();
			}
		}
	}

}
