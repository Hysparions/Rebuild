package engine.gui.listeners;

import engine.gui.UIPanel;

/**
 * Use this interface to implement algorithm running
 * when a component is hovered by the mouse
 * @author louis
 *
 */
public interface UIHoverListener{

	/**
	 * This method allows you to implements a behavior
	 * when a panel is hovered by the mouse
	 * It takes the source component as a parameter
	 * @param source
	 */
	public void onHover(UIPanel source);
	
	/**
	 * This method allows you to implements a behavior
	 * when the mouse quits the panel previously hovered
	 * @param source
	 */
	public void onQuit(UIPanel source);
	
}
