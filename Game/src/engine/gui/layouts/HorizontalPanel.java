package engine.gui.layouts;

/**
 * This Panel stores its component horizontally
 * @author louis
 *
 */
public class HorizontalPanel{
	
	/**
	 *  Default Constructor of the GUI Horizontal Panel class
	 */
	public HorizontalPanel() {
	}

	/**
	 * Constructor of the GUI Horizontal Panel class specifying the following sizing behaviors
	 * @param optimalX	Optimal Horizontal size of the component
	 * @param optimalY	Optimal Vertical size of the component
	 */
	public HorizontalPanel( int optimalX, int optimalY) {
	}

	/**
	 * Constructor of the GUI Horizontal Panel specifying the following sizing behaviors
	 * @param minimalX	Minimal Horizontal size of the component
	 * @param minimalY	Minimal Vertical size of the component
	 * @param optimalX	Optimal Horizontal size of the component
	 * @param optimalY	Optimal Vertical size of the component
	 * @param maximalX	Maximal Horizontal size of the component
	 * @param maximalY  Maximal Vertical size of the component
	 */
	public HorizontalPanel(int minimalX, int minimalY, int optimalX, int optimalY, int maximalX, int maximalY) {
	}
	/**
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
		if(this.minimal().x > w) {
			for(GUIComponent component : children) {
				component.hide();
			}
		}
		// Case between minimal and maximal
		else if(this.minimal().x >= w && this.maximal().x <= w){
			
			// Compute interpolation ratio between min and max
			float ratio;
			if(this.maximal().x() - this.minimal().x() == 0.0f) { 
				ratio = 0.0f;
			}else {
				ratio = (w-this.minimal().x()) / (this.maximal().x() - this.minimal().x());
			}
			// Keep offset
			float offset = x;
			for(GUIComponent component : children) {
				component.show();
				component.build(offset, y, component.minimal().x() + (component.maximal().x()-component.minimal().x()) * ratio , h);
				offset += component.minimal().x() +(component.maximal().x()-component.minimal().x()) * ratio;
			}
		}
		// Case over maximal
		else {
			// Compute offset position
			float offset = x + ((w-this.maximal().x())/2.0f);
			for(GUIComponent component : children) {
				component.show();
				component.build(offset, y, component.maximal().x() , h);
				offset += component.maximal().x();
			}
		}
	}
	*/
	

}
