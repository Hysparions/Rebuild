package recover.gui.views;

import engine.gui.UIWindow;
import engine.gui.text.UILabel;
import engine.utils.Color;

/**
 * This Wrapper contains the navigation bar code for 
 * the main menu bar
 * @author louis
 *
 */
public class MainMenuNavigation extends UIWindow{
	
	
	/**
	 * Default Constructor of the Main Navigation Menu
	 */
	public MainMenuNavigation() {
		super("Main Menu Navigation");
		UILabel label = new UILabel("Coucou", "HelvLight", new Color(255, 255, 255, 255), 12, 40);
		this.head().add(label);
		System.out.println(this.head());
	}

	
}
