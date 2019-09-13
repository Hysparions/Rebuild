package recover.gui.views;

import org.joml.Vector2f;

import engine.gui.core.UIBox;
import engine.gui.core.UIComponent;
import engine.gui.core.UIWrapper;
import engine.gui.layouts.UIHorizontalLayout;

/**
 * This Wrapper contains the navigation bar code for 
 * the main menu bar
 * @author louis
 *
 */
public class MainMenuNavigation extends UIWrapper{
	
	
	/**
	 * Default Constructor of the Main Navigation Menu
	 */
	public MainMenuNavigation() {
		super("Main Menu Navigation");
	
		// Setting Horizontal Layout
		this.head().layout(new UIHorizontalLayout());
		this.head().setBoxDimension(400, 30, 800, 40, 1000, 50);
		// Adding First Filler
		this.head().add(new UIComponent(new UIBox(50, 12, 80, 20, 200, 30)));
		this.head().add(new UIComponent(new UIBox(50, 20)));
		this.head().add(new UIComponent(new UIBox(20, 12, 50, 20, 100, 30)));
		this.head().add(new UIComponent(new UIBox(50, 20)));
		this.head().add(new UIComponent(new UIBox(20, 12, 50, 20, 100, 30)));
		this.head().add(new UIComponent(new UIBox(50, 20)));
		this.head().add(new UIComponent(new UIBox(50, 12, 80, 20, 200, 30)));
				
	}

	@Override
	public void resizeAndPosition(float width, float height) {
		
		float availableWidth = width/1.6f;
		float availableHeight = height/20.0f;
		
		// Main nav bar should be taking 10/16 width and 1/12 height
		Vector2f min = this.head().boxMinimal();
		Vector2f max = this.head().boxMaximal();
		float x, y, w, h;
		if (availableWidth < min.x()) {
			w = min.x();
		} else if (availableWidth > max.x()) {
			w = max.x();
		} else {
			w = availableWidth;
		}
		x = (width - w) / 2;
		if (availableHeight < min.y()) {
			h = min.y();
		} else if (availableHeight > max.y()) {
			h = max.y();
		} else {
			h = availableHeight;
		}
		y = 0.0f;
		this.setWrapperPosition(x, y);
		this.setWrapperSize(w, h);
		//System.out.println(this.head());
	}
	
	
}
