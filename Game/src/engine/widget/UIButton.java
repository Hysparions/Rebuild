package engine.widget;

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
	/** font selected Scale */
	private float scale = 1.2f;
	

	public UIButton(String text, String font, float fontMin, float fontMax, Color color) {
		super();
		// Creating text label
		this.label = new UILabel(text, font, color, fontMin, fontMax);
		// Adding text label to the component
		this.add(label);
	}
	
	/**
	 * Over Loaded constructor of the UIBox object
	 * @param minimalWidth dimension in pixel
	 * @param minimalHeight dimension in pixel
	 * @param optimalWidth dimension in pixel
	 * @param optimalHeight dimension in pixel
	 * @param maximalWidth dimension in pixel
	 * @param maximalHeight dimension in pixel
	 */
	public void dimension(float minimalWidth, float minimalHeight, float optimalWidth, float optimalHeight, float maximalWidth, float maximalHeight) {
		this.box.minimal(minimalWidth, minimalHeight);
		this.box.optimal(optimalWidth, optimalHeight);
		this.box.maximal(maximalWidth, optimalHeight);
		
	}
}
