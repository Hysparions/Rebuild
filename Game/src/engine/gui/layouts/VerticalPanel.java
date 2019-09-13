package engine.gui.layouts;


/**
 * This Panel stores its component vertically
 * @author louis
 *
 */
public class VerticalPanel{
	/**
	 *  Constructor of the GUI Vertical Panel class
	 */
	public VerticalPanel() {
	}

	/**
	 * Constructor of the GUI Vertical Panel class specifying the following sizing behaviors
	 * @param optimalX	Optimal Horizontal size of the component
	 * @param optimalY	Optimal Vertical size of the component
	 */
	public VerticalPanel( int optimalX, int optimalY) {
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
	}
/*

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
*/
}
