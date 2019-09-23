package engine.gui.listeners;

import engine.gui.UIPanel;

/**
 * Use this interface to implement algorithm running
 * when a panel hovered detect scroll input from mouse
 * @author louis
 *
 */
public interface UIScrollListener {

	/**
	 * Implements this function to give a behavior to your panel
	 * when scroll action is detected on it
	 * @param source panel
	 * @param scroll amount
	 */
	public void onScroll(UIPanel source, double scroll);
}
