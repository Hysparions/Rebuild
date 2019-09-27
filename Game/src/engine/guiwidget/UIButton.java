package engine.guiwidget;

import engine.gui.UIBox;
import engine.gui.UIPanel;
import engine.gui.text.UILabel;
import engine.utils.Color;

/**
 * This class is an UI Component acting like a button
 * It has a Label With Text highlighted on Hover
 * @author louis
 *
 */
public class UIButton extends UIPanel{

	/** Label of the panel */
	private UILabel label;
	/** sidePanel Left */
	private UIPanel left;
	/** sidePanel Left */
	private UIPanel right;
	/** font Minimal size */
	private float fontMin;
	/** font Maximal size */
	private float fontMax;
	/** Color */
	private Color color;
	/** font selected Scale >= 1.0f */
	private float scale = 1.2f;
	

	public UIButton(String text, String font, Color color, float fontMin, float fontMax) {
		super();

		this.fontMin = fontMin;
		this.fontMax = fontMax;
		this.color = color;
		// Setting margin
		this.layout().margin(5.0f, 5.0f, 5.0f, 5.0f);
		
		// Creating text label
		this.label = new UILabel(text, font, this.color, this.fontMin, this.fontMax);

		float minX = (scale-1.0f)*label.length(this.fontMin);
		float minY = scale*this.fontMin;
		float maxX = (scale-1.0f)*label.length(this.fontMax);
		float maxY = scale*this.fontMax;
		
		//this.overlay(0,0,200, 100);
		// Create Left panel
		this.left = new UIPanel(new UIBox(minX,minY, maxX, maxY));
		//this.left.overlay(200, 0, 0, 150);
		// Create Right panel
		this.right = new UIPanel(new UIBox(minX,minY, maxX, maxY));
		//this.right.overlay(200, 0, 0, 150);
		
		// Adding component to the button panel
		this.add(left);
		this.add(label);
		this.add(right);

	}
	
	/**
	 * Modify the text color of the button
	 * @param red color component
	 * @param green color component
	 * @param blue color component
	 * @param alpha color component
	 */
	public void textColor(int red, int green, int blue, int alpha) {
		this.label.color().set(red, green, blue, alpha);
	}
	
	
}
